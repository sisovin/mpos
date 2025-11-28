plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "co.peanech.mpos.core.data"
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
    implementation(project(":core-network"))
    implementation(project(":core-db"))
    implementation(project(":core-common"))
    implementation(libs.kotlinx.coroutines)
    implementation("javax.inject:javax.inject:1")
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
