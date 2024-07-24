@file:Suppress("UnstableApiUsage")

package androidx.typescript.runner

import android.content.Context
import androidx.annotation.NonUiContext
import androidx.javascriptengine.JavaScriptConsoleCallback
import androidx.javascriptengine.JavaScriptSandbox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.Language

@Suppress("RequiresFeature")
class JsEngine private constructor(
  private val context: Context,
  private val sandbox: JavaScriptSandbox,
) : AutoCloseable by sandbox {
  private val tsRunner = sandbox.createIsolate().apply {
    setConsoleCallback(JavaScriptConsoleCallback { console ->
      println("tsRunner: ${console.message}")
    })
  }
  private val jsRunner = sandbox.createIsolate().apply {
    setConsoleCallback(JavaScriptConsoleCallback { console ->
      println("jsRunner: ${console.message}")
    })
  }

  suspend fun js(@Language("js") code: String) = runCatching {
    jsRunner.evaluateJavaScriptAsync(code).get()
  }

  suspend fun ts2js(@Language("ts") code: String) = runCatching {
    @Language("json") val compileOptions = """
    { 
      "compilerOptions": {
        "module": "NONE",
        "target": "ES3",
        "moduleResolution": "classic"
      }
    }
    """.trimIndent().replace("\n", " ").also {
      println("compileOptions: $it")
    }

    tsRunner.evaluateJavaScriptAsync(
      StringBuilder().apply {
        val ts = context.assets.open("typescript.js")
          .bufferedReader()
          .use { it.readText() }
          .also { println("typescript.js: ${it.length} length") }
        append(ts)
        appendLine("console.log('typescript.js loaded')")
        append("""ts.transpileModule(`$code`, $compileOptions).outputText""")
      }.toString(),
    ).get()
  }

  companion object {
    suspend fun create(@NonUiContext context: Context): JsEngine = coroutineScope {
      val sandbox = withContext(Dispatchers.IO) {
        JavaScriptSandbox.createConnectedInstanceAsync(context).get()
      }
      JsEngine(context, sandbox)
    }
  }
}