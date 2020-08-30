import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Android

fun Project.androidLib(extraConf: (LibraryExtension.() -> Unit)? = null) {
    extensions.getByType(LibraryExtension::class.java).apply {
        configureAndroid(
                libVersionCode = (project.properties["VERSION_CODE"] as String).toInt(),
                libVersionName = project.properties["VERSION_NAME"] as String
        )
        extraConf?.invoke(this)
    }
    kotlinCompile()
}

fun Project.androidForMultiplatformLib() {
    extensions.getByType(LibraryExtension::class.java).apply {
        configureAndroid(
                libVersionCode = (project.properties["VERSION_CODE"] as String).toInt(),
                libVersionName = project.properties["VERSION_NAME"] as String,
                customConf = {
                    sourceSets {
                        getByName("main") {
                            manifest.srcFile("src/androidMain/AndroidManifest.xml")
                            java.srcDirs("src/androidMain/kotlin")
                            res.srcDirs("src/androidMain/res")
                        }
                        getByName("androidTest") {
                            java.srcDirs("src/androidTest/kotlin")
                            res.srcDirs("src/androidTest/res")
                        }
                    }
                }
        )
    }
}

fun Project.kotlinCompile() {
    tasks.withType(KotlinCompile::class.java) {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

fun LibraryExtension.configureAndroid(
        libVersionCode: Int,
        libVersionName: String,
        customConf: (LibraryExtension.() -> Unit)? = null
) {
    compileSdkVersion(Versions.compileSdk)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = libVersionCode
        versionName = libVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    testOptions.unitTests.isIncludeAndroidResources = true

    customConf?.invoke(this)
}