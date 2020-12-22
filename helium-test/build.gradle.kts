plugins {
    id("com.android.library")
    kotlin("android")
}

androidLib()

dependencies {
    implementation(project(":helium-core"))
    Deps.androidXTestDeps.forEach{ api(it) }
}

apply(plugin = "com.vanniktech.maven.publish")
