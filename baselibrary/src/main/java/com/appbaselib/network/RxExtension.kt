package net.medlinker.main.network;

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.appbaselib.app.AppManager
import com.appbaselib.base.BaseModel
import com.appbaselib.network.CustomSubscriber
import com.appbaselib.network.RxHttpUtil
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * 简化网络回调  扩展方法
 */
fun <T> Observable<BaseModel<T>>.get(
        context: Context = AppManager.getInstance().currentActivity,  //最好指定 context 免得有时候取不到context
        isShowDialog: Boolean = false,
        message: String? = "请稍候",
        title: String? = "",
        err: ((mS: Throwable?) -> Unit)? = null,//错误回调
        complete: (() -> Unit)? = null, //完成回调
        start: (() -> Unit)? = null//开始回调
        , times: Int = 2,//重试次数
        next: (T?) -> Unit//完成回调

) {
    this.compose(RxHttpUtil.handleResult(context as LifecycleOwner, times))
            .subscribe(object : CustomSubscriber<T>(isShowDialog,
                    if (isShowDialog) ProgressBarHelper.getDefaultProgressBar(title,message,context) else null ) {
                override fun onSucess(t: T?) {
                    next(t)
                }

                override fun onFail(e: Throwable?) {
                    if (err != null) {
                        err(e)
                    } else {
                        super.onFail(e) //走之前的默认实现
                    }
                }

                override fun onComplete() {
                    complete?.let {
                        it()
                    }
                }

                override fun onSubscribe(b: Disposable) {
                    super.onSubscribe(b)
                    //订阅前可能需要做的操作
                    start?.let {
                        start()
                    }
                }
            })
}
