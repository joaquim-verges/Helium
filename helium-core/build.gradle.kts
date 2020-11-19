plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
}

version = (project.properties["VERSION_NAME"] as String)
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

    sourceSets {

        all {
            languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
        }

        val commonMain by getting {
            dependencies {
                implementation(Deps.stdlib)
                api(Deps.coroutines)
            }
        }

        val androidMain by getting {
            dependencies {
                Deps.lifecycleDeps.forEach(::api)
                api(Deps.coroutinesAndroid)
            }
        }

        val androidTest by getting

        val iosMain by getting {
            dependencies {
                api(Deps.coroutineNative)
            }
        }
        val iosTest by getting
    }

    cocoapods {
        // Configure fields required by CocoaPods.
        summary = "CocoaPod for Helium Core on iOS"
        homepage = "https://github.com/joaquimverges/Helium"

        // The name of the produced framework can be changed.
        // The name of the Gradle project is used here by default.
        frameworkName = ios_framework_name
        noPodspec() // avoid overriding the existing podspec
    }
}

androidForMultiplatformLib()

apply("../maven-push.gradle")
