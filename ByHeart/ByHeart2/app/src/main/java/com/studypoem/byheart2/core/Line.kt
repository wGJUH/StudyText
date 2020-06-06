package com.studypoem.byheart2.core

import android.graphics.Point

data class Line(
    val index : Int,
    val string : String,
    val wordBounds : ArrayList<Point>,
    val wordClosedBounds : ArrayList<Point>
)