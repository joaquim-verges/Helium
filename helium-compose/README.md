# Helium Compose

Handy functions and extensions to integrate Helium Logic Blocks with your Jetpack compose UIs.

Usage:

```kotlin
// Simple logic, can be in Kotlin Multiplatform common code
class MyLogic: LogicBlock<State, Event>() {

    init { pushState(State(count = 0)) }

    override fun onUiEvent(event: Event) {
        when (event) {
            Tap -> incrementAndPushNewState()
        }
    }
}

// Independent UI Block
@Composable
fun MyUi(state: State, dispatcher: EventDispatcher<Event>) {
    Column {
        Text(state.count.toString()).onClick {
            dispatcher.send(Event.Tap)
        }
    }
}

// Assemble them with AppBlock
@Composable
fun App() {
    val logic: MyLogic = logicBlock()
    AppBlock(logic) { state, dispatcher -> MyUi(state, dispatcher) }
}
```

More detailed documentation coming soon!

In the meantime check out some real world examples in the [sample news app UI](/samples/multiplatform_app/android/src/main/java/com/joaquimverges/kmp/news/android/AppUi.kt).
