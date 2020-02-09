package com.joaquimverges.helium.ui.viewdelegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaquimverges.helium.core.event.ViewEvent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Convenience Adapter that renders list items in a BaseRecyclerViewItem.
 *
 * @param viewEvents the observable to push ViewEvent objects to
 * @param viewHolderFactory Provides how to create ViewHolders for this list
 */
class BaseRecyclerAdapter<in T, E : ViewEvent, VH : BaseRecyclerViewItem<T, E>>(
    private val inflater: LayoutInflater,
    private val viewHolderFactory: (LayoutInflater, ViewGroup) -> VH,
    private val viewEvents: PublishSubject<E> = PublishSubject.create()
) : RecyclerView.Adapter<VH>() {

    private val mArticles = mutableListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = viewHolderFactory.invoke(inflater, parent)
        holder.viewEvents = viewEvents
        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    fun observeItemEvents(): Observable<E> {
        return viewEvents.hide()
    }

    private fun getItem(position: Int): T {
        return mArticles[position]
    }

    override fun getItemCount(): Int {
        return mArticles.size
    }

    fun setItems(items: List<T>) {
        mArticles.clear()
        mArticles.addAll(items)
        notifyDataSetChanged()
    }

    fun appendItems(items: List<T>) {
        mArticles.addAll(items)
        notifyDataSetChanged()
    }
}
