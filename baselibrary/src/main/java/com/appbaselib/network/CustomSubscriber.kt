package com.appbaselib.network


import android.app.ProgressDialog
import android.content.Context
import android.text.TextUtils
import com.appbaselib.base.BaseModel
import com.appbaselib.ext.toast
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * 通用订阅者,用于统一处理回调
 */
abstract class CustomSubscriber<T>(var isShowProgressBar: Boolean = false, var progressBar: ProgressDialog? = null) : Observer<BaseModel<T>> {

    private var mDisposable: Disposable? = null

    override fun onComplete() {}

    override fun onSubscribe(b: Disposable) {
        mDisposable = b

        if (isShowProgressBar) {
            //点击取消的时候取消订阅  也就是取消网络请求
            progressBar!!.setOnCancelListener { mDisposable!!.dispose() }
            progressBar?.show()
        }
    }

    override fun onNext(t: BaseModel<T>) {

        if (isShowProgressBar) {
            progressBar?.dismiss()
        }
        onSucess(t.data)
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        if (isShowProgressBar) {
            progressBar?.dismiss()
        }

        onFail(e)
    }

    protected abstract fun onSucess(t: T?)

    //默认实现
    protected open fun onFail(e: Throwable?) {
        // TODO: 2021/1/16
        toast(e?.message)
    }

    //默认dialog
    class ProgressBarHelper {
        companion object {
            fun getDefaultProgressBar(title: String?, message: String?, mContext: Context?): ProgressDialog? {
                var mProgressDialog: ProgressDialog? = null
                mContext?.let {
                    mProgressDialog = ProgressDialog(mContext)

                    if (!TextUtils.isEmpty(title))
                        mProgressDialog!!.setTitle(title)
                    if (!TextUtils.isEmpty(message))
                        mProgressDialog!!.setMessage(message)

                    mProgressDialog!!.setCancelable(true)
                    mProgressDialog!!.setCanceledOnTouchOutside(true)
                }
                return mProgressDialog

            }
        }
    }

}

