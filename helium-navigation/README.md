# Helium Navigation

Collection of AppBlocks powering Android navigation:

- Toolbar
- Collapsing Toolbar
- Bottom Navigation
- Navigation Drawer

*Detailed documentation and examples coming soon...*

## Toolbars

Most Android apps use a `Toolbar` in some way. Helium provides a few useful set of classes to integrate it easily into your apps, especially the more convoluted collapsing toolbars.

<img src="/docs/images/collapsing_toolbar.gif" width=300>

#### ToolbarUi

- Simple `Toolbar` with default layout
- Pass a menu resource id to inflate the menu items
- Pushes `ToolbarEvent` events
- Sets itself as the Activity `ActionBar` if needed
- Allows customization of the layout
- Allows customization of the `ActionBar` / `Toolbar`
- Integrates with a default `ToolbarLogic` or your own

#### CollapsingToolbarUi

- Only requires passing a `UIBlock` for the content of the screen (typically a scrolling list)
- Sets up a default scroll behavior (toolbar scrolls off while scrolling)
- Allows to pass a custom scroll behavior
- Allows to pass a backdrop UI block to show behind the toolbar in expanded mode
- Allows customization of `AppBarLayout` / `CollapsingToolbarLayout`
- Contains a `ToolbarUi` with all its configuration options
- See [CardListActivity](s/demoapp/src/main/java/com/joaquimverges/demoapp/CardListActivity.kt) for a simple example, or [ArticleListUi](/samples/newsapp/src/main/java/com/jv/news/ui/ArticleListUi.kt) for a more complex one (as seen in the gif above)

Simple usage:

```kotlin
CollapsingToolbarUi<BlockState, BlockEvent>(layoutInflater, myListUi)
```

## Bottom navigation

One of the most common navigation patterns is using a bottom bar. Helium facilitates creating screens with this patterns by providing a UI block that does most of the setup for you, using Material components and the Jetpack Navigation Library.

<img src="/docs/images/bottom_nav.gif" width=300>

#### BottomNavUi

- Only requires passing a Menu resource id for the bottom navigation icon/labels, and a Graph resource id to map those with corresponding Fragments.
- Sets up a `NavHostFragment` and a `NavController` automatically, and binds your menu items with your navigation graph.
- Pushes `BottomNavEvent` events to perform extra actions on navigation changes
- Integrates with a default `BottomNavLogic` or your own
- Allows customization of the `BottomNavigationView`
- See [BottomNavActivity](/samples/demoapp/src/main/java/com/joaquimverges/demoapp/BottomNavActivity.kt) for sample code

Simple usage:

```kotlin
class BottomNavActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ui = BottomNavUi(layoutInflater, R.menu.my_menu, R.navigation.my_graph)
        setContentView(ui.view)
    }
}
```

TIP: You can maintain state between navigation changes. To do so, use `requireActivity().getRetainedLogicBlock()` in your fragment. That will tell the framework to retain a logic block as long as the Activity is active, rather than just an individual fragment.


## Navigation Drawer

Slightly less popular than bottom navigation these days, but still very useful for many apps, A navigation drawer UI block is also provided by this Helium package.

<img src="/docs/images/nav_drawer.gif" width=300>

#### NavDrawerUI

- Requires passing a UI block for the drawer content, and one for the main content.
- Sets up a default layout, with default drawer gravity (Start)
- Allows customization of the `DrawerLayout`
- Integrates with a `NavDrawerLogic`
- Renders `NavDrawerState`
- Pushes `NavDrawerEvent`
- See [MainScreenUi](/master/samples/newsapp/src/main/java/com/jv/news/ui/MainScreenUi.kt) for sample code

Simple usage:

```kotlin
NavDrawerUi(mainUi, drawerUi)
```
