package com.appbaselib.app

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

import com.appbaselib.utils.CommonUtils
import com.appbaselib.utils.SystemUtils
import com.squareup.leakcanary.LeakCanary
import java.util.concurrent.TimeUnit

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

abstract class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppManager.getInstance().setApplication(this)//给App管理器设置上下文
        /**检测当前进程名称是否为应用包名，否则return （像百度地图等sdk需要在单独的进程中执行，会多次执行Application的onCreate()方法，所以为了只初始化一次应用配置，作此判断） */
        if (CommonUtils.getCurProcessName(this) != packageName) {
            return
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    companion object {
        lateinit var instance: BaseApplication
    }

}
