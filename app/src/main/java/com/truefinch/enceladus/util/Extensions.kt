package com.truefinch.enceladus.util

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

inline fun <reified T : View> Fragment.lazyView(
    @IdRes viewId: Int
): Lazy<T> = lazy { requireActivity().findViewById<T>(viewId) }