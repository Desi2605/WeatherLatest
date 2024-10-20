plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

android {
    compileSdk = 34

    namespace = "com.example.weatherapp"

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }

    kotlinOptions {
        jvmTarget = "11"
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
}

 dependencies {
    // Core AndroidX components
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose libraries
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Add Compose foundation for horizontalScroll
    implementation("androidx.compose.foundation:foundation:1.5.0")

     // Room components for database management
     implementation("androidx.room:room-runtime:2.6.1")
     implementation(libs.androidx.espresso.core)
     implementation(libs.androidx.constraintlayout.compose)
     kapt("androidx.room:room-compiler:2.6.1")
     implementation("androidx.room:room-ktx:2.6.1") // Coroutines support

// Coroutines
     implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
     implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

     // Retrofit for networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debugging tools
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


     debugImplementation("androidx.compose.ui:ui-tooling:1.7.3")
     implementation("androidx.compose.ui:ui-tooling-preview:1.7.3")
}





