package com.rx.rxutil3.rxjava;

import com.rx.rxutil3.rxjava.scheduler.SchedulerType;

import org.reactivestreams.Publisher;

import java.util.concurrent.Executor;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
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


/**
 * 线程切换
 *
 * @author xuexiang
 * @since 2018/6/12 下午11:25
 */
public class SchedulerTransformer<T> implements ObservableTransformer<T, T>, FlowableTransformer<T, T>, SingleTransformer<T, T>, MaybeTransformer<T, T>, CompletableTransformer {

    /**
     * 线程类型
     */
    private SchedulerType mSchedulerType;
    /**
     * io线程池
     */
    private Executor mIOExecutor;

    public SchedulerTransformer() {
        this(SchedulerType._io_main, RxSchedulerUtils.getIOExecutor());
    }

    /**
     * 构造方法
     *
     * @param schedulerType 线程类型
     */
    public SchedulerTransformer(SchedulerType schedulerType) {
        this(schedulerType, RxSchedulerUtils.getIOExecutor());
    }

    /**
     * 构造方法
     *
     * @param executor 线程池
     */
    public SchedulerTransformer(Executor executor) {
        this(SchedulerType._io_main, executor);
    }

    /**
     * 构造方法
     *
     * @param schedulerType 线程类型
     * @param executor 线程池
     */
    public SchedulerTransformer(SchedulerType schedulerType, Executor executor) {
        mSchedulerType = schedulerType;
        mIOExecutor = executor;
    }

    /**
     * 设置自定义IO线程池
     * @param executor
     * @return
     */
    public SchedulerTransformer<T> setIOExecutor(Executor executor) {
        mIOExecutor = executor;
        return this;
    }

    /**
     * 设置线程类型
     * @param schedulerType
     * @return
     */
    public SchedulerTransformer<T> setSchedulerType(SchedulerType schedulerType) {
        mSchedulerType = schedulerType;
        return this;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        switch (mSchedulerType) {
            case _main:
                return upstream.observeOn(AndroidSchedulers.mainThread());
            case _io:
                return upstream.observeOn(RxSchedulerUtils.io(mIOExecutor));
            case _io_main:
                return upstream
                        .subscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .unsubscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .observeOn(AndroidSchedulers.mainThread());
            case _io_io:
                return upstream
                        .subscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .unsubscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .observeOn(RxSchedulerUtils.io(mIOExecutor));
            default:
                break;
        }
        return upstream;
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        switch (mSchedulerType) {
            case _main:
                return upstream.observeOn(AndroidSchedulers.mainThread());
            case _io:
                return upstream.observeOn(RxSchedulerUtils.io(mIOExecutor));
            case _io_main:
                return upstream
                        .subscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .unsubscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .observeOn(AndroidSchedulers.mainThread());
            case _io_io:
                return upstream
                        .subscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .unsubscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .observeOn(RxSchedulerUtils.io(mIOExecutor));
            default:
                break;
        }
        return upstream;
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        switch (mSchedulerType) {
            case _main:
                return upstream.observeOn(AndroidSchedulers.mainThread());
            case _io:
                return upstream.observeOn(RxSchedulerUtils.io(mIOExecutor));
            case _io_main:
                return upstream
                        .subscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .unsubscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .observeOn(AndroidSchedulers.mainThread());
            case _io_io:
                return upstream
                        .subscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .unsubscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .observeOn(RxSchedulerUtils.io(mIOExecutor));
            default:
                break;
        }
        return upstream;
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        switch (mSchedulerType) {
            case _main:
                return upstream.observeOn(AndroidSchedulers.mainThread());
            case _io:
                return upstream.observeOn(RxSchedulerUtils.io(mIOExecutor));
            case _io_main:
                return upstream
                        .subscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .unsubscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .observeOn(AndroidSchedulers.mainThread());
            case _io_io:
                return upstream
                        .subscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .unsubscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .observeOn(RxSchedulerUtils.io(mIOExecutor));
            default:
                break;
        }
        return upstream;
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        switch (mSchedulerType) {
            case _main:
                return upstream.observeOn(AndroidSchedulers.mainThread());
            case _io:
                return upstream.observeOn(RxSchedulerUtils.io(mIOExecutor));
            case _io_main:
                return upstream
                        .subscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .unsubscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .observeOn(AndroidSchedulers.mainThread());
            case _io_io:
                return upstream
                        .subscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .unsubscribeOn(RxSchedulerUtils.io(mIOExecutor))
                        .observeOn(RxSchedulerUtils.io(mIOExecutor));
            default:
                break;
        }
        return upstream;
    }
}
