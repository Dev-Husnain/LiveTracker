plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.mapsPlatformPlugin)
    kotlin("plugin.serialization").version("2.1.20")
}

android {
    namespace = "com.location.livetracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.location.livetracker"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }

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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.lifecycleLivedataKtx)
    implementation(libs.lifecycleProcess)
    implementation(libs.koin)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.googleMap)
    implementation(libs.bundles.ktor)
}