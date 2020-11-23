plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
}

apply(plugin = "com.vanniktech.maven.publish")

group = (project.properties["GROUP"] as String)
version = (project.properties["VERSION_NAME"] as String)
val ios_framework_name = "HeliumCore"

kotlin {

    iosX64("ios") {
        val target = this
        binaries {
            framework(ios_framework_name) {
                freeCompilerArgs = freeCompilerArgs + "-Xobjc-generics"
            }
        }
        mavenPublication {
            artifactId = "${project.name}-iosx64"
        }
    }

    android {
        publishAllLibraryVariants()
        publishLibraryVariantsGroupedByFlavor = true
    }

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
            }
        }

        val iosMain by getting {
            dependencies {
                // none for noe
            }
        }
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

fun getProp(prop: String) = (project.properties[prop] as String)
