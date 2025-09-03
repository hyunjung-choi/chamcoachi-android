plugins {
  `kotlin-dsl`
}

group = "com.hyunjung.chamcoach.buildlogic"

dependencies {
  compileOnly(libs.android.gradlePlugin)
  compileOnly(libs.compose.compiler.gradlePlugin)
  compileOnly(libs.kotlin.gradlePlugin)
  compileOnly(libs.spotless.gradlePlugin)
}

gradlePlugin {
  plugins {
    register("androidApplicationCompose") {
      id = "hyunjung.chamcoach.android.application.compose"
      implementationClass = "AndroidApplicationComposeConventionPlugin"
    }
    register("androidApplication") {
      id = "hyunjung.chamcoach.android.application"
      implementationClass = "AndroidApplicationConventionPlugin"
    }
    register("androidLibraryCompose") {
      id = "hyunjung.chamcoach.android.library.compose"
      implementationClass = "AndroidLibraryComposeConventionPlugin"
    }
    register("androidLibrary") {
      id = "hyunjung.chamcoach.android.library"
      implementationClass = "AndroidLibraryConventionPlugin"
    }
    register("androidFeature") {
      id = "hyunjung.chamcoach.android.feature"
      implementationClass = "AndroidFeatureConventionPlugin"
    }
    register("androidHilt") {
      id = "hyunjung.chamcoach.android.hilt"
      implementationClass = "AndroidHiltConventionPlugin"
    }
    register("spotless") {
      id = "hyunjung.chamcoach.spotless"
      implementationClass = "SpotlessConventionPlugin"
    }
  }
}
