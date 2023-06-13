package com.example.datingapp.data

import android.app.Instrumentation
import androidx.test.platform.app.InstrumentationRegistry

object Utils {

    fun getActivityMonitor(className: String) =
        Instrumentation.ActivityMonitor(className, null, false)

    fun add(activityMonitor: Instrumentation.ActivityMonitor) {
        InstrumentationRegistry.getInstrumentation().addMonitor(activityMonitor)
    }

    fun remove(activityMonitor: Instrumentation.ActivityMonitor) {
        InstrumentationRegistry.getInstrumentation().removeMonitor(activityMonitor)
    }

}