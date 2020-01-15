package com.appbaselib.network;

import androidx.annotation.NonNull;

import com.appbaselib.rxlife.RxLife;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * ===============================
 * 描    述：Rx网络统一处理封装类
 * ===============================
 */
public class RxHttpUtil {

    /**
     * 统一线程处理
     */
    public static <T> FlowableTransformer<T, T> schedulerHelper() {    //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一返回结果处理
     */
    public static <T> FlowableTransformer<ResponseBean<T>, T> resultHelper() {   //compose判断结果
        return new FlowableTransformer<ResponseBean<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<ResponseBean<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<ResponseBean<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(@NonNull ResponseBean<T> rawResponse) throws Exception {
                        if (rawResponse.getCode() == 0) {
                            return createData(rawResponse.getData());
                        } else {
                            return Flowable.error(new ApiException(rawResponse.getMessage(), rawResponse.getCode()));
                        }
                    }
                });
            }
        };
    }


    /**
     * 统一返回结果处理 绑定生命周期
     */
    public static <T> ObservableTransformer<ResponseBean<T>, T> handleResult(final LifecycleOwner context) {   //compose判断结果

        return new ObservableTransformer<ResponseBean<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<ResponseBean<T>> upstream) {
                return upstream.flatMap(new Function<ResponseBean<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(ResponseBean<T> tResponseBean) throws Exception {

                        if (tResponseBean.getCode() == 0) {
                            return createData2(tResponseBean.getData());
                        } else {
                            return Observable.error(new ApiException(tResponseBean.getMessage(), tResponseBean.getCode()));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .compose(RxLife.with(context).<T>bindOnDestroy());
            }
        };
    }

    /**
     * 统一返回结果处理 绑定生命周期（返回全部结果 避免rxjava2 发射空数据 走错误方法）
     */
    public static <T> ObservableTransformer<ResponseBean<T>, ResponseBean<T>> handleResult2(final LifecycleOwner context) {   //compose判断结果
        return new ObservableTransformer<ResponseBean<T>, ResponseBean<T>>() {
            @Override
            public ObservableSource<ResponseBean<T>> apply(Observable<ResponseBean<T>> upstream) {
                return upstream.flatMap(new Function<ResponseBean<T>, ObservableSource<ResponseBean<T>>>() {
                    @Override
                    public ObservableSource<ResponseBean<T>> apply(ResponseBean<T> tResponseBean) throws Exception {
                        if (tResponseBean.getCode() == 0) {
                            return createData2(tResponseBean);
                        } else {
                            return Observable.error(new ApiException(tResponseBean.getMessage(), tResponseBean.getCode()));
                        }
                    }
                }).compose(RxLife.with(context).<ResponseBean<T>>bindOnDestroy()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };

    }

    /**
     * 统一返回结果处理 不绑定生命周期（返回全部结果 避免rxjava2 发射空数据 走错误方法）
     */
    public static <T> ObservableTransformer<ResponseBean<T>, ResponseBean<T>> handleResult3(final LifecycleOwner context) {   //compose判断结果
        return new ObservableTransformer<ResponseBean<T>, ResponseBean<T>>() {
            @Override
            public ObservableSource<ResponseBean<T>> apply(Observable<ResponseBean<T>> upstream) {
                return upstream.flatMap(new Function<ResponseBean<T>, ObservableSource<ResponseBean<T>>>() {
                    @Override
                    public ObservableSource<ResponseBean<T>> apply(ResponseBean<T> tResponseBean) throws Exception {
                        if (tResponseBean.getCode() == 0) {
                            return createData2(tResponseBean);
                        } else {
                            return Observable.error(new ApiException(tResponseBean.getMessage(), tResponseBean.getCode()));
                        }
                    }
                });
            }
        };

    }

    /**
     * 生成Flowable
     */
    public static <T> Flowable<T> createData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                    e.printStackTrace();
                }
            }
        }, BackpressureStrategy.BUFFER);
    }


    /**
     * 生成Flowable
     */
    public static <T> Observable<T> createData2(final T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                try {
                    e.onNext(t);
                    e.onComplete();
                } catch (Exception e1) {
                    e.onError(e1);
                    e1.printStackTrace();
                }
            }
        });

    }
//    /**
//     * 生成Flowable
//     */
//    public static <T> Observable<T> createData3(final T t) {
//        return Observable.create(new ObservableOnSubscribe<T>() {
//            @Override
//            public void subscribe(ObservableEmitter<T> e) throws Exception {
//                try {
//                    e.onNext(t);
//                    e.onComplete();
//                } catch (Exception e1) {
//                    e.onError(e1);
//                    e1.printStackTrace();
//                }
//            }
//        });
//
//    }

    public static <T, R> Observable<R> just(T t, Function<T, R> f) {
        return Observable.just(t).map(f).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    public static <T, R> Observable<R> just(Function<T[], R> f, T... ts) {
        return Observable.just(ts).map(f).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
