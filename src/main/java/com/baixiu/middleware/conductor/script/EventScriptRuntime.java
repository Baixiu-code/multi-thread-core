package com.baixiu.middleware.conductor.script;

import com.baixiu.middleware.conductor.callback.EventCallback;
import com.baixiu.middleware.conductor.handler.EventHandler;
import com.baixiu.middleware.conductor.handler.exceute.EventHandlerProcess;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 时间脚本执行器.
 * 完成本次脚本内所有事件的提交
 * @author baixiu
 * @date 2024年10月30日
 */
public class EventScriptRuntime<Event> {

    //脚本运行器持有的事件
    private final Event event;
    //脚本运行器所持有的线程池
    private final ExecutorService executorService;
    //线程执行器
    private final CountDownLatch completeLatch;
    //事件与事件对应的事件执行器
    private final Map<EventHandler<Event>, EventHandlerProcess<Event>> processMap;
    //事件回调器
    private final EventCallback<Event> callback;
    //当前脚本的超时时间
    private final long timeout;
    //异常
    private volatile Throwable error;
    //锁
    private final Lock lock = new ReentrantLock();
    //是否取消
    private volatile boolean canceled = false;
    //事件句柄
    private final ArrayList<Future<?>> futures;
    //控事件句柄
    private static final ArrayList<Future<?>> EmptyFutures = new ArrayList(0);


    public EventScriptRuntime(Event event, ExecutorService executorService, CountDownLatch completeLatch
            , Map<EventHandler<Event>, EventHandlerProcess<Event>> processMap, EventCallback<Event> callback
            , long timeout, ArrayList<Future<?>> futures) {
        this.event = event;
        this.executorService = executorService;
        this.processMap = processMap;
        this.callback = callback;
        if (callback == null) {
            this.completeLatch = new CountDownLatch(1);
        } else {
            this.completeLatch = null;
        }

        this.timeout = timeout;
        if (this.timeout > 0L) {
            this.futures = new ArrayList(1);
        } else {
            this.futures = EmptyFutures;
        }
    }
}
