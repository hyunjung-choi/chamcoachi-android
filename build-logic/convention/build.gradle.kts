plugins {
  `kotlin-dsl`
}

group = "com.hyunjung.chamcoachi.buildlogic"

dependencies {
  compileOnly(libs.android.gradlePlugin)
  compileOnly(libs.compose.compiler.gradlePlugin)
  compileOnly(libs.kotlin.gradlePlugin)
  compileOnly(libs.spotless.gradlePlugin)
}

gradlePlugin {
  plugins {
    register("androidApplicationCompose") {
      id = "hyunjung.chamcoachi.android.application.compose"
      implementationClass = "AndroidApplicationComposeConventionPlugin"
    }
    register("androidApplication") {
      id = "hyunjung.chamcoachi.android.application"
      implementationClass = "AndroidApplicationConventionPlugin"
    }
    register("androidLibraryCompose") {
      id = "hyunjung.chamcoachi.android.library.compose"
      implementationClass = "AndroidLibraryComposeConventionPlugin"
    }
    register("androidLibrary") {
      id = "hyunjung.chamcoachi.android.library"
      implementationClass = "AndroidLibraryConventionPlugin"
    }
    register("androidFeature") {
      id = "hyunjung.chamcoachi.android.feature"
      implementationClass = "AndroidFeatureConventionPlugin"
    }
    register("androidHilt") {
      id = "hyunjung.chamcoachi.android.hilt"
      implementationClass = "AndroidHiltConventionPlugin"
    }
    register("spotless") {
      id = "hyunjung.chamcoachi.spotless"
      implementationClass = "SpotlessConventionPlugin"
    }
  }
}
