package com.tapadoo.alerter

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import com.tapadoo.alerter.utils.getDimenPixelSize
import com.tapadoo.alerter.utils.notchHeight
/*import kotlinx.android.synthetic.main.alerter_alert_default_layout.view.*
import kotlinx.android.synthetic.main.alerter_alert_view.view.**/

/**
 * Custom Alert View
 *
 * @author Kevin Murphy, Tapadoo, Dublin, Ireland, Europe, Earth.
 * @since 26/01/2016
 */
@SuppressLint("ViewConstructor")
class Alert @JvmOverloads constructor(context: Context,
                                      attrs: AttributeSet? = null,
                                      defStyle: Int = 0)
    : FrameLayout(context, attrs, defStyle), View.OnClickListener, Animation.AnimationListener, SwipeDismissTouchListener.DismissCallbacks {

    internal var status:(onShowAlert:Boolean) -> Unit = {}

    internal var enterAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_in_from_top)
    internal var exitAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_out_to_top)

    internal var duration = DISPLAY_TIME_IN_SECONDS

    private var enableInfiniteDuration: Boolean = false

    private var runningAnimation: Runnable? = null

    private var isDismissible = true



    /**
     * Flag to ensure we only set the margins once
     */
    private var marginSet: Boolean = false

    /**
     * Flag to enable / disable haptic feedback
     */
    private var vibrationEnabled = true



    /**
     * Sets the Layout Gravity of the Alert
     *
     * @param layoutGravity Layout Gravity of the Alert
     */
    var layoutGravity = Gravity.TOP
        set(value) {

            if (value != Gravity.TOP) {
                enterAnimation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_in_from_bottom)
                exitAnimation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_out_to_bottom)
            }

            field = value
        }



    val layoutComposeView: ComposeView? by lazy { findViewById<ComposeView>(R.id.compose_view) }

    private val currentDisplay: Display? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display
        } else {
            @Suppress("DEPRECATION")
            (context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        }
    }

    private val physicalScreenHeight: Int
        get() =
            DisplayMetrics().also { currentDisplay?.getRealMetrics(it) }.heightPixels

    private val usableScreenHeight: Int
        get() = Resources.getSystem().displayMetrics.heightPixels

    private val cutoutsHeight: Int
        get() = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                currentDisplay?.cutout?.run { safeInsetTop + safeInsetBottom } ?: 0
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ->
                rootWindowInsets?.displayCutout?.run { safeInsetTop + safeInsetBottom } ?: 0
            else -> 0
        }

    private val navigationBarHeight by lazy {
        physicalScreenHeight - usableScreenHeight - cutoutsHeight
    }

    init {
        inflate(context, R.layout.alerter_alert_view, this)

        isHapticFeedbackEnabled = true

        ViewCompat.setTranslationZ(this, Integer.MAX_VALUE.toFloat())


        findViewById<LinearLayout>(R.id.llAlertBackground).setOnClickListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        findViewById<LinearLayout>(R.id.llAlertBackground).apply {

            (layoutParams as LayoutParams).gravity = layoutGravity

            if (layoutGravity != Gravity.TOP) {
                setPadding(
                        paddingLeft, getDimenPixelSize(R.dimen.alerter_padding_default),
                        paddingRight, getDimenPixelSize(R.dimen.alerter_alert_padding)
                )
            }
        }

        (layoutParams as MarginLayoutParams).apply {
            if (layoutGravity != Gravity.TOP) {
                bottomMargin = navigationBarHeight
            }
        }

        enterAnimation.setAnimationListener(this)

        // Set Animation to be Run when View is added to Window
        animation = enterAnimation


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (!marginSet) {
            marginSet = true

            // Add a negative top margin to compensate for overshoot enter animation
            (layoutParams as MarginLayoutParams).topMargin = getDimenPixelSize(R.dimen.alerter_alert_negative_margin_top)

            // Check for Cutout
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                findViewById<LinearLayout>(R.id.llAlertBackground).apply {
                    setPadding(paddingLeft, paddingTop + (notchHeight() / 2), paddingRight, paddingBottom)
                }
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    // Release resources once view is detached.
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        enterAnimation.setAnimationListener(null)
    }

    /* Override Methods */

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.performClick()
        return super.onTouchEvent(event)
    }

    override fun onClick(v: View) {
        if (isDismissible) {
            hide()
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        findViewById<LinearLayout>(R.id.llAlertBackground).setOnClickListener(listener)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        for (i in 0 until childCount) {
            getChildAt(i).visibility = visibility
        }
    }

    /* Interface Method Implementations */

    override fun onAnimationStart(animation: Animation) {
        if (!isInEditMode) {
            visibility = View.VISIBLE

            if (vibrationEnabled) {
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }

        }
    }

    override fun onAnimationEnd(animation: Animation) {
        status(true)

        startHideAnimation()
    }

    private fun startHideAnimation() {
        //Start the Handler to clean up the Alert
        if (!enableInfiniteDuration) {
            runningAnimation = Runnable { hide() }

            postDelayed(runningAnimation, duration)
        }
    }

    override fun onAnimationRepeat(animation: Animation) {
        //Ignore
    }

    /* Clean Up Methods */

    /**
     * Cleans up the currently showing alert view.
     */
    private fun hide() {
        try {
            exitAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    findViewById<LinearLayout>(R.id.llAlertBackground)?.setOnClickListener(null)
                    findViewById<LinearLayout>(R.id.llAlertBackground)?.isClickable = false
                }

                override fun onAnimationEnd(animation: Animation) {
                    removeFromParent()
                }

                override fun onAnimationRepeat(animation: Animation) {
                    //Ignore
                }
            })

            startAnimation(exitAnimation)
        } catch (ex: Exception) {
            Log.e(javaClass.simpleName, Log.getStackTraceString(ex))
        }
    }

    /**
     * Removes Alert View from its Parent Layout
     */
    internal fun removeFromParent() {
        clearAnimation()
        visibility = View.GONE

        postDelayed(object : Runnable {
            override fun run() {
                try {
                    if (parent != null) {
                        try {
                            (parent as ViewGroup).removeView(this@Alert)

                           // onHideListener?.onHide()
                            status(false)
                        } catch (ex: Exception) {
                            Log.e(javaClass.simpleName, "Cannot remove from parent layout")
                        }
                    }
                } catch (ex: Exception) {
                    Log.e(javaClass.simpleName, Log.getStackTraceString(ex))
                }
            }
        }, CLEAN_UP_DELAY_MILLIS.toLong())
    }

    /* Setters and Getters */

    /**
     * Sets the Alert Background colour
     *
     * @param color The qualified colour integer
     */
    fun setAlertBackgroundColor(@ColorInt color: Int) {
        findViewById<LinearLayout>(R.id.llAlertBackground).setBackgroundColor(color)
    }



    /**
     * Disable touches while the Alert is showing
     */
    fun disableOutsideTouch(disable:Boolean = false) {
        findViewById<FrameLayout>(R.id.flClickShield).isClickable = disable
    }


    /**
     * Set if the alerter is isDismissible or not
     *
     * @param dismissible True if alert can be dismissed
     */
    fun setDismissible(dismissible: Boolean) {
        this.isDismissible = dismissible
    }

    /**
     * Get if the alert is isDismissible
     * @return
     */
    fun isDismissible(): Boolean {
        return isDismissible
    }

    /**
     * Set whether to enable swipe to dismiss or not
     */
    fun enableSwipeToDismiss(enable:Boolean = false) {
        if (enable){
            findViewById<LinearLayout>(R.id.llAlertBackground).let {
                it.setOnTouchListener(SwipeDismissTouchListener(it, object : SwipeDismissTouchListener.DismissCallbacks {
                    override fun canDismiss(): Boolean {
                        return true
                    }

                    override fun onDismiss(view: View) {
                        removeFromParent()
                    }

                    override fun onTouch(view: View, touch: Boolean) {
                        // Ignore
                    }
                }))
            }
        }

    }

    /**
     * Set if the duration of the alert is infinite
     *
     * @param enableInfiniteDuration True if the duration of the alert is infinite
     */
    fun setEnableInfiniteDuration(enableInfiniteDuration: Boolean) {
        this.enableInfiniteDuration = enableInfiniteDuration
    }



    /**
     * Enable or Disable haptic feedback
     *
     * @param vibrationEnabled True to enable, false to disable
     */
    fun setVibrationEnabled(vibrationEnabled: Boolean) {
        this.vibrationEnabled = vibrationEnabled
    }



    /**
     *  Set elevation of the alert background.
     *
     *  Only available for version LOLLIPOP and above.
     *
     *  @param elevation Elevation value, in pixel.
     */
    fun setBackgroundElevation(elevation: Float) {
        findViewById<LinearLayout>(R.id.llAlertBackground).elevation = elevation
    }



    override fun canDismiss(): Boolean {
        return isDismissible
    }

    override fun onDismiss(view: View) {
        findViewById<FrameLayout>(R.id.flClickShield)?.removeView(findViewById<LinearLayout>(R.id.llAlertBackground))
    }

    override fun onTouch(view: View, touch: Boolean) {
        if (touch) {
            removeCallbacks(runningAnimation)
        } else {
            startHideAnimation()
        }
    }


    companion object {

        private const val CLEAN_UP_DELAY_MILLIS = 100

        /**
         * The amount of time the alert will be visible on screen in seconds
         */
        private const val DISPLAY_TIME_IN_SECONDS: Long = 3000
        private const val MUL = -0x1000000
    }
}
