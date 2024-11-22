package com.baixiu.middleware.conductor;

import com.baixiu.middleware.conductor.handler.AbstractEventHandler;
import com.baixiu.middleware.conductor.handler.EventHandler;
import com.baixiu.middleware.conductor.handler.EventHandlerGroup;
import com.baixiu.middleware.conductor.script.EventScript;
import com.baixiu.middleware.conductor.script.EventScriptRuntimeBuilder;
import javafx.beans.property.adapter.JavaBeanBooleanPropertyBuilder;

import java.util.concurrent.ExecutorService;

/**
 * the conductor of multi thread event
 * 多线程执行处理调度指挥器.用以调度多线程执行器执行情况.
 * @author:baixiu
 * @date:2024年10月25日
 */
public class Conductor {

    //持有多线程池.用以提交 thread .或者 runnable 线程
    private final ExecutorService executorService;

    //当前指挥家持有的事件脚本
    private final EventScript eventScript;


    private EventScriptRuntimeBuilder runTimeBuilder;

    //构造函数.指定本次调度自定义的线程池.并构建本次事件的脚本处理器
    public Conductor(ExecutorService executorService) {
        this.executorService = executorService;
        this.eventScript=new EventScript<>();
    }

    //创建一个事件组
    public EventHandlerGroup<?> initFirstHandlerGroup(EventHandler<?> ...abstractEventHandlers){
        return this.eventScript.start(abstractEventHandlers);
    }

    //添加一个事件组
    public EventHandlerGroup appendHandlerGroup(EventHandler<?> ...abstractEventHandlers){
        return this.eventScript.then(abstractEventHandlers);
    }

    public synchronized void start(){
        if(!this.eventScript.isReady()){
            //开启事件脚本
            this.eventScript.ready();
            this.runTimeBuilder.build().start();
        }

    }

}
