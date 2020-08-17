import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

version = "1.0"
val ios_framework_name = "NewsCommon"
val ktor_version = "1.3.2-1.4.0-rc"

// TODO extract
val compile_sdk = 28
val min_sdk = 14
val target_sdk = 28

val kotlin_version = "1.4.0"
val arch_lifecycle_version = "2.2.0"
val arch_lifecycle_runtime_version = "2.3.0-alpha05"
val arch_lifecycle_viewmodel_version = "2.3.0-alpha05"

kotlin {

    android()

    ios {
        binaries {

            framework(ios_framework_name) {
                // FIXME these below get ignored by the cocoapod sync
                isStatic = false
                transitiveExport = true
                export(project(":helium-core"))
                freeCompilerArgs = freeCompilerArgs + "-Xobjc-generics"
            }
            // FIXME these below is probably useless
            sharedLib {
                // It's possible to export different sets of dependencies to different binaries.
                export(project(":helium-core"))
            }
        }
    }

    sourceSets["commonMain"].apply {
        dependencies {
            api(project(":helium-core"))
            implementation("io.ktor:ktor-client-core:$ktor_version")
        }
    }

    sourceSets["androidMain"].dependencies {
        implementation("io.ktor:ktor-client-android:$ktor_version")
    }

    sourceSets["iosMain"].apply {
        dependsOn(sourceSets["commonMain"])
        dependencies {
            implementation("io.ktor:ktor-client-ios:$ktor_version")
        }
    }

    cocoapods {
        // Configure fields required by CocoaPods.
        summary = "Common code for multiplatform news app"
        homepage = "https://github.com/joaquimverges/Helium/samples/multiplatform_app/common"

        // The name of the produced framework can be changed.
        // The name of the Gradle project is used here by default.
        frameworkName = ios_framework_name

        // TODO experiment with having core as a pod dependency to get the swift files
        //pod("helium_core", podspec = project.file("../../../helium-core/helium_core.podspec"))
        podfile = project.file("../ios/Podfile")
    }

    targets.withType<KotlinNativeTarget> {
        binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework> {
            // isStatic = false FIXME this break things
            // !! These 2 lines below are what makes transitive deps wor
            transitiveExport = true
            export(project(":helium-core"))
        }
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