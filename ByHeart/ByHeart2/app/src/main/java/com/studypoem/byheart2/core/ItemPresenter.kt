package com.studypoem.byheart2.core

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class ItemPresenter {

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    abstract fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: Any?
    )

    abstract fun onViewRecycled(holder: RecyclerView.ViewHolder)
    open fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {}

    open class ViewHolder(
        val view: View
    ) : RecyclerView.ViewHolder(view)
}
