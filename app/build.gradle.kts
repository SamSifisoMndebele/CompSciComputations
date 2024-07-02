plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.jetbrains.kotlin.compose.compiler)
    alias(libs.plugins.google.devtools.ksp)
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
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    dynamicFeatures += setOf(
        ":feature:karnaugh_maps",
        ":feature:matrix_methods",
        ":feature:number_systems",
        ":feature:polish_expressions"
    )
}

dependencies {
    implementation(libs.postgresql)
//    implementation("androidx.paging:paging-runtime:3.3.0")
//    implementation("androidx.paging:paging-compose:3.3.0")

    /**Machine Learning*/
//    implementation (libs.tensorflow.lite.task.vision.play.services)
//    implementation (libs.play.services.tflite.gpu)
//    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")

    /**Hilt DI*/
    implementation(libs.hilt.android)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    /**Local database*/
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)

    /**Firebase*/
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(project(":pdf-viewer"))

    implementation(project(":core:ktor-client"))

//    implementation(libs.glide.compose)
    implementation(libs.coil.compose)
    implementation(libs.lottie.compose)

    implementation(libs.kotlinx.serialization.json)
    implementation("com.google.android.play:feature-delivery:2.1.0")
//    implementation("com.google.android.play:review:2.0.1")
//    implementation("com.google.android.play:asset-delivery:2.2.2")
//    implementation("com.google.android.play:app-update:2.1.0")
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
    implementation(libs.androidx.navigation.compose)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}