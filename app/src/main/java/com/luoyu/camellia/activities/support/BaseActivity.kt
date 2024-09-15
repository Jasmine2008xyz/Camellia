package com.luoyu.camellia.activities.support

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager

import androidx.fragment.app.FragmentActivity

import com.luoyu.utils.MergeClassLoader
import com.luoyu.utils.XRes

open class BaseActivity : FragmentActivity() {

    private val mLoader = BaseActivityClassLoader(BaseActivity::class.java.classLoader)

    override fun getClassLoader(): ClassLoader = mLoader

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val windowState = savedInstanceState.getBundle("android:viewHierarchyState")
        windowState?.setClassLoader(mLoader)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XRes.addAssetsPath(this)
    }

    @Suppress("DEPRECATION")
    /*
     * 我系统API刚好30，所以没测试过else下的块
     * 哪天抓个倒霉蛋试试
     */
    protected open fun requestHideNavigationBar() {
    // WindowInsetsController 这个东西只有系统API大于 30 才有
        val insetsController = window.insetsController
        if (insetsController != null && Build.VERSION.SDK_INT >= 30) {
            insetsController.hide(WindowInsets.Type.navigationBars())
            insetsController.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = uiOptions
        }
        
    }

  private class BaseActivityClassLoader(referencer: ClassLoader) : ClassLoader() {
        private val mBaseReferencer: ClassLoader = referencer
        private val mHostReferencer: ClassLoader = ActivityProxyManager.HostClassLoader

        @Throws(ClassNotFoundException::class)
        override fun loadClass(name: String, resolve: Boolean): Class<*> {
            try {
                if (name.startsWith("androidx.compose") ||
                    name.startsWith("androidx.navigation") ||
                    name.startsWith("androidx.activity")) {
                    return mBaseReferencer.loadClass(name)
                }
                return Context::class.java.classLoader.loadClass(name)
            } catch (ignored: ClassNotFoundException) {
            }
            try {
                if (name == "androidx.lifecycle.LifecycleOwner" ||
                    name == "androidx.lifecycle.ViewModelStoreOwner" ||
                    name == "androidx.savedstate.SavedStateRegistryOwner") {
                    return mHostReferencer.loadClass(name)
                }
            } catch (ignored: ClassNotFoundException) {
            }
            return mBaseReferencer.loadClass(name)
        }
    }
}

