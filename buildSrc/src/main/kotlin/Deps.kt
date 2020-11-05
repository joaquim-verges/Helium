object Deps {

    // Kotlin
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-common"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutineNative = "org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${Versions.coroutines}"

    // AndroidX
    val lifecycleDeps = listOf(
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.arch_lifecycle_viewmodel}",
            "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.arch_lifecycle_runtime}",
            "androidx.lifecycle:lifecycle-extensions:${Versions.arch_lifecycle}"
    )

    val androidXUiDeps = listOf(
            "androidx.appcompat:appcompat:${Versions.appcompat}",
            "androidx.recyclerview:recyclerview:${Versions.recyclerview}",
            "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefresh}",
            "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}",
            "com.google.android.material:material:${Versions.material_lib}"
    )

    val androidXNavigationDeps = listOf(
            "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}",
            "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    )

    val androidXTestDeps = listOf(
            "androidx.test:core:${Versions.test_core}",
            "androidx.test:runner:${Versions.test_core}",
            "androidx.test:rules:${Versions.test_core}",
            "androidx.test.ext:junit:${Versions.test_ext}",
            "androidx.test.ext:junit-ktx:${Versions.test_ext}",
            "androidx.test.espresso:espresso-core:${Versions.test_espresso}",
            "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockito_kotlin}",
            "org.robolectric:robolectric:${Versions.robolectric}",
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    )

    val composeDeps = listOf(
            "androidx.compose.runtime:runtime:${Versions.compose}",
            "androidx.compose.ui:ui:${Versions.compose}",
            "androidx.compose.foundation:foundation-layout:${Versions.compose}",
            "androidx.compose.material:material:${Versions.compose}",
            "androidx.compose.foundation:foundation:${Versions.compose}",
            "androidx.compose.animation:animation:${Versions.compose}",
            "androidx.ui:ui-tooling:${Versions.compose}"
    )

    // MPP
    val ktorCommonDeps = listOf(
            "io.ktor:ktor-client-core:${Versions.ktor}",
            "io.ktor:ktor-client-serialization:${Versions.ktor}"
    )
    const val ktorAndroid = "io.ktor:ktor-client-android:${Versions.ktor}"
    const val ktorIOS = "io.ktor:ktor-client-ios:${Versions.ktor}"
}

