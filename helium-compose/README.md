# Helium Compose

Handy functions and extensions to integrate Helium Logic Blocks with your Jetpack compose UIs.

Usage:

```kotlin
@Composable
fun ArticleList() {
    val logic: MyLogic = viewModel()
    AppBlock(logic) { state, dispatcher ->
        MyUi(state, dispatcher)
    }
}
```
