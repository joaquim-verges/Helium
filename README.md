# Helium

Lightweight MVP framework for Android. 100% Kotlin.

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

Helium follows this pattern, and provides some useful base classes will save you lots of develoment time, while keeping your code clean and organized.

### BaseRepository

Helium follows this pattern, and provides some useful base classes will save you lots of develoment time, while keeping your code clean and organized.

- Simple interface that returns some data
- Here is a good place to put your network calls, database queries/writes, preferences edits, etc...
- Responsible for producing the object models that the presenter will use

### BasePresenter

- can push state to a ViewDelegate via `pushState(state)`
- receives view events from any attached ViewDelegate via `onVieEvent(event)`
- receives lifecycle events (implements `LifecycleObserver`)
- can be persisted accross orientation changes (implements `ViewModel`)
- no view references here, only state pushing and reacting to view events

### BaseViewDelegate

- Can render Android views according to the ViewState passed in `render(viewState)`
- Can push ViewEvents to any attached presenter via `pushEvent()`
- This is the only place where you hold context or views
- no business logic here, only enough to render views

### Notes on the implementaion

 - Presenters and ViewDelegates communicate via RxJava subjects, they are not coupled together
 - Lifecycle is handled for you, no need to worry about cleaning up or detaching anything
 - Each of the three classes  are independent can be re-used for other components

## Usage

```kotlin
val presenter = MyPresenter()
val viewDelegate = MyViewDelegate(layoutInflater)
presenter.attach(viewDelegate)
```

This is all you need to create a component in an Activity or fragment.

You can also make your presenter retained upon configuration changes by accessing it via the RetainedPresenters.get method:

```kotlin
val presenter = RetainedPresenters.get(this, MyPresenter::class.java)
```

where `this` is an Activity or Fragment.

A typical Presenter looks like this:

```kotlin
class MyPresenter(repository: MyRepository()) : BasePresenter<MyState, MyEvent> {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun loadData() {
        repository
            .getData()
            .doOnSubscribe { pushState(MyState.Loading) }
            .async()
            .subscribe(
                { data -> pushState(MyState.DataReady(data)) },
                { error -> pushState(MyState.Error(error)) }
            )
    }

    override onViewEvent(event : MyEvent) {
        when(event) {
            is Click -> ...
            is LongPress -> ...
        }
    }
}
```

note that `loadData()` is annotated with a `OnLifecycleEvent` annotation, which can be used to schedule method calls when a certain lifecycle event happens.

A typical ViewDelegate looks like this:

```kotlin
class MyViewDelegate(inflater: LayoutInflater) 
    : BaseViewDelegate<MyState, MyEvent>(inflater, R.layout.my_layout) {

    val myButton : TextView = view.findViewById(R.id.my_button)

    init{
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

In this example, we're using `MyState` and `MyEvent` as the medium of communication between our Presenter and our ViewDelegate. These state and event classes can be anything you want. I like to use sealed kotlin classes to define them :

```kotlin
sealed class MyState : ViewState {
    object Loading : MyState()
    data class Error(val error: Throwable) : MyState()
    data class DataReady(val data: MyData) : MyState()
}

sealed class MyEvent : ViewEvent {
    data class Click(view: View) : MyEvent()
    data class LongPress(view: View)  : MyEvent()
}
```

## Ready-to-use components

On top of providing the base classes to build any component you can think of, Helium has a set of handy presenters and viewdelegates ready to be used as building blocks for your own components.

Most Android apps have common patterns (loading data from network, displaying lists, viewpagers, etc), Helium can help you build those components with minimal amount of code.

Loading data from network, and display it in a `RecyclerView` is is by far the most common thing we Android developers have to implement, and Helium helps you cut down on a lot of boilerplate code, while keeping the structure clean:

### DataListPresenter

The role of this class is to simply load some data asynchronously and push its current state.

Configuration:

- `BaseRepository` that provides the data
- `RefreshPolicy` param to configure how often this data should be refreshed

States :

- `NetworkViewState` class to describe the network request status (loading, error, empty, data ready, etc.)
- the data ready state comes with the list of loaded data

This presenter can be used with any `ViewDelegate` that can render a `NetworkViewState`. Helium provides a ready-made list view delegate that can be used with this presenter:

### DataListViewDelegate

This viewdelegate holds a recycler view, a loading spinner and a empty view container. It knows how to render any `NetworkViewState`.

Configuration:

- `BaseRecyclerViewItem` for the list items layout (extends `RecyclerView.ViewHolder`)

There's also a handful of configuration options that you can use to customize your layout, list, empty view, etc.

Events:

- Relies on the passed `BaseRecyclerViewItem` implementation to relay any kind of view events up to the presenter layer (typically clicks, long press, etc)

This list view delegate is all you'll need to display a list of models of any kind. All you need to focus on is the actual list item layout, the rest is handled for you.

### BaseRecyclerItem

This is a specialized `ViewDelegate` that is specific to recycler views. It holds the list item layout that can be bound to some data and recycled, and also provides the same mechanism to push events up to the presenter layer.

This class is particularly useful when used with a `DataListViewDelegate`, as Helium will handle all the adapter code for you.

Configuration:

- Only needs the view or the layout resource id

Events:

- Can push any view event, typically clicks, long presses, etc.

## PagerViewDelegate

Another widely use UI pattern is the ViewPager, so Helium provides a `ViewDelegate` that you can use out of the box.

Configuration:

- `FragmentPageProvider`, that defines which Fragment goes in which page.

There's also a handful of configuration options to customize your layout, view pager configuration, etc.

## Samples

You will find all the classes described above put into practice with simple examples in [samples/demoapp](/samples/demoapp).

For a more full fledged app using Helium, check out [samples/newsapp](/samples/newsapp). Also available on [Google Play](https://play.google.com/store/apps/details?id=com.jv.news).
