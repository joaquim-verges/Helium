object Versions {
    const val kotlin = "4.0"
    const val coroutines = "1.3.9"
    const val robolectric = "4.2.1"
    const val mockito_kotlin = "2.1.0"
    const val test_core = "1.2.0"
    const val test_ext = "1.1.1"
    const val test_espresso = "3.2.0"

    const val arch_lifecycle = "2.2.0"
    const val arch_lifecycle_runtime = "2.3.0-alpha05"
    const val arch_lifecycle_viewmodel = "2.3.0-alpha05"
    const val appcompat = "1.1.0"
    const val recyclerview = "1.1.0"
    const val swiperefresh = "0.0"
    const val constraint_layout = "1.1.3"
    const val material_lib = "1.2.0-alpha05"
    const val navigation = "2.2.1"
}

object Deps {
    // Kotlin
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-common"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutineNative= "org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${Versions.coroutines}"

    // AndroidX
    val lifecycleDeps = listOf("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.arch_lifecycle_viewmodel}",
            "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.arch_lifecycle_runtime}",
            "androidx.lifecycle:lifecycle-extensions:${Versions.arch_lifecycle}")
}