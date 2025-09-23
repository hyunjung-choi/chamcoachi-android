plugins {
    id("hyunjung.chamcoach.android.application")
    id("hyunjung.chamcoach.android.application.compose")
    id("hyunjung.chamcoach.android.hilt")
    id("hyunjung.chamcoach.spotless")
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.hyunjung.chamcoach"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.hyunjung.chamcoach"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // features
//    implementation(projects.feature.home)
//    implementation(projects.feature.details)
//
//    // cores
//    implementation(projects.core.model)
    implementation(project(":core:designsystem"))
//    implementation(projects.core.navigation)

    // compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)

    // datastore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    implementation(libs.kotlinx.serialization.json)

    // di
    implementation(libs.hilt.android)
    implementation(libs.androidx.ui.tooling)
    ksp(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.testing)
    kspAndroidTest(libs.hilt.compiler)

    // unit test
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)
}