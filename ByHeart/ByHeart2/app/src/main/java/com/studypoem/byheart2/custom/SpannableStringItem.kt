package com.studypoem.byheart2.custom

import android.graphics.Color
import android.graphics.Point
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView

const val DARK_GREEN = -0xffa6b8

class SpannableStringItem(
    private val onSpannableClickListener: OnSpannableClickListener? = null
) : ClickableSpan() {

    override fun onClick(widget: View) {
        val (start, end) = getStartEndPoint(widget as TextView)
        onSpannableClickListener?.onSpannableClicked(Point(start, end))
        val (spannable: Spannable, backgroundColorSpans, foregroundColorSpans) = toSpannable(
            widget,
            start,
            end
        )
        clearOldSpans(spannable, backgroundColorSpans, foregroundColorSpans)
        backgroundColorSpans?.backgroundColor?.let {
            if (it == Color.WHITE) {
                toHidden(widget, spannable, start, end)
            } else {
                widget.text = spannable
            }
        } ?: toHidden(widget, spannable, start, end)
    }

    private fun getStartEndPoint(widget: View): Pair<Int, Int> {
        val s = (widget as TextView).text as Spanned
        val start = s.getSpanStart(this)
        val end = s.getSpanEnd(this)
        return Pair(start, end)
    }

    private fun toHidden(
        view: TextView,
        spannable: Spannable,
        start: Int,
        end: Int
    ) {
        spannable.setSpan(
            BackgroundColorSpan(DARK_GREEN),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(DARK_GREEN),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        view.text = spannable
    }

    private fun clearOldSpans(
        spannable: Spannable,
        backgroundColorSpans: BackgroundColorSpan?,
        foregroundColorSpans: ForegroundColorSpan?
    ) {
        backgroundColorSpans?.let { spannable.removeSpan(it) }
        foregroundColorSpans?.let { spannable.removeSpan(it) }
    }

    private fun toSpannable(
        widget: TextView,
        start: Int,
        end: Int
    ): Triple<Spannable, BackgroundColorSpan?, ForegroundColorSpan?> {
        val spannable: Spannable = SpannableString(widget.text)

        val backgroundColorSpan = spannable.getSpans(
            start, end,
            BackgroundColorSpan::class.java
        ).firstOrNull()
        val foregroundColorSpans = spannable.getSpans(
            start, end,
            ForegroundColorSpan::class.java
        ).firstOrNull()
        return Triple(spannable, backgroundColorSpan, foregroundColorSpans)
    }

    companion object {
        private val TAG = SpannableStringItem::class.java.simpleName
    }
}