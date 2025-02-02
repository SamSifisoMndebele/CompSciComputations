plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.jetbrains.kotlin.compose.compiler)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.google.libraries.mapsplatform.secrets)
    id("androidx.navigation.safeargs")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("D:\\keystore.jks")
            keyAlias = "compscicomputationskey"
            storePassword = "Mn_9903155459080"
            keyPassword = "CompSciComputations"
        }
    }
    namespace = "com.compscicomputations"
    compileSdk = 35

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/proto")
        }
    }

    defaultConfig {
        applicationId = "com.compscicomputations"
        minSdk = 24
        targetSdk = 35
        versionCode = 11
        versionName = "0.11.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        release {
            isShrinkResources = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
        buildConfig = true
        viewBinding = true
    }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    dynamicFeatures += setOf(
        ":feature:karnaugh_maps",
        ":feature:number_systems",
        ":feature:polish_expressions",
//        ":feature:matrix_methods",
//        ":feature:questions"
    )
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {

    /**Machine Learning*/
    implementation(libs.generativeai)
//    implementation(libs.text.recognition)
//    implementation(libs.play.services.mlkit.text.recognition)
//    implementation(libs.play.services.tflite.gpu)
//    implementation(libs.tensorflow.lite.task.vision.play.services)

    /** Auth */
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    /**Hilt DI*/
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    /**Local database*/
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.datastore.preferences)

    /**Firebase*/
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
//    implementation(libs.firebase.firestore)
//    implementation(libs.firebase.config)
//    implementation(libs.firebase.storage)

    /**Local Modules*/
    implementation(project(":core:pdf-viewer"))
    implementation(project(":core:client"))
    implementation(project(":core:keyboard"))

    /**Android Modules*/
//    implementation("com.google.android.play:review:2.0.1")
//    implementation("com.google.android.play:asset-delivery:2.2.2")
//    implementation("com.google.android.play:app-update:2.1.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.feature.delivery)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.viewbinding)
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
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /**Other Modules*/
    implementation(libs.compose.markdown)
    implementation(libs.glassmorphic.composables)
    implementation(libs.lottie.compose)
    implementation(libs.coil.compose)
//    implementation(libs.glide.compose)
//    implementation("androidx.paging:paging-runtime:3.3.0")
//    implementation("androidx.paging:paging-compose:3.3.0")
}