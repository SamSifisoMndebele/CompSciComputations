import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.google.dagger.hilt.android)
    id("kotlin-kapt")
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

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField("String", "SUPABASE_URL", "\"${properties.getProperty("supabase_url")}\"")
        buildConfigField("String", "SUPABASE_KEY", "\"${properties.getProperty("supabase_key")}\"")
        buildConfigField("String", "WEB_CLIENT_ID", "\"${properties.getProperty("web_google_client_id")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
//            isMinifyEnabled = true
//            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
//            isMinifyEnabled = true
//            isShrinkResources = true
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
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
    dynamicFeatures += setOf(
        ":features:karnaugh_maps",
        ":features:number_systems",
        ":features:polish_expressions",
        ":features:matrix_methods"
    )
}

dependencies {

    implementation(libs.hilt.android)
    implementation(libs.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //Machine Learning
    //implementation (libs.tensorflow.lite.task.vision.play.services)
    //implementation (libs.play.services.tflite.gpu)
    //implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")

    //Firebase DB
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.auth)
//    implementation(libs.firebase.storage)
//    implementation(libs.firebase.firestore)

    //Supabase DB
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.storage.kt)
//    implementation(libs.supabase.gotrue.kt)
    implementation(libs.supabase.postgrest.kt)
    implementation(libs.supabase.realtime.kt)

    //Local SQLite DB
    implementation (libs.androidx.room.runtime)
    annotationProcessor (libs.androidx.room.compiler)
    testImplementation (libs.androidx.room.testing)

    implementation(libs.ktor.client.android)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    implementation(libs.glide.compose)
    implementation(libs.coil.compose)
    implementation(libs.library)
    implementation(libs.lottie.compose)
    implementation(libs.zoomlayout)

    implementation(libs.gson)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended) //Takes more space
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
//    implementation(project(":maths_lib"))
    implementation(project(":pdf_viewer"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}