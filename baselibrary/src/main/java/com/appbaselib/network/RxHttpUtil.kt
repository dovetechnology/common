package com.appbaselib.network

import androidx.lifecycle.LifecycleOwner
import com.appbaselib.base.BaseModel
import com.dhh.rxlife2.RxLife.with
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
/**
 * 描    述：Rx网络统一处理封装类
 */
object RxHttpUtil {
    /**
     * 统一返回结果处理 绑定生命周期（返回全部结果 避免rxjava2 发射空数据 走错误方法）
     */
    fun <T> handleResult(context: LifecycleOwner, times: Int = 2): ObservableTransformer<BaseModel<T>, BaseModel<T>> {   //compose判断结果
        return ObservableTransformer { upstream ->
            upstream
                    .retryWhen(RetryWithDelay(times, 200)) //错误重试
                    .flatMap { baseEntity ->
                        if (baseEntity.code == 0) { //success
                            createData(baseEntity)
                        } else {
                            //     ResponseUtil.process(tBaseEntity);
                            Observable.error(ApiException(baseEntity.msg, baseEntity.code))
                        }
                    }.compose(with(context!!).bindOnDestroy())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 统一返回结果处理 不绑定生命周期（返回全部结果 避免rxjava2 发射空数据 走错误方法）
     */
    fun <T> handleResultNoBindLife(times: Int = 2): ObservableTransformer<BaseModel<T>, BaseModel<T>> {   //compose判断结果
        return ObservableTransformer { upstream ->
            upstream
                    .retryWhen(RetryWithDelay(times, 200)) //错误重试
                    .flatMap { tBaseEntity ->
                        if (tBaseEntity.code == 0) { //sucess
                            createData(tBaseEntity)
                        } else {
                            Observable.error(ApiException(tBaseEntity.msg, tBaseEntity.code))
                        }
                    }
        }
    }

    /**
     * Observable
     */
    fun <T> createData(t: T): Observable<T> {
        return Observable.create { e ->
            try {
                e.onNext(t)
                e.onComplete()
            } catch (e1: Exception) {
                e.onError(e1)
                e1.printStackTrace()
            }
        }
    }
}