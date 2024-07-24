/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/AndroidxTypescriptRunner/blob/trunk/LICENSE
 */

package androidx.typescript.runner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Text("Hello, World!")

      LaunchedEffect(Unit) {
        val engine = JsEngine.create(applicationContext)
        val result = engine.ts2js(
          """ 
          |class Money {
          |    money: number;
          |
          |    constructor(money: number) {
          |        this.money = money
          |    }
          |
          |    getMoney(): number {
          |        return this.money;
          |    }
          |
          |    addMoney(amount: number) {
          |        this.money += amount
          |    }
          |}
          |
          |let money = new Money(100)
          |console.log(money.getMoney())
          |money.addMoney(100)
          |console.log(money.getMoney())
          """.trimMargin(),
        )
        println("success: " + result.getOrNull())
        println("failure: " + result.exceptionOrNull())

        result.getOrNull()?.let { js ->
          engine.js(js).let { jsResult ->
            println("js success: ${jsResult.getOrNull()}")
            println("js failure: ${jsResult.exceptionOrNull()}")
          }
        }
      }
    }
  }
}
