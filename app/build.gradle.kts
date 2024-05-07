import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    kotlin("plugin.serialization") version libs.versions.kotlin
}

android {
    namespace = "com.compscicomputations"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.compscicomputations"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        val properties = Properties()
        release {
            properties.load(project.rootProject.file("local.properties").inputStream())

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String",
                "SUPABASE_KEY", "\"${properties.getProperty("supabase_key")}\"")
        }
        debug {
            isMinifyEnabled = false
            buildConfigField("String",
                "SUPABASE_KEY", "\"${properties.getProperty("supabase_key")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
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
    //implementation (libs.tensorflow.lite.task.vision.play.services)
    //implementation (libs.play.services.tflite.gpu)
    implementation(libs.text.recognition)
    //implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)

    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.compose.auth)
    implementation(libs.supabase.storage.kt)
    implementation(libs.supabase.gotrue.kt)
    implementation(libs.supabase.postgrest.kt)
    implementation(libs.supabase.realtime.kt)

    implementation(libs.glide.compose)
    implementation(libs.coil.compose)
    implementation(libs.library)
    implementation(libs.lottie.compose)
    implementation(libs.zoomlayout)
    implementation(libs.pdfbox.android)
    api(libs.pdfium.android)

    implementation(libs.gson)
    implementation(libs.androidx.core.ktx)
    //implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.ktor.client)
    implementation(project(":KarnaughMap"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}