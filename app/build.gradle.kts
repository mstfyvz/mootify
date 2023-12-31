plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "com.sebnem.mootify"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sebnem.mootify"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
    }
}

dependencies {

    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.28")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //dbflow library for sqlite
    implementation ("com.github.Raizlabs.DBFlow:dbflow-core:4.2.4")
    implementation ("com.github.Raizlabs.DBFlow:dbflow:4.2.4")
    implementation ("com.github.Raizlabs.DBFlow:dbflow-kotlinextensions:4.2.4")
    kapt ("com.github.Raizlabs.DBFlow:dbflow-processor:4.2.4")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.0")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
}
