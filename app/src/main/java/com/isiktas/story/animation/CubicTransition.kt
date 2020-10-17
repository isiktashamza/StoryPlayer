package com.isiktas.story.animation

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class CubicTransition : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 -> {
                page.alpha = 0f
            }
            position <= 0 -> {
                page.alpha = 1f
                page.pivotX = page.width.toFloat()
                page.pivotY = page.height.toFloat()/2
                page.rotationY = -45* abs(position)
            }
            position <= 1 -> {
                page.alpha = 1f
                page.pivotX = 0f
                page.pivotY = page.height.toFloat()/2
                page.rotationY = 45* abs(position)
            }
            else -> {
                page.alpha = 0f
            }
        }

    }

}