package com.jasakarya.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

class AnimationUtil {
    companion object {
        private const val DELAY = 200L
        private const val TIME = 800L

        fun playAnimation(vararg views: View) {
            val animation = mutableListOf<Animator>()
            views.forEach { view ->
                animation.add(ObjectAnimator.ofFloat(view, View.ALPHA, 1f).setDuration(TIME))
            }
            AnimatorSet().apply {
                playSequentially(animation)
                startDelay = DELAY
                start()
            }
        }
    }
}