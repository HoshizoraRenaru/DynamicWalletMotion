package org.hoshizora.dynamicwalletmotion

import android.content.Context
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible

object SuicaOverlayService {
    private var overlayView: View? = null
    private var windowManager: WindowManager? = null

    fun show(context: Context) {
        if (overlayView != null) return  // Prevent duplicate views

        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val inflater = LayoutInflater.from(context)
        overlayView = inflater.inflate(R.layout.suica_card, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        params.y = 80  // vertical offset from top

        windowManager?.addView(overlayView, params)

        animateCardIn(context)
    }

    private fun animateCardIn(context: Context) {
        val cardView = overlayView ?: return
        val textView = cardView.findViewById<TextView>(R.id.paymentText)

        // Start from near front camera (top center)
        cardView.scaleX = 0.5f
        cardView.scaleY = 0.5f
        cardView.alpha = 0f
        cardView.translationY = -40f  // simulate "inside" island

        cardView.animate()
            .translationY(80f) // pop down
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(400)
            .setInterpolator(android.view.animation.OvershootInterpolator())
            .start()

        // Show text after a short delay
        Handler(Looper.getMainLooper()).postDelayed({
            textView.text = "✓ 決済完了"
        }, 1000)

        // Exit animation
        Handler(Looper.getMainLooper()).postDelayed({
            cardView.animate()
                .alpha(0f)
                .translationY(160f)
                .setDuration(300)
                .withEndAction {
                    dismiss()
                }
                .start()
        }, 2500)
    }

    fun dismiss() {
        if (overlayView != null && windowManager != null) {
            windowManager?.removeView(overlayView)
            overlayView = null
        }
    }
}
