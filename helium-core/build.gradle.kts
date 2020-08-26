
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

    sourceSets["commonMain"].dependencies {
        implementation(Deps.stdlib)
        api(Deps.coroutines)
    }

    sourceSets["androidMain"].dependencies {
        Deps.lifecycleDeps.forEach(::api)
    }

    sourceSets["iosMain"].dependencies {
        // none for now
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
// TODO
//apply("../maven-push.gradle")