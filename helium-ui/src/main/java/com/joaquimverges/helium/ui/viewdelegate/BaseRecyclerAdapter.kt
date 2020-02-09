package com.joaquimverges.helium.ui.viewdelegate

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
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

    private val diff = AsyncListDiffer<T>(this, object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    })

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
        return diff.currentList[position]
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    fun setItems(items: List<T>) {
        diff.submitList(items)
    }
}
