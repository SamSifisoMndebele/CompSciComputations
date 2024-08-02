plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.jetbrains.kotlin.compose.compiler)
    alias(libs.plugins.google.protobuf)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    id("androidx.navigation.safeargs")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("D:\\_CompSciComputations\\android-app\\config\\keystore\\keystore.jks")
            storePassword = "Mn_9903155459080"
            keyAlias = "compscicomputationskey"
            keyPassword = "CompSciComputations"
        }
    }
    namespace = "com.compscicomputations"
    compileSdk = 34

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/proto")
        }
    }

    defaultConfig {
        applicationId = "com.compscicomputations"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "1.0.3"

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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.26.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java")
            }
        }
    }
}

dependencies {

    /** Auth */
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

//    /**Machine Learning*/
//    implementation(libs.generativeai)
//    implementation(libs.play.text.recognition)
//    implementation(libs.play.services.mlkit.text.recognition)
//    implementation(libs.play.services.tflite.gpu)
//    implementation(libs.tensorflow.lite.task.vision.play.services)

    /**Hilt DI*/
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    /**Local database*/
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.java)

    /**Firebase*/
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
//    implementation(libs.firebase.auth)
//    implementation(libs.firebase.config)
//    implementation(libs.firebase.storage)
//    implementation(libs.firebase.firestore)

    /**Local Modules*/
    implementation(project(":pdf-viewer"))
    implementation(project(":core:client"))

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