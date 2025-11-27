plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "co.peanech.mpos.core.network"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        targetSdk = 36
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":core-common"))
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.moshi)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines)
}
