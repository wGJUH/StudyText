package com.studypoem.byheart2.core

import android.graphics.Point
import kotlin.random.Random

data class Line(
    val index: Int,
    val string: String,
    val wordBounds: ArrayList<Point>,
    val wordClosedBounds: ArrayList<Point>
) {
    fun hideRandom(): Boolean {
        return if (wordBounds.isNotEmpty()) {
            wordClosedBounds.add(wordBounds.removeAt(Random.nextInt(0, wordBounds.size)))
            true
        } else {
            false
        }
    }
}