package com.studypoem.byheart2.core

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ObjectAdapter(
    private var list: List<*>,
    private val presenter: ItemPresenter?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    constructor(list: List<*>, presenterSelector: ItemPresenterSelector) :
        this(list, null) {
        this@ObjectAdapter.presenterSelector = presenterSelector
    }

    /**
     *  we can do  list.first(),
     *  because when presenter val exists that means, all objects have one type
     */
    private var presenterSelector: ItemPresenterSelector = ItemPresenterSelector()
        .apply {
            presenter?.let { itemPresenter ->
                add(Any::class.java, itemPresenter)
            }
        }

    override fun getItemCount(): Int = list.size

    fun getItems(): List<*> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return presenterSelector.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return presenterSelector.getItemViewType(list[position])
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        presenterSelector.onBindViewHolder(holder, list[position])
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        presenterSelector.onViewRecycled(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        presenterSelector.onViewAttachedToWindow(holder)
    }

    lateinit var recyclerView: RecyclerView
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun setItemsForced(list: List<*>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setItems(list: List<*>) {
        if (this.list.isEmpty()) {
            this.list = list
            notifyItemRangeInserted(0, list.size)
        } else {
            val result = DiffUtil.calculateDiff(DiffU(this.list, list))
            this.list = list
            result.dispatchUpdatesTo(this)
            updateReferences()
        }
    }

    private fun updateReferences() {
        this.list.indices.forEach { index ->
            recyclerView.findViewHolderForAdapterPosition(index)?.let {
                bindViewHolder(
                    it,
                    index
                )
            }
        }
    }

    fun addItems(list: List<*>) {
        if (this.list.isEmpty()) {
            this.list = list
            notifyItemRangeInserted(0, list.size)
        } else {
            val tempList = this.list.toMutableList().apply { addAll(list) }
            val result = DiffUtil.calculateDiff(DiffU(this.list, tempList))
            this.list = tempList
            result.dispatchUpdatesTo(this)
        }
    }

    fun add(index: Int, item: Any) {
        val tempList = this.list.toMutableList().apply { add(index, item) }
        val result = DiffUtil.calculateDiff(DiffU(this.list, tempList))
        this.list = tempList
        result.dispatchUpdatesTo(this)
    }

    fun clear() {
        val size = list.size
        this.list.toMutableList().clear()
        notifyItemRangeRemoved(0, size)
    }

    inner class DiffU(
        private val oldList: List<*>,
        private val newList: List<*>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition]?.equals(newList[newItemPosition]) ?: false
    }
}
