package com.joaquimverges.helium.core.event

/**
 * Base Interface for informing a Presenter of a view event.
 * These are typically user actions like clicks, scrolls, etc. or view specific like an animation ended.
 * These events are emitted by ViewDelegate classes.
 *
 * @see com.joaquimverges.helium.core.presenter.BasePresenter
 * @see com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
 */
interface BlockEvent