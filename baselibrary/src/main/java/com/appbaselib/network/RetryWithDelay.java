package com.appbaselib.network;

import android.util.Log;

import androidx.annotation.NonNull;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class RetryWithDelay implements Function<Observable<Throwable>, ObservableSource<?>> {

    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.retryCount = 0;
    }


    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable
                .flatMap(new Function<Throwable, ObservableSource<?>>() {

                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        if (retryCount <= maxRetries) {
                            Log.i("RetryWithDelay", "get error, it will try after " + 200 * retryCount
                                    + " millisecond, retry count " + retryCount);
                            // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                            retryCount++;
                            return Observable.timer(retryDelayMillis * retryCount,
                                    TimeUnit.MILLISECONDS);
                        }
                        return Observable.error(throwable);
                    }
                });
    }
}