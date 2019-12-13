package com.appbaselib.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View

import com.alibaba.android.arouter.launcher.ARouter
import com.appbaselib.loading.VaryViewHelperController
import com.appbaselib.netstatus.NetUtils
import kotlinx.android.synthetic.main.title_bar.*
import org.greenrobot.eventbus.EventBus
import com.umeng.analytics.MobclickAgent
import com.umeng.message.PushAgent


/**
 * Created by tangming on 2016/11/15.
 */

abstract class BaseMvcActivity : BaseMvcAppCompatActivity() {

     lateinit var mVaryViewHelperController: VaryViewHelperController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   DisplayUtils.setCustomDensity(this, BaseApplication.mInstance);//适配
        ARouter.getInstance().inject(this)
        PushAgent.getInstance(this).onAppStart();
        if (registerEventBus()) {
            EventBus.getDefault().register(this)    //alter  by  tangming  加入eventbus
        }
        titleBar?.setLeftClickListener {
            finish()
        }
        if (null != getLoadingTargetView()) {
            mVaryViewHelperController = VaryViewHelperController(getLoadingTargetView())
        }
        initView(savedInstanceState)
    }

    public override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    public override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    //请求数据统一的方法,以便统一修改  不强制使用，但如果不使用会导致下面的loaderror方法无作用，且以后不方便修改
    protected open fun requestData() {}

    //统一的加载失败处理方式  不强制使用
    fun loadError() {
        toggleShowError(true, "加载失败,点击重新加载", View.OnClickListener {
            toggleShowLoading(true, "加载中……")
            requestData()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {   //但点击toolbar的返回按钮的时候，
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (registerEventBus()) {
            EventBus.getDefault().unregister(this)   //alter  tm   17 5/26
        }
    }

    protected abstract fun initView(mSavedInstanceState: Bundle?)

    /**
     * 是否注册EventBus，默认不注册
     *
     * @return
     */
    open fun registerEventBus(): Boolean {
        return false
    }


    //===============================================我是分隔符================================================


    override val isListenNetWork: Boolean
        get() = false

    override fun onNetworkConnected(type: NetUtils.NetType) {

    }

    override fun onNetworkDisConnected() {

    }

    /**
     * toggle show loading
     *
     * @param toggle
     */
    protected fun toggleShowLoading(toggle: Boolean, msg: String) {
        if (null == mVaryViewHelperController) {
            throw IllegalArgumentException("You must return a right target view for loading")
        }

        if (toggle) {
            mVaryViewHelperController.showLoading(msg)
        } else {
            mVaryViewHelperController.restore()
        }
    }

    /**
     * @param toggle
     */

    protected fun toggleShowLoading(toggle: Boolean) {
        if (null == mVaryViewHelperController) {
            throw IllegalArgumentException("You must return a right target view for loading")
        }

        if (toggle) {
            mVaryViewHelperController!!.showLoading("加载中……")
        } else {
            mVaryViewHelperController!!.restore()
        }
    }


    /**
     * toggle show empty
     *
     * @param toggle
     */
    protected fun toggleShowEmpty(toggle: Boolean, msg: String, onClickListener: View.OnClickListener) {
        if (null == mVaryViewHelperController) {
            throw IllegalArgumentException("You must return a right target view for loading")
        }

        if (toggle) {
            mVaryViewHelperController!!.showEmpty(msg, onClickListener)
        } else {
            mVaryViewHelperController!!.restore()
        }
    }

    /**
     * toggle show error
     *
     * @param toggle
     */
    protected fun toggleShowError(toggle: Boolean, msg: String, onClickListener: View.OnClickListener) {
        if (null == mVaryViewHelperController) {
            throw IllegalArgumentException("You must return a right target view for loading")
        }

        if (toggle) {
            mVaryViewHelperController!!.showError(msg, onClickListener)
        } else {
            mVaryViewHelperController!!.restore()
        }
    }


    /**
     * toggle show error
     *
     * @param toggle
     */
    protected fun toggleShowError(toggle: Boolean, onClickListener: View.OnClickListener) {
        if (null == mVaryViewHelperController) {
            throw IllegalArgumentException("You must return a right target view for loading")
        }

        if (toggle) {
            mVaryViewHelperController!!.showError("获取失败，请点击重新获取~", onClickListener)
        } else {
            mVaryViewHelperController!!.restore()
        }
    }

    /**
     * toggle show network error
     *
     * @param toggle
     */
    protected fun toggleNetworkError(toggle: Boolean, onClickListener: View.OnClickListener?=null) {
        if (null == mVaryViewHelperController) {
            throw IllegalArgumentException("You must return a right target view for loading")
        }

        if (toggle) {
            mVaryViewHelperController!!.showNetworkError(onClickListener)
        } else {
            mVaryViewHelperController!!.restore()
        }
    }

}
