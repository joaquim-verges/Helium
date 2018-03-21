package com.joaquimverges.helium.event

/**
 * Base Interface for informing a Presenter of a view event.
 * These are typically user actions like clicks, scrolls, etc. or view specific like an animation ended.
 * These events are emitted by ViewDelegate classes.
 *
 * @see com.joaquimverges.helium.presenter.BasePresenter
 * @see com.joaquimverges.helium.viewdelegate.BaseViewDelegate
 */
interface ViewEvent