import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

version = "1.0"
val ios_framework_name = "NewsCommon"

kotlin {

    android()
    iosX64("ios")

    sourceSets["commonMain"].apply {
        dependencies {
            api(project(":helium-core"))
            Deps.ktorCommonDeps.forEach(::implementation)
            Deps.sqlDelightCommonDeps.forEach(::implementation)
        }
    }

    sourceSets["androidMain"].dependencies {
        implementation(Deps.ktorAndroid)
        implementation(Deps.sqlDelightAndroid)
        implementation(Deps.coroutinesAndroid)
    }

    sourceSets["iosMain"].dependencies {
        implementation(Deps.ktorIOS)
        implementation(Deps.sqlDelightIos)
    }

    cocoapods {
        // Configure fields required by CocoaPods.
        summary = "Common code for multiplatform news app"
        homepage = "https://github.com/joaquimverges/Helium/samples/multiplatform_app/common"

        // The name of the produced framework can be changed.
        // The name of the Gradle project is used here by default.
        frameworkName = ios_framework_name

        // TODO experiment with having core as a pod dependency to get the swift files
        // pod("helium_core", podspec = project.file("../../../helium-core/helium_core.podspec"))
        podfile = project.file("../ios/Podfile")
    }

    targets.withType<KotlinNativeTarget> {
        binaries.withType<Framework> {
            transitiveExport = true
            export(project(":helium-core"))
        }
    }
}

androidForMultiplatformLib()

sqldelight {
    database("HeliumNewsDB") {
        packageName = "com.joaquimverges.kmp.news"
        sourceFolders = listOf("sqldelight")
    }
}
