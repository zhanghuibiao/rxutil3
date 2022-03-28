package com.rx.rxutil3.lifecycle;

import org.reactivestreams.Publisher;


import static com.rx.rxutil3.lifecycle.ActivityLifecycle.onDestroy;
import static com.rx.rxutil3.lifecycle.ActivityLifecycle.onPause;
import static com.rx.rxutil3.lifecycle.ActivityLifecycle.onStop;

import androidx.annotation.NonNull;


import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableSource;
import io.reactivex.rxjava3.core.CompletableTransformer;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableTransformer;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeSource;
import io.reactivex.rxjava3.core.MaybeTransformer;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;

/**
 * 生命周期转化器
 *
 * @author xuexiang
 * @since 2018/6/11 上午12:50
 */
public class LifecycleTransformer<T> implements ObservableTransformer<T, T>, FlowableTransformer<T, T>, SingleTransformer<T, T>, MaybeTransformer<T, T>, CompletableTransformer {
    private Observable<?> mObservable;


    LifecycleTransformer(Observable<ActivityLifecycle> lifecycleObservable) {
        Observable<ActivityLifecycle> observable = lifecycleObservable.share();
        mObservable = Observable.combineLatest(observable.take(1).map(ACTIVITY_LIFECYCLE), observable.skip(1),
                new BiFunction<ActivityLifecycle, ActivityLifecycle, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull ActivityLifecycle ActivityLifecycle, @NonNull ActivityLifecycle ActivityLifecycle2) throws Exception {
                        return ActivityLifecycle.equals(ActivityLifecycle2);
                    }
                })
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(@NonNull Boolean aBoolean) throws Exception {
                        return aBoolean;
                    }
                });

    }

    LifecycleTransformer(Observable<ActivityLifecycle> lifecycleObservable, final ActivityLifecycle ActivityLifecycle) {
        mObservable = lifecycleObservable
                .filter(new Predicate<ActivityLifecycle>() {
                    @Override
                    public boolean test(@NonNull ActivityLifecycle event) throws Exception {
                        return event.equals(ActivityLifecycle);
                    }
                })
                .take(1);
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.takeUntil(mObservable);
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.takeUntil(mObservable.toFlowable(BackpressureStrategy.LATEST));
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return upstream.takeUntil(mObservable.firstOrError());
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return upstream.takeUntil(mObservable.firstElement());
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        return Completable.ambArray(upstream);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LifecycleTransformer<?> that = (LifecycleTransformer<?>) o;

        return mObservable.equals(that.mObservable);
    }

    @Override
    public int hashCode() {
        return mObservable.hashCode();
    }

    @Override
    public String toString() {
        return "LifecycleTransformer{" +
                "mObservable=" + mObservable +
                '}';
    }


    // Figures out which corresponding next lifecycle event in which to unsubscribe, for Activities
    private static final Function<ActivityLifecycle, ActivityLifecycle> ACTIVITY_LIFECYCLE =
            new Function<ActivityLifecycle, ActivityLifecycle>() {
                @Override
                public ActivityLifecycle apply(@NonNull ActivityLifecycle lastEvent) throws Exception {
                    switch (lastEvent) {
                        case onCreate:
                            return onDestroy;
                        case onStart:
                            return onStop;
                        case onResume:
                            return onPause;
                        case onPause:
                            return onStop;
                        case onStop:
                            return onDestroy;
                        case onDestroy:
                            throw new IllegalStateException("Cannot injectRxLifecycle to Activity lifecycle when outside of it.");
                        default:
                            throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
                    }
                }
            };
}
