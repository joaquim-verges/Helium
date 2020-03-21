# Helium Core

This tiny module is all you need to start building your own App Blocks.

## Understanding the pattern

<img src="/docs/images/helium_arch_diagram.png" width="600">

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

#### Retained logic

If you want your logic and latest state to be retained across configurations changes, simply replace `MyLogic()` with `getRetainedLogicBlock<MyLogic>()`. This will ensure your latest state is automatically restored after a configuration change.

You can also call your own constructor if you have dynamic data to pass to your logic, like an id from a bundle for example:

```kotlin
val id = intent.extras.getLong(DATA_ID)
val logic = getRetainedLogicBlock<MyLogic>() { MyLogic(id) }
```


#### Implementing a Logic Block

The most common logic for Android apps is to load some data from the network or a database, usually through a repository class.

Here's a example of LogicBlock that fetches some data, pushing the relevant states along the way, and reacts to user events coming from the UI.

```kotlin
class MyLogic(private val repository: MyRepository) : LogicBlock<MyState, MyEvent>() {

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

Note also that there is no UI references in this class, Logic Blocks should only care about pushing state, and handling events.

#### Implementing a UI Block

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

UI Blocks can inflate layouts for you, or you can pass a pre-inflated view hierarchy.

Note that there is no business logic in this class, UI Blocks should only care about rendering state, and pushing events.

#### State and events

In this example, we're using `MyState` and `MyEvent` as the medium of communication between our Logic and our UI. These state and event classes can be anything you want. One option is to use sealed Kotlin classes to define them:

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

Helium Core provides the most common state and event types that you can use in your own blocks:

- [DataLoadState](src/main/java/com/joaquimverges/helium/core/state/DataLoadState.kt): a generic sealed class with all possible loading states when fetching data. Great to use for any logic block whose job is to fetch some data asynchronously.
- [ClickEvent](src/main/java/com/joaquimverges/helium/core/event/ClickEvent.kt): a simple, generic data class to describe a user click event, passing a data model and the view that was clicked.

### More Code Samples

- [newsapp](/samples/newsapp) - Fully functional News app downloadable on [Google Play](https://play.google.com/store/apps/details?id=com.jv.news)
- [demoapp](/samples/demoapp) - A catalog of different AppBlocks usages
