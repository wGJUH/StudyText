package com.studypoem.byheart2.core

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ItemPresenterSelector {

    val presenterSelector: LinkedHashMap<String, ItemPresenter> =
        LinkedHashMap()
    private val viewHolderMap = HashMap<RecyclerView.ViewHolder, ItemPresenter>()

    inline fun <reified T : Any> add(presenter: ItemPresenterReifined<T>) {
        presenterSelector[T::class.java.simpleName] = presenter
    }

    fun add(itemClass: Class<*>, presenter: ItemPresenter) {
        presenterSelector[itemClass.simpleName] = presenter
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val presenter = presenterSelector[presenterSelector.keys.toList()[viewType]]
        val holder = presenter?.onCreateViewHolder(parent, viewType)
            ?: throw IllegalArgumentException("presenter not founded for class")
        viewHolderMap[holder] = presenter
        return holder
    }

    fun getItemViewType(any: Any?): Int {
        val clazz = any?.let { it::class.java.simpleName }
        return when {
            presenterSelector.contains(clazz) -> {
                presenterSelector.keys.indexOfFirst { it == clazz }
            }
            presenterSelector.contains(Any::class.java.simpleName) -> 0
            else -> {
                throw IllegalArgumentException("presenter not founded for class: $clazz")
            }
        }
    }

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, any: Any?) {
        viewHolderMap[holder]?.onBindViewHolder(holder, any)
    }

    fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        viewHolderMap[holder]?.onViewRecycled(holder)
    }

    fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        viewHolderMap[holder]?.onViewAttachedToWindow(holder)
    }
}
