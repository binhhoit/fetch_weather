package com.base.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.lang.ref.WeakReference

abstract class RecyclerAdapter<VH : RecyclerAdapter.BaseHolder<*, T>, T>(
    private var onItemClickListener: OnItemClickListener<T>? = null,
    private var comparator: ItemComparator<T>? = null
) : RecyclerView.Adapter<VH>() {

    private var _itemList = ArrayList<T>()

    private var job: Job? = null

    private var _adapterDataObserver: RecyclerView.AdapterDataObserver? = null

    protected val itemList: List<T>
        get() = _itemList

    @Volatile
    var recyclerViewReference: WeakReference<RecyclerView>? = null

    protected val mRecyclerView: RecyclerView?
        get() = recyclerViewReference?.get()

    protected abstract fun getViewHolder(parent: ViewGroup, viewType: Int): VH?

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerViewReference = WeakReference(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val viewHolder = getViewHolder(parent, viewType)
        if (viewHolder != null && onItemClickListener != null) {
            viewHolder.binding.root.setOnClickListener { view ->
                val pos = viewHolder.adapterPosition
                if (pos != NO_POSITION) {
                    onItemClickListener!!.onItemClick(view, pos, itemList[pos])
                }
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindData(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun registerDataObserver(adapterDataObserver: RecyclerView.AdapterDataObserver) {
        if (_adapterDataObserver != null)
            unregisterAdapterDataObserver(_adapterDataObserver!!)
        _adapterDataObserver = adapterDataObserver
        registerAdapterDataObserver(_adapterDataObserver!!)
    }

    private fun unRegisterDataObserver() {
        if (_adapterDataObserver != null)
            unregisterAdapterDataObserver(_adapterDataObserver!!)
        _adapterDataObserver = null
    }

    open fun update(items: List<T>) {
        if (itemList.isNotEmpty() && items.isNotEmpty() && comparator != null) {
            updateDiffItemsOnly(items)
        } else {
            updateAllItems(items)
        }
    }

    private fun updateAllItems(items: List<T>) {
        updateItemsInModel(items)
        notifyDataSetChanged()
    }

    private fun updateDiffItemsOnly(items: List<T>) {
        if (job != null && !job!!.isCancelled)
            job!!.cancel()
        job = flow { emit(calculateDiff(items)) }
            .flowOn(Dispatchers.IO)
            .map {
                updateItemsInModel(items)
                it
            }
            .onEach { result -> updateAdapterWithDiffResult(result) }
            .launchIn(CoroutineScope(Dispatchers.Main))
    }

    private fun calculateDiff(newItems: List<T>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(DiffUtilCallback(itemList, newItems, comparator!!))
    }

    private fun updateItemsInModel(items: List<T>) {
        _itemList.clear()
        _itemList.addAll(items)
    }

    private fun updateAdapterWithDiffResult(result: DiffUtil.DiffResult) {
        result.dispatchUpdatesTo(this)
        recyclerViewReference?.get()?.layoutManager?.let {
            when (it) {
                is LinearLayoutManager -> {
                    if (it.findFirstCompletelyVisibleItemPosition() == 0)
                        recyclerViewReference?.get()?.scrollToPosition(0)
                }
                else -> {
                }
            }
        }
    }

    open fun release() {
        onItemClickListener = null
        unRegisterDataObserver()
    }

    open class BaseHolder<out V : ViewDataBinding, in T>(val binding: V) :
        RecyclerView.ViewHolder(
            binding.root
        ) {

        open fun bindData(data: T) {}
    }

    interface OnItemClickListener<in T> {
        fun onItemClick(v: View, position: Int, data: T)
    }
}