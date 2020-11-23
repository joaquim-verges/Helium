import java.net.URI
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("maven-publish")
    signing
}

group = (project.properties["GROUP"] as String)
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

publishing {
    repositories {
        maven {
            url = URI("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = getProp("NEXUS_USERNAME")
                password = getProp("NEXUS_PASSWORD")
            }
        }
    }
    publications {
        create<MavenPublication>("mavenPub") {
            artifactId = getProp("POM_ARTIFACT_ID")
            version = getProp("VERSION_NAME")
            artifact(tasks.sourcesJar)
        }
    }

    publications.withType(MavenPublication::class.java).all {
        pom {
            groupId = (project.properties["GROUP"] as String)
            artifactId = (project.properties["POM_ARTIFACT_ID"] as String)
            version = (project.properties["VERSION_NAME"] as String)
            name.set(getProp("POM_NAME"))
            packaging = getProp("POM_PACKAGING")
            description.set(getProp("POM_DESCRIPTION"))
            url.set(getProp("POM_URL"))

            scm {
                url.set(getProp("POM_SCM_URL"))
                connection.set(getProp("POM_SCM_CONNECTION"))
                developerConnection.set(getProp("POM_SCM_DEV_CONNECTION"))
            }

            licenses {
                license {
                    name.set(getProp("POM_LICENCE_NAME"))
                    url.set(getProp("POM_LICENCE_URL"))
                    distribution.set(getProp("POM_LICENCE_DIST"))
                }
            }

            developers {
                developer {
                    id.set(getProp("POM_DEVELOPER_ID"))
                    name.set(getProp("POM_DEVELOPER_NAME"))
                }
            }
        }
    }
}

signing {
    sign(publishing.publications)
}

fun getProp(prop: String) = (project.properties[prop] as String)
