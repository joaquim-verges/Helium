import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("android")
}

androidLib {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerVersion = Versions.kotlin
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    implementation(project(":helium-core"))
    Deps.composeDeps.forEach(::api)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + listOf("-Xallow-jvm-ir-dependencies", "-Xskip-prerelease-check")
    }
}