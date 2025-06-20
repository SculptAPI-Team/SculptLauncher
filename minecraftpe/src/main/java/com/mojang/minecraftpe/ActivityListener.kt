package com.mojang.minecraftpe

import android.content.Intent

interface ActivityListener {
    fun onActivityResult(i: Int, i2: Int, intent: Intent?)

    fun onDestroy()

    fun onResume()

    fun onStop()
}
