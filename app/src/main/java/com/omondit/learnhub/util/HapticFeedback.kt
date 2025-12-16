package com.omondit.learnhub.util

import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

@Composable
fun rememberHapticFeedback(): HapticFeedback {
    val view = LocalView.current
    return remember { HapticFeedback(view) }
}

class HapticFeedback(private val view: View) {

    fun click() {
        view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    }

    fun longPress() {
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }

    fun success() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
    }

    fun error() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.REJECT)
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }
}
