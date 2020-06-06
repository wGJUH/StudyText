package com.studypoem.byheart2.core

import android.util.Log
import com.studypoem.byheart2.BuildConfig

fun String.logi(TAG: String) {
    if (BuildConfig.DEBUG) {
        Log.i(TAG, this)
    }
}

fun String.logd(TAG: String) {
    if (BuildConfig.DEBUG) {
        Log.d(TAG, this)
    }
}

fun String.logd(TAG: String, throwable: Throwable) {
    if (BuildConfig.DEBUG) {
        Log.d(TAG, this, throwable)
    }
}

fun String.logw(TAG: String) {
    if (BuildConfig.DEBUG) {
        Log.w(TAG, this)
    }
}

fun String.logw(TAG: String, throwable: Throwable) {
    if (BuildConfig.DEBUG) {
        Log.w(TAG, this, throwable)
    }
}

fun String.loge(TAG: String) {
    if (BuildConfig.DEBUG) {
        Log.e(TAG, this)
    }
}

fun String.loge(TAG: String, throwable: Throwable) {
    if (BuildConfig.DEBUG) {
        Log.e(TAG, this, throwable)
    }
}
