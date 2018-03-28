# Helium

Lightweight MVP framework for Android. 100% Kotlin.

## Benefits

Helium follows the MVP pattern described below and helps you keep code clean and organized.

It also provides some implementations that help you build common Android components like lists and viewpagers, saving you time and code.

Here's a working app that displays a scrolling list of 100 words in 20 lines of code:

```kotlin
class SimpleListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ListViewDelegate(layoutInflater,
                { inflater, container -> MyListItem(R.layout.list_item_layout, inflater, container) })
                .apply { setContentView(view) }
                .also { ListPresenter<String, ViewEvent>(MyRepository()).attach(it) }
    }

    class MyRepository : BaseRepository<List<String>> {
        override fun getData() = Observable.range(0, 100).map { i -> "Word number $i" }.toList()
    }

    class MyListItem(@LayoutRes layoutResId: Int, inflater: LayoutInflater, parent: ViewGroup)
        : BaseRecyclerViewItem<String, ViewEvent>(layoutResId, inflater, parent) {
        private val textView: TextView = view.findViewById(R.id.text_view)
        override fun bind(data: String) {
            textView.text = data
        }
    }
}
```

The only custom logic needed is the data to display and the list item view that binds it, the rest is handled for you.

These implementations come with a variety of customization options to use them direcly, or include them as subcomponents of your own components.

## Apps

Explore simple usages of the library in the [Demo App](/samples/demoapp).

For a more full fledged App using Helium, check out [Helium News](/samples/newsapp). Also available on [Google Play](https://play.google.com/store/apps/details?id=com.jv.news).


## Philosphy

This Framework aims to help you build Android apps fast and cleanly. 

Think of it as a collection of base classes and implementations that you can use as building blocks for your Android app.

The framework helps you organize your code in 3 main categories:

- **ViewDelegate** - This is what holds and renders your Android Views
- **Presenter** - This is where you decide what data to get, when to show it, and how to react to user events
- **Repository** - This is the class that knows how to get the actual data (network, disk, cache, etc)

It's a very classic MVP pattern that works for any component in your app. 

![data flow diagram](/docs/images/data_flow_diagram.png)

## Implementation

The following bases classes are the building blocks for any components in your app:

### BaseRepository

- Simple interface that returns some data
- Here is a good place to put your network calls, database queries/writes, preferences edits, etc...
- Responsible for producing the model objects that the presenter will use

### BasePresenter

- can push state to a `ViewDelegate` via `pushState(state)`
- receives `ViewEvent` from any attached `ViewDelegate` via `onViewEvent(event)`
- receives lifecycle events (implements `LifecycleObserver`)
- can be persisted accross orientation changes (implements `ViewModel`)
- no view references here, only state pushing and reacting to view events

### BaseViewDelegate

- Can render Android views according to the `ViewState` passed in `render(state)`
- Can push `ViewEvent` to any attached presenter via `pushEvent(event)`
- This is the only place where you hold context or views
- no business logic here, only enough to render views

### Notes on the implementaion

 - Uses [RxJava](https://github.com/ReactiveX/RxJava) to handle communication between Presenters and ViewDelegates
 - Uses [RxLifecycle](https://github.com/trello/RxLifecycle) to automatically dispose subscriptions, no need to worry about cleaning up or detaching anything
 - Uses `ViewModel` from the [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/viewmodel.html) to retain presenters across configuration changes
 - Each of the three classes are independent from each other can be re-used for other components

## Usage

This is all you need to create a component and display it in an Activity:

```kotlin
class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val presenter = MyPresenter()
        val viewDelegate = MyViewDelegate(layoutInflater)
        presenter.attach(viewDelegate)
        setContentView(viewDelegate.view)
    }
}
```

You can also make your presenter retained upon configuration changes by accessing it via the `RetainedPresenters.get()` method:

This is an example of a retained presenter in a fragment:

```kotlin
class MyFragment : Fragment() {

    private lateinit var presenter: MyDetailPresenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        presenter = RetainedPresenters.get(this, MyPresenter::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return MyViewDelegate(inflater, container).also { presenter.attach(it) }.view
    }
}
```

A typical Presenter implementation looks like this:

```kotlin
class MyPresenter(repository: MyRepository()) : BasePresenter<MyState, MyEvent> {

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

    override fun onViewEvent(event : MyEvent) {
        when(event) {
            is Click -> ...
            is LongPress -> ...
        }
    }
}
```

note that `loadData()` is annotated with a `@OnLifecycleEvent` annotation, which can be used to schedule method calls when a certain lifecycle event happens. This is not required but is very useful in the Android world.

A typical ViewDelegate implementation looks like this:

```kotlin
class MyViewDelegate(inflater: LayoutInflater) 
    : BaseViewDelegate<MyState, MyEvent>(inflater, R.layout.my_layout) {

    val myButton : TextView = view.findViewById(R.id.my_button)

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

In this example, we're using `MyState` and `MyEvent` as the medium of communication between our Presenter and our ViewDelegate. These state and event classes can be anything you want. One option is to use sealed kotlin classes to define them:

```kotlin
sealed class MyState : ViewState {
    object Loading : MyState()
    data class Error(val error: Throwable) : MyState()
    data class DataReady(val data: MyData) : MyState()
}

sealed class MyEvent : ViewEvent {
    data class Click(val view: View) : MyEvent()
    data class LongPress(val view: View)  : MyEvent()
}
```

## Ready-to-use components

On top of providing the base classes to build any component you can think of, Helium has a set of handy presenters and view delegates ready to be used as building blocks for your own components.

Most Android apps have common patterns (loading data from network, displaying lists, viewpagers, etc), Helium can help you build those components with minimal amount of code.

Loading data from network, and display it in a `RecyclerView` is is by far the most common thing we Android developers have to implement, and Helium helps you cut down on a lot of boilerplate code, while keeping the structure clean:

### ListPresenter

The role of this class is to simply load some data asynchronously and push its current state.

Configuration:

- `BaseRepository` that provides the data
- `RefreshPolicy` param to configure how often this data should be refreshed

States :

- `NetworkViewState` class to describe the network request status (loading, error, empty, data ready, etc.)
- the data ready state comes with the list of loaded data

This presenter can be used with any `ViewDelegate` that can render a `NetworkViewState`. Helium provides a ready-made list view delegate that can be used with this presenter:

### ListViewDelegate

This viewdelegate holds a recycler view, a loading spinner and a empty view container. It knows how to render any `NetworkViewState`.

Configuration:

- `BaseRecyclerViewItem` for the list items layout (extends `RecyclerView.ViewHolder`)

There's also a handful of configuration options that you can use to customize your layout, list, empty view, etc.

Events:

- Relies on the passed `BaseRecyclerViewItem` implementation to relay any kind of view events up to the presenter layer (typically clicks, long press, etc)

This list view delegate is all you'll need to display a list of models of any kind. All you need to focus on is the actual list item layout, the rest is handled for you.

### BaseRecyclerItem

This is a specialized `ViewDelegate` that is specific to recycler views. It holds the list item layout that can be bound to some data and recycled, and also provides the same mechanism to push events up to the presenter layer.

This class is particularly useful when used with a `ListViewDelegate`, as Helium will handle all the adapter code and the wiring for you.

Configuration:

- Only needs the view or the layout resource id

Events:

- Can push any view event, typically clicks, long presses, etc.

### PagerViewDelegate

Another widely used UI pattern is the `ViewPager`, so Helium provides a `ViewDelegate` that you can use out of the box.

Configuration:

- `FragmentPageProvider`, that defines which Fragment goes in which page.

There's also a handful of configuration options to customize your layout, view pager configuration, etc.


