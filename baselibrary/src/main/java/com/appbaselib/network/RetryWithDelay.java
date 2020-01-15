package com.appbaselib.network;

import android.util.Log;

import androidx.annotation.NonNull;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class RetryWithDelay implements Function<Flowable<? extends Throwable>, Publisher<?>> {

    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.retryCount = 0;
    }

    @Override
    public Publisher<?> apply(@NonNull Flowable<? extends Throwable> attempts) throws Exception {

        return attempts.flatMap(new Function<Throwable, Publisher<?>>() {
            @Override
            public Publisher<?> apply(Throwable throwable) throws Exception {
                if (++retryCount <= maxRetries) {

                    Log.i("RetryWithDelay", "get error, it will try after " + retryDelayMillis
                            + " millisecond, retry count " + retryCount);
                    // When this Observable calls onNext, the original
                    // Observable will be retried (i.e. re-subscribed).
                    return Flowable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);

                } else {

                    // Max retries hit. Just pass the error along.
                    return Flowable.error(throwable);
                }
            }
        });
    }
}