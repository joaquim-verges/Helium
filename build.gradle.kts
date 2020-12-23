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
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:9.3.0")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.10.2")
        classpath("com.squareup.sqldelight:gradle-plugin:1.4.3")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.13.0")
    }
}

allprojects {
    repositories {
        jcenter()
        google()
        mavenCentral()
        mavenLocal()
    }
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.jetbrains.dokka")
}
