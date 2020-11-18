buildscript {
    repositories {
        jcenter()
        google()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.agp}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:9.3.0")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.0")
        classpath("com.squareup.sqldelight:gradle-plugin:1.4.3")
    }
}

allprojects {
    repositories {
        jcenter()
        google()
        mavenCentral()
    }
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.jetbrains.dokka")
}
