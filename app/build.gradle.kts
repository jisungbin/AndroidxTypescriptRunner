/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/AndroidxTypescriptRunner/blob/trunk/LICENSE
 */

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("plugin.compose")
}

android {
  namespace = "androidx.typescript.runner"
  compileSdk = 34

  defaultConfig {
    minSdk = 26
    targetSdk = 34
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  sourceSets {
    getByName("main").java.srcDir("src/main/kotlin")
  }

  packaging {
    resources {
      excludes.add("**/*.kotlin_builtins")
    }
  }
}

composeCompiler {
  enableStrongSkippingMode = true
  enableNonSkippingGroupOptimization = true
}

dependencies {
  implementation(libs.androidx.activity)
  implementation(libs.androidx.jsEngine)

  implementation(libs.compose.activity)
  implementation(libs.compose.material3)

  implementation(libs.kotlin.coroutines)

  testImplementation(kotlin("test-junit5"))
  testImplementation(libs.test.kotlin.coroutines)
}
