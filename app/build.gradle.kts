import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.jetbrains.kotlin.compose.compiler)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    id("androidx.navigation.safeargs")
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
        vectorDrawables { useSupportLibrary = true }

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        buildConfigField("String", "GENERATIVE_AI_KEY", properties.getProperty("generative_ai_key"))
        buildConfigField("String", "WEB_CLIENT_ID", properties.getProperty("web_google_client_id"))
    }

    buildTypes {
        release {
//            isMinifyEnabled = true
//            isShrinkResources = true
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
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
    dynamicFeatures += setOf(
        ":feature:karnaugh_maps",
        ":feature:matrix_methods",
        ":feature:number_systems",
        ":feature:polish_expressions"
    )
}

dependencies {
    /** Auth */
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    /**Machine Learning*/
    implementation(libs.play.services.mlkit.text.recognition)
    implementation(libs.generativeai)
//    implementation (libs.tensorflow.lite.task.vision.play.services)
//    implementation (libs.play.services.tflite.gpu)

    /**Hilt DI*/
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    /**Local database*/
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)

    /**Firebase*/
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.config)

    /**Local Modules*/
    implementation(project(":pdf-viewer"))
    implementation(project(":core:ktor-client"))

    /**Android Modules*/
//    implementation("com.google.android.play:review:2.0.1")
//    implementation("com.google.android.play:asset-delivery:2.2.2")
//    implementation("com.google.android.play:app-update:2.1.0")
    implementation(libs.google.feature.delivery)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.pullrefresh)
    implementation(libs.androidx.material.icons.extended) //Takes more space
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.dynamic.features.fragment)

    /**Testing*/
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.androidx.room.testing)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /**Other Modules*/
    implementation(libs.glassmorphic.composables)
    implementation(libs.lottie.compose)
    implementation(libs.coil.compose)
//    implementation(libs.glide.compose)
//    implementation("androidx.paging:paging-runtime:3.3.0")
//    implementation("androidx.paging:paging-compose:3.3.0")
}