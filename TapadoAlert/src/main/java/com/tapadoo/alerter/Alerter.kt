package com.tapadoo.alerter

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import java.lang.ref.WeakReference

/**
 * Alert helper class. Will attach a temporary layout to the current activity's content, on top of
 * all other views. It should appear under the status bar.
 *
 * @author Kevin Murphy
 * @since 03/11/2015.
 */
class Alerter private constructor() {




    /**
     * Sets the Alert
     *
     * @param alert The Alert to be references and maintained
     */
    private var alert: Alert? = null

    /**
     * Shows the Alert, after it's built
     *
     * @return An Alert object check can be altered or hidden
     */
    fun show(): Alert? {
        //This will get the Activity Window's DecorView
        decorView?.get()?.let {
            android.os.Handler(Looper.getMainLooper()).post {
                it.addView(alert)
            }
        }

        return alert
    }




    /**
     * Set Layout Gravity of the Alert
     *
     * @param layoutGravity of Alert
     * @return This Alerter
     */
    fun setLayoutGravity(layoutGravity: Int): Alerter {
        alert?.layoutGravity = layoutGravity

        return this
    }



    /**
     * Set the Alert's Background Colour
     *
     * @param colorInt Colour int value
     * @return This Alerter
     */
    fun setBackgroundColorInt(@ColorInt colorInt: Int): Alerter {
        alert?.setAlertBackgroundColor(colorInt)

        return this
    }


    /**
     * Set the on screen duration of the alert
     *
     * @param milliseconds The duration in milliseconds
     * @return This Alerter
     */
    fun setDuration(milliseconds: Long): Alerter {
        alert?.duration = milliseconds

        return this
    }



    /**
     * Enable or disable infinite duration of the alert
     *
     * @param infiniteDuration True if the duration of the alert is infinite
     * @return This Alerter
     */
    fun enableInfiniteDuration(infiniteDuration: Boolean): Alerter {
        alert?.setEnableInfiniteDuration(infiniteDuration)

        return this
    }



    /**
     * Sets the Alert Hidden Listener
     *
     * @param listener OnHideAlertListener of Alert
     * @return This Alerter
     */
    fun setOnHideListener(listener: OnHideAlertListener): Alerter {
        alert?.onHideListener = listener

        return this
    }

    /**
     * Enables swipe to dismiss
     *
     * @return This Alerter
     */
    fun enableSwipeToDismiss(enabled: Boolean = false): Alerter {
        alert?.enableSwipeToDismiss(enabled)

        return this
    }

    /**
     * Enable or Disable Vibration
     *
     * @param enable True to enable, False to disable
     * @return This Alerter
     */
    fun enableVibration(enable: Boolean): Alerter {
        alert?.setVibrationEnabled(enable)

        return this
    }


    /**
     * Disable touch events outside of the Alert
     *
     * @return This Alerter
     */
    fun disableOutsideTouch(disable:Boolean = false): Alerter {
        alert?.disableOutsideTouch(disable)

        return this
    }





    /**
     * Set if the Alert is dismissible or not
     *
     * @param dismissible true if it can be dismissed
     * @return This Alerter
     */
    fun setDismissable(dismissible: Boolean): Alerter {
        alert?.setDismissible(dismissible)

        return this
    }




    /**
     *  Set elevation of the alert background.
     *
     *  Only available for version LOLLIPOP and above.
     *
     *  @param elevation Elevation value, in pixel.
     */
    fun setElevation(elevation: Float): Alerter {
        alert?.setBackgroundElevation(elevation)

        return this
    }


    fun layoutComposeView(): ComposeView? {
        return alert?.layoutComposeView
    }

    companion object {

        private var decorView: WeakReference<ViewGroup>? = null

        /**
         * Creates the Alert
         *
         * @param activity The calling Activity
         * @return This Alerter
         */
        @JvmStatic
        fun create(activity: Activity): Alerter {
            return create(activity = activity, dialog = null)
        }



        /**
         * Creates the Alert with custom view, and maintains a reference to the calling Activity or Dialog's
         * DecorView
         *
         * @param activity The calling Activity
         * @param dialog The calling Dialog
         * @param layoutId Custom view layout res id
         * @return This Alerter
         */
        @JvmStatic
        private fun create(activity: Activity? = null, dialog: Dialog? = null): Alerter {
            val alerter = Alerter()

            //Hide current Alert, if one is active
            clearCurrent(activity, dialog)

            alerter.alert = dialog?.window?.let {
                decorView = WeakReference(it.decorView as ViewGroup)
                Alert(context = it.decorView.context)
            } ?: run {
                activity?.window?.let {
                    decorView = WeakReference(it.decorView as ViewGroup)
                    Alert(context = it.decorView.context)
                }
            }

            return alerter
        }

        /**
         * Cleans up the currently showing alert view, if one is present. Either pass
         * the calling Activity, or the calling Dialog
         *
         * @param activity The current Activity
         * @param dialog The current Dialog
         * @param listener OnHideAlertListener to known when Alert is dismissed
         */
        @JvmStatic
        @JvmOverloads
        fun clearCurrent(activity: Activity?, dialog: Dialog?, listener: OnHideAlertListener? = null) {
            dialog?.let {
                it.window?.decorView as? ViewGroup
            } ?: kotlin.run {
                activity?.window?.decorView as? ViewGroup
            }?.also {
                removeAlertFromParent(it, listener)
            } ?: listener?.onHide()
        }

        /**
         * Cleans up the currently showing alert view, if one is present. Either pass
         * the calling Activity, or the calling Dialog
         *
         * @param activity The current Activity
         * @param listener OnHideAlertListener to known when Alert is dismissed
         */
        @JvmStatic
        @JvmOverloads
        fun clearCurrent(activity: Activity?, listener: OnHideAlertListener? = null) {
            clearCurrent(activity, null, listener)
        }

        /**
         * Hides the currently showing alert view, if one is present
         * @param listener to known when Alert is dismissed
         */
        @JvmStatic
        @JvmOverloads
        fun hide(listener: OnHideAlertListener? = null) {
            decorView?.get()?.let {
                removeAlertFromParent(it, listener)
            } ?: listener?.onHide()
        }

        private fun removeAlertFromParent(decorView: ViewGroup, listener: OnHideAlertListener?) {
            //Find all Alert Views in Parent layout
            for (i in 0..decorView.childCount) {
                val childView = if (decorView.getChildAt(i) is Alert) decorView.getChildAt(i) as Alert else null
                if (childView != null && childView.windowToken != null) {
                    ViewCompat.animate(childView).alpha(0f).withEndAction(getRemoveViewRunnable(childView, listener))
                }
            }
        }

        /**
         * Check if an Alert is currently showing
         *
         * @return True if an Alert is showing, false otherwise
         */
        @JvmStatic
        val isShowing: Boolean
            get() {
                var isShowing = false

                decorView?.get()?.let {
                    isShowing = it.findViewById<View>(R.id.llAlertBackground) != null
                }

                return isShowing
            }

        private fun getRemoveViewRunnable(childView: Alert?, listener: OnHideAlertListener?): Runnable {
            return Runnable {
                childView?.let {
                    (childView.parent as? ViewGroup)?.removeView(childView)
                }
                listener?.onHide()
            }
        }
    }
}
