import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
}

// TODO extract
val compile_sdk = 28
val min_sdk = 14
val target_sdk = 28

val kotlin_version = "1.4.0"
val coroutines_version = "1.3.9"
val arch_lifecycle_version = "2.2.0"
val arch_lifecycle_runtime_version = "2.3.0-alpha05"
val arch_lifecycle_viewmodel_version = "2.3.0-alpha05"

val ios_framework_name = "HeliumCore"

kotlin {

    ios {
        binaries {
            framework(ios_framework_name) {
                freeCompilerArgs = freeCompilerArgs + "-Xobjc-generics"
            }
        }
    }

    android()

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    }

    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        api("androidx.lifecycle:lifecycle-viewmodel-ktx:$arch_lifecycle_viewmodel_version")
        api("androidx.lifecycle:lifecycle-runtime-ktx:$arch_lifecycle_runtime_version")
        api("androidx.lifecycle:lifecycle-extensions:$arch_lifecycle_version")
    }

    sourceSets["iosMain"].apply {
        dependsOn(sourceSets["commonMain"])
//        api("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutines_version")
    }

    cocoapods {
        // Configure fields required by CocoaPods.
        summary = "CocoaPod for Helium Core on iOS"
        homepage = "https://github.com/joaquimverges/Helium"

        // The name of the produced framework can be changed.
        // The name of the Gradle project is used here by default.
        frameworkName = "$ios_framework_name"
    }
}

android {
    compileSdkVersion(compile_sdk)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        minSdkVersion(min_sdk)
        targetSdkVersion(target_sdk)
        versionCode = (project.properties["VERSION_CODE"] as String).toInt()
        versionName = project.properties["VERSION_NAME"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    testOptions.unitTests.isIncludeAndroidResources = true

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
        }
        getByName("androidTest") {
            java.srcDirs("src/androidTest/kotlin")
            res.srcDirs("src/androidTest/res")
        }
    }
}

// TODO
//apply("../maven-push.gradle")