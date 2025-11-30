import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.21"
}

android {
    namespace = "com.mustafa.myapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mustafa.myapplication"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val props = Properties().apply {
            load(FileInputStream(rootProject.file("local.properties")))
        }
        buildConfigField("String", "TMDB_API_KEY", "\"${props["TMDB_API_KEY"]}\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    implementation(libs.coroutines.android)
    implementation(libs.lifecycle.viewmodel.ktx)

    implementation(libs.glide)
    implementation(libs.coil.compose)

    implementation(libs.navigation.compose)
    implementation(libs.navigation.runtime)
    implementation(libs.navigation.common)
    implementation(libs.navigation.compose.beta)

    implementation(libs.lifecycle.viewmodel.compose)

    implementation(libs.compose.icons.extended)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)

    implementation(libs.material3)
    implementation(libs.kotlinx.serialization.json)




}