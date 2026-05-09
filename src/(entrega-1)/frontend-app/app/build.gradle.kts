plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.projeto8"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.projeto8"
        minSdk = 24
        targetSdk = 36
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.glide)// adiciona a biblioteca GLIDE, baixa imagem, redimensiona, salva em cache e coloca no imgview, uma mão na roda
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)
   // implementation("androidx.media3:media3-exoplayer:1.3.1")
    //implementation("androidx.media3:media3-ui:1.3.1")
}
