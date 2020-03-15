# Helium Core

This tiny module is all you need to start building your own App Blocks.

## Understanding the pattern

#### LogicBlock

- can push state to a `UiBlock` via `pushState(state)`
- receives `BlockEvent` from any attached `UiBlock` via `onUiEvent(event)`
- receives lifecycle events (implements `LifecycleObserver`)
- can be persisted across orientation changes (extends `ViewModel`)
- no view references here, only state pushing and reacting to view events

#### UiBlock

- Can render Android views according to the `BlockState` passed in `render(state)`
- Can push `BlockEvent` to any attached `LogicBlock` via `pushEvent(event)`
- This is the only place where you hold context or views
- no business logic here, only enough to render the UI

#### Differences from other architecture patterns

- Unlike MVP, a `LogicBlock` does not hold a reference to a `UIBlock`
- Unlike MVVM, a `UIBlock` does not hold a reference to a `LogicBlock`
- `LogicBlock` and `UIBlock` are bound by a simple final class called `AppBlock`

#### Notes on the implementation

 - Uses [RxJava](https://github.com/ReactiveX/RxJava) to handle communication between Logic and UI blocks.
 - Uses [AutoDispose](https://github.com/uber/AutoDispose) to automatically dispose subscriptions, no need to worry about cleaning up or detaching anything.
 - Uses `ViewModel` from the [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/viewmodel.html) to retain logic blocks and their states across configuration changes

## A typical, real world example

##### Entry points

Your Activity or Fragment is always the entry point for your App blocks. There shouldn't be any logic in the entry points themselves, just enough to assemble the blocks together. Helium provides handy extension functions to make this easy and intuitive.

In an Activity:

```kotlin
class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val logic = MyLogic() // create a logic block
        val ui = MyUi(layoutInflater) // create a UI block        
        assemble(logic + ui) // assemble them
        setContentView(ui.view)
    }
}
```

In a Fragment:

```kotlin
class MyFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      val logic = MyLogic() // create a logic block
      val ui = MyUi(inflater)  // create a UI block
      assemble(logic + ui)  // assemble them
      return ui.view
  }
}
```

That is all the wiring code you need. From there, you can write your logic and UI independently, with clear responsibilities for each and nice separation of concerns.

##### Logic

The most common logic for Android apps is to load some data from the network or a database, usually through a repository class.

Here's a example of LogicBlock that fetches some data, pushing the relevant states along the way, and reacts to user events coming from the UI.

```kotlin
class MyLogic(repository: MyRepository) : LogicBlock<MyState, MyEvent>() {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun loadData() {
        repository
            .getData()
            .doOnSubscribe { pushState(MyState.Loading) }
            .async()
            .subscribe(
                { data -> pushState(MyState.DataReady(data)) },
                { error -> pushState(MyState.Error(error)) }
            )
    }

    override fun onUiEvent(event : MyEvent) {
        when (event) {
            is Click -> handleClick()
            is LongPress -> handleLongPress()
        }
    }
}
```

Note that `loadData()` is annotated with a `@OnLifecycleEvent` annotation, which can be used to schedule method calls when a certain lifecycle event happens. This is not required but is very useful in the Android world.

##### UI

Now that your logic is defined with clear states, it's trivial to write a compatible UiBlock that renders the UI for each possible state, and pushes events when certain views get clicked.

```kotlin
class MyUi(inflater: LayoutInflater)
    : UiBlock<MyState, MyEvent>(inflater, R.layout.my_layout) {

    val myButton: TextView = findView(R.id.my_button)

    init {
        myButton.setOnClickListener { view -> pushEvent(MyEvent.Click(view)) }
    }

    override fun render(state: MyState) {
        when(state) {
            is Loading -> showLoading()
            is Error -> showError(state.error)
            is DataReady -> showData(state.data)
        }
    }
}
```

##### State and events

In this example, we're using `MyState` and `MyEvent` as the medium of communication between our Logic and our UI. These state and event classes can be anything you want. One option is to use sealed kotlin classes to define them:

```kotlin
sealed class MyState : BlockState {
    object Loading : MyState()
    data class Error(val error: Throwable) : MyState()
    data class DataReady(val data: MyData) : MyState()
}

sealed class MyEvent : BlockEvent {
    data class Click(val view: View) : MyEvent()
    data class LongPress(val view: View)  : MyEvent()
}
```
