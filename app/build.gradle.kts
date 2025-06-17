plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.21"
}

android {
    namespace = "org.thelauncher.sculptlauncher"
    compileSdk = 36
    ndkVersion = "28.1.13356709"

    packaging {
        resources.excludes.add("META-INF/DEPENDENCIES")
        jniLibs.pickFirsts.add("**/libshadowhook.so")
        jniLibs.pickFirsts.add("**/libshadowhook_nothing.so")
    }

    externalNativeBuild {
        cmake {
            version = "3.31.6"
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }

    defaultConfig {
        applicationId = "org.thelauncher.sculptlauncher"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        externalNativeBuild {
            cmake {
                cppFlags += ""
            }
        }

        ndk {
            abiFilters.add("arm64-v8a")
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Androidx libs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // added Androidx libs
    implementation(libs.androidx.core.splashscreen) // splash screen
    implementation(libs.androidx.compose.material.icons) // icons
    implementation(libs.androidx.datastore.preferences) // preference
    implementation(libs.androidx.navigation.compose) // navigation

    // App self-dep
    implementation(project(":minecraftpe"))
    implementation(project(":microsoft:xal"))
    implementation(project(":hooker"))
    implementation(libs.play.services.gcm)
    implementation(libs.play.services.iid)
    implementation(files("../minecraftpe/src/main/libs/appsflyer.jar"))

    implementation(libs.kotlinx.serialization.json) // serialization

    // Testing libs
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}