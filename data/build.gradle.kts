plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
  androidTarget()
  jvm()
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.timber)
        implementation(projects.model)
        implementation(projects.domain)
        implementation(projects.datasource.remote)
        implementation(projects.datasource.local)
        implementation(libs.kotlin.coroutines)
        implementation(libs.kotlin.datetime)
      }
    }
    androidMain {
      dependencies {
        api(libs.gms.location)
      }
    }
    jvmTest {
      dependencies {
        implementation(libs.kotlin.test)
        implementation(libs.kotlin.test.junit)
        implementation(libs.kotlin.coroutine.test)
        implementation(libs.mockk)
        implementation(libs.turbine)
      }
    }
  }

  // https://youtrack.jetbrains.com/issue/KT-61573
  compilerOptions {
    freeCompilerArgs.add("-Xexpect-actual-classes")
  }
}

android {
  namespace = "io.github.castrokingjames.data"
  compileSdk = 34
  defaultConfig {
    minSdk = 24
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
