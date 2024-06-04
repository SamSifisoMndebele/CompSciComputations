import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
    id("kotlin-kapt")
}

android {
    namespace = "com.compscicomputations.core.database"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField("String", "SUPABASE_URL", "\"${properties.getProperty("supabase_url")}\"")
        buildConfigField("String", "SUPABASE_KEY", "\"${properties.getProperty("supabase_key")}\"")
        buildConfigField("String", "WEB_CLIENT_ID", "\"${properties.getProperty("web_google_client_id")}\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    /**Hilt DI*/
    implementation (libs.hilt.android)
    kapt (libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    /**SQLite Room DB*/
    implementation (libs.androidx.room.runtime)
    annotationProcessor (libs.androidx.room.compiler)
    testImplementation (libs.androidx.room.testing)

    /**Firebase*/
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)
//    implementation(libs.firebase.storage)
//    implementation(libs.firebase.firestore)

    /**Supabase*/
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.storage.kt)
//    implementation(libs.supabase.gotrue.kt)
    implementation(libs.supabase.postgrest.kt)
    implementation(libs.supabase.realtime.kt)

    implementation(libs.ktor.client.android)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}