package com.studypoem.byheart2.ui.home

import android.graphics.Point
import android.text.Spannable
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.RecyclerView
import com.studypoem.byheart2.core.ItemPresenter
import com.studypoem.byheart2.core.Line
import com.studypoem.byheart2.core.logd
import com.studypoem.byheart2.custom.DARK_GREEN
import com.studypoem.byheart2.custom.OnSpannableClickListener
import com.studypoem.byheart2.custom.SpannableStringItem

class SpannableItemPresenter : ItemPresenter() {

    var closedItems: List<Point> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                android.R.layout.simple_list_item_1,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Any?) {
        val data = item as Line
        "onBindViewHolder: index:${data.index}".logd(TAG)
        with(holder.itemView.findViewById<TextView>(android.R.id.text1)) {
            movementMethod = LinkMovementMethod.getInstance()
            text = makeSpannable(data, SpannableListener(data), data.string.toSpannable())
        }
    }

    private class SpannableListener(private val line: Line) : OnSpannableClickListener {
        override fun onSpannableClicked(point: Point) {
            val indexHidden = line.wordClosedBounds.indexOf(point)
            val indexOpened = line.wordBounds.indexOf(point)
            if (indexHidden != -1) {
                "onSpannableClicked: hiddenClicked".logd(TAG)
                line.wordBounds.add(line.wordClosedBounds.removeAt(indexHidden))
            } else if (indexOpened != -1) {
                "onSpannableClicked: openedClicked".logd(TAG)
                line.wordClosedBounds.add(line.wordBounds.removeAt(indexOpened))
            }
        }
    }

    private fun makeSpannable(
        line: Line,
        listener: OnSpannableClickListener,
        originalString: Spannable
    ): Spannable {
        line.wordBounds.forEach { word ->
            originalString.setSpan(
                SpannableStringItem(listener),
                word.component1(),
                word.component2(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        line.wordClosedBounds.forEach { word ->
            toHiddenSpan(originalString, word, listener)
        }
        return originalString
    }

    private fun toHiddenSpan(
        originalString: Spannable,
        point: Point,
        listener: OnSpannableClickListener
    ) {
        originalString.setSpan(
            SpannableStringItem(listener),
            point.component1(),
            point.component2(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        originalString.setSpan(
            BackgroundColorSpan(DARK_GREEN),
            point.component1(),
            point.component2(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        originalString.setSpan(
            ForegroundColorSpan(DARK_GREEN),
            point.component1(),
            point.component2(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
    }

    companion object {
        private val TAG = SpannableItemPresenter::class.java.simpleName
    }
}
