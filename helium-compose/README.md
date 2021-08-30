# Helium Compose

Handy functions and extensions to integrate Helium Logic Blocks with your Jetpack compose UIs.

Usage:

```kotlin
@Composable
fun App() {
    val logic: MyLogic = LocalContext.current.getRetainedLogicBlock()
    AppBlock(logic) { state, dispatcher ->
        MyUi(state, dispatcher)
    }
}
```

More detailed documentation coming soon!

In the meantime check out some real world examples in the [sample news app UI](/samples/multiplatform_app/android/src/main/java/com/joaquimverges/kmp/news/android/AppUi.kt).
