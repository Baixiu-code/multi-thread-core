package com.baixiu.middleware.conductor.handler;

import com.baixiu.middleware.conductor.script.EventScript;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;

//事件组处理器.
//用以处理事件以及事件的依赖关系
public class EventHandlerGroup<T> {

    private EventScript<T> eventScript = null;

    private List<EventHandler<T>> abstractEventHandlers;

    //init 事件组
    public EventHandlerGroup(EventScript script,EventHandler<T>[] abstractEventHandlers){
        synchronized (script){
            if(script.isReady()){
                throw new RuntimeException("script is already ready,not allow edit pls");
            }
            //赋值当前剧本执行器
            this.eventScript=script;
            this.abstractEventHandlers=Lists.newArrayListWithCapacity(abstractEventHandlers.length);
            //初始化当前事件组
            this.abstractEventHandlers.addAll(Arrays.asList(abstractEventHandlers));
            for (EventHandler<T> abstractEventHandler : this.abstractEventHandlers) {
                eventScript.addPreDependencyEventHandlers(abstractEventHandler,null);
            }
        }
    }

    //在事件组对象还没有开始时,添加事件组.
    //并更新当前事件组的依赖事件.
    public EventHandlerGroup<T> thenEventHandler(EventHandler<T>[] abstractEventHandlers){
        synchronized (this.eventScript){
            if(this.eventScript.isReady()){
                throw new RuntimeException("script is already ready,not allow edit pls");
            }
            //添加当前对象的现有事件组currentEventHandler依赖事件.
            for (EventHandler<T> currentEventHandler : this.abstractEventHandlers) {
                for (EventHandler<T> newEventHandler : abstractEventHandlers) {
                    this.eventScript.addPreDependencyEventHandlers(currentEventHandler,newEventHandler);
                }
            }
            //添加后面添加的事件组 依赖  事件.事件为空.
            for (EventHandler<T> abstractEventHandler : abstractEventHandlers) {
                this.eventScript.addPreDependencyEventHandlers(abstractEventHandler,null);
            }
            //将后面添加的事件组添加到当前对象的事件组中
            this.abstractEventHandlers.addAll(Arrays.asList(abstractEventHandlers));
            return this;
        }
    }
}
