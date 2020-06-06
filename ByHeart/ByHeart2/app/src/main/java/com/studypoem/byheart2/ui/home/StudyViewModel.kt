package com.studypoem.byheart2.ui.home

import android.content.Context.WINDOW_SERVICE
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Typeface
import android.provider.UserDictionary
import android.text.Spannable
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.text.toSpannable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.studypoem.byheart2.core.App
import com.studypoem.byheart2.core.Line
import com.studypoem.byheart2.core.logd
import com.studypoem.byheart2.core.loge
import com.studypoem.byheart2.custom.DARK_GREEN
import com.studypoem.byheart2.custom.SpannableStringItem
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import kotlin.math.log

const val PATTERN_STRING = "\\n\\r|\\r\\n|\\r|\\n|\\Z|\\n\\n"
const val PATTERN_WORD = "[A-zA-Z0-9а-яА-ЯёЁ]+"

const val HIDDEN_PERCENT = 20

sealed class Result {
    object Loading : Result()
    data class Success(val result: Any) : Result()
    data class Error(val thr: Throwable) : Result()
}

class StudyViewModel : ViewModel() {
    val handler = CoroutineExceptionHandler { _, exception ->
        "handler catch an error: ".loge(TAG, exception)
    }
    private val _text: MutableLiveData<String> = MutableLiveData(getTestText())
    private val update : MutableLiveData<Boolean> = MutableLiveData()

    private var wordsCount: Int = 0
    private var lines: List<Line> = listOf()

    val updateObserver : LiveData<Boolean> = update
    val text: LiveData<String> = _text
    val preparedText: LiveData<Result> = _text.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO + handler) {
            emit(Result.Loading)
            val start = System.currentTimeMillis()
            emit(map())
            "fixStrings for: ${System.currentTimeMillis() - start}".logd(TAG)
        }
    }

    private suspend fun map(): Result {

        return Result.Success(
            StringBuilder().apply {
                for (i in 0..100) {
                    append(getTestText())
                }
            }.toString()
                .toLines()
        )
    }

    private suspend fun String.toLines(): List<Line> {
        val originalLine = this
        viewModelScope.launch {
            lines = originalLine.lines()
                .mapIndexed { ind, line ->
                    Line(
                        ind,
                        line,
                        splitToWords(line),
                        arrayListOf()
                    )
                }.also {
                    wordsCount = it.sumBy { it.wordBounds.size }
                }
        }.join()
        "toLines: $wordsCount".logd(TAG)
        return lines
    }

    private fun splitToStrings(
        words: ArrayList<Point>,
        string: String,
        lines: ArrayList<Point>
    ) {
        val wm = App.context.getSystemService(WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        var startPosition = 0
        var endPosition: Int = startPosition
        val textPaint = Paint()
        textPaint.textSize = 20f.sp2Px
        textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)

        words.chunked(words.size / 10).map { chunk ->
            chunk.forEachIndexed { ind, point ->
                val lineWidth =
                    textPaint.measureText(
                        string.substring(
                            startPosition,
                            point.component2()
                        )
                    )
                if (ind < words.lastIndex) {
                    if (lineWidth <= width) {
                        endPosition = point.component2()
                    } else {
                        lines.add(Point(startPosition, endPosition))
                        startPosition = point.component1()
                        endPosition = point.component2()
                    }
                } else {
                    lines.add(Point(startPosition, point.component2()))
                }
            }
        }
    }

    private fun makeSpannable(
        wordsBounds: ArrayList<Point>,
        originalString: Spannable
    ): Spannable {
        wordsBounds.forEach { word ->
            originalString.setSpan(
                SpannableStringItem(),
                word.component1(),
                word.component2(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            originalString.setSpan(
                BackgroundColorSpan(DARK_GREEN),
                word.component1(),
                word.component2(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            originalString.setSpan(
                ForegroundColorSpan(DARK_GREEN),
                word.component1(),
                word.component2(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return originalString
    }

    private fun splitToWords(value: String): ArrayList<Point> {
        val matcher = PATTERN_WORD.toPattern().matcher(value)
        val words = arrayListOf<Point>()
        while (matcher.find()) {
            words.add(Point(matcher.start(), matcher.end()))
        }
        return words
    }

    inline val Float.sp2Px
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            Resources.getSystem().displayMetrics
        )

    private fun getTestText() =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus nec ornare augue. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nunc imperdiet sodales lectus, vel molestie libero egestas vel. Quisque non molestie risus, tristique condimentum lorem. Nulla vitae neque risus. Aenean in massa vitae libero rhoncus sollicitudin. Mauris facilisis sem justo, a interdum lacus egestas eu. Aliquam erat volutpat. Cras turpis est, iaculis quis auctor in, lobortis nec ligula. Integer non mattis lorem. Quisque in enim erat. Nulla faucibus non elit ac volutpat. Suspendisse erat libero, condimentum et pharetra eget, sagittis at urna. Sed aliquam purus sem. Maecenas fermentum neque orci, a suscipit leo auctor ut. Aenean ut leo non lorem vulputate scelerisque sed nec justo.\n" +
            "\n" +
            "Vestibulum lobortis sem quis ex luctus, at elementum orci aliquam. Cras semper ut arcu sit amet suscipit. Aenean non venenatis enim. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed nulla nunc, interdum eu pharetra non, suscipit vel tortor. Praesent sed gravida eros. Donec faucibus lectus at laoreet posuere. Pellentesque bibendum sed felis vulputate molestie. Duis a sollicitudin nisl, ut finibus purus.\n" +
            "\n" +
            "In varius lacinia laoreet. Sed diam sem, vestibulum a orci nec, porttitor pretium leo. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer et ornare orci. Ut eget elit lectus. Cras sit amet nibh nunc. Curabitur aliquam sagittis ante, a posuere enim tempus ac. Curabitur hendrerit elit sit amet felis aliquet commodo.\n" +
            "\n" +
            "Cras quis dui et nisl laoreet tincidunt et nec purus. Aenean ut erat ipsum. In euismod pulvinar libero in euismod. Donec sagittis imperdiet eros, a ornare elit ultrices ut. Aenean eget quam diam. Duis id maximus ex, id dignissim justo. Curabitur tincidunt nisl eget diam ornare auctor et non orci. Maecenas iaculis arcu in congue finibus. Aliquam erat volutpat. Donec scelerisque venenatis elit, at vestibulum purus euismod eget. Nunc hendrerit elit sed dolor pellentesque, ut elementum sem lobortis. Sed odio sem, posuere vel iaculis ut, accumsan vel nulla.\n" +
            "\n" +
            "Praesent non nisi sit amet quam cursus cursus quis eu lorem. Duis sagittis posuere orci, id ornare sem ullamcorper nec. Mauris orci turpis, sagittis in est quis, porta egestas urna. Curabitur et commodo enim, nec semper dolor. Maecenas placerat tortor sed felis volutpat scelerisque. Quisque risus odio, euismod mattis tellus sed, posuere semper ligula. Maecenas varius tellus orci, nec tempor ex elementum ut."

    fun onHideClicked() {

        var wordsToClose = wordsCount * HIDDEN_PERCENT / 100
        val wordNotClosed = lines.sumBy { it.wordBounds.size }
        wordsToClose = wordsToClose.coerceAtMost(wordNotClosed)
        "onHideClicked: $wordsToClose $wordNotClosed".logd(TAG)
        var i = 0
        while (wordsToClose > 0){
            if(lines[i++ % lines.size].hideRandom()){
                wordsToClose -= 1
            }
        }
        update.postValue(true)

    }

    companion object {
        private val TAG = StudyViewModel::class.java.simpleName
    }
}