plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.estacionamientoapp"
    compileSdk = 34
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.estacionamientoapp"
        minSdk = 23
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
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.fragment:fragment:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation ("com.makeramen:roundedimageview:2.3.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    implementation ("com.google.zxing:core:3.5.0")
    implementation("com.google.ar.sceneform:rendering:1.17.1")
    implementation("androidx.room:room-runtime:2.5.2")
    implementation ("com.google.firebase:firebase-database")
    implementation ("com.google.firebase:firebase-auth")
    implementation("androidx.media3:media3-common:1.4.1")
    implementation("com.google.firebase:firebase-analytics:22.1.0")
    implementation("com.google.firebase:firebase-firestore:25.1.0")
    implementation (platform("com.google.firebase:firebase-bom:32.2.3"))
    implementation ("com.google.firebase:firebase-firestore")
    implementation ("com.google.firebase:firebase-database")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    testImplementation("junit:junit:4.13.2")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    annotationProcessor("androidx.room:room-compiler:2.5.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}