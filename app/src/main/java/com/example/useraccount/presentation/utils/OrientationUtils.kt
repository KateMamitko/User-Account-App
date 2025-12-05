package com.example.useraccount.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import java.util.Locale

object OrientationUtils {
    fun lockPortrait(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun unlockOrientation(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    fun getString(context: Context, resId: Int, locale: String): String {
        val config = Configuration(context.resources.configuration)
        config.setLocale(Locale(locale))
        return context.createConfigurationContext(config).resources.getString(resId)
    }

}