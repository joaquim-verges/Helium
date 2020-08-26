plugins {
    id("com.android.library")
    kotlin("android")
}

androidLib()

dependencies {
    implementation(project(":helium-core"))
    implementation(project(":helium-ui"))
    Deps.androidXNavigationDeps.forEach(::api)
}

// TODO
//apply from: '../maven-push.gradle'