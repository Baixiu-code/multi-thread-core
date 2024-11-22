package com.baixiu.middleware.conductor.script;

import com.baixiu.middleware.conductor.handler.AbstractEventHandler;
import com.baixiu.middleware.conductor.handler.EventHandler;
import com.baixiu.middleware.conductor.handler.EventHandlerGroup;
import com.google.common.collect.Lists;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;

/**
 * 用来处理事件中需要多少具体执行的 handler
 * @author 事件剧本
 * @param <Event> 泛型.统一事件处理上下文
 */
public class EventScript<Event> {

    //控制本次脚本是否执行
    private boolean ready=false;

    private final IdentityHashMap<EventHandler, List<EventHandler>> preDependencyEventHandlers=new IdentityHashMap();

    public EventScript(){}


    synchronized public EventHandlerGroup<?> start(EventHandler<?>[] abstractEventHandlers) {
        if(!ready){
            return new EventHandlerGroup(this,abstractEventHandlers);
        }
        throw new RuntimeException("script is already ready,not allow edit pls");
    }

    synchronized public EventHandlerGroup then(EventHandler<?>[] abstractEventHandlers) {
        if(!ready){
            for (EventHandler<?> abstractEventHandler : abstractEventHandlers) {
                if(!this.preDependencyEventHandlers.containsKey(abstractEventHandler)){
                    throw new RuntimeException("event handler is not is pre handlers.pls add into script yet");
                }
            }
            return this.start(abstractEventHandlers);
        }
        throw new RuntimeException("script is already ready,not allow edit pls");
    }

    public EventScript ready(){
        this.ready=true;
        return this;
    }

    public boolean isReady(){
        return ready;
    }

    public void addPreDependencyEventHandlers(EventHandler<Event> currentHandler, EventHandler<Event> dependencyHandlerItem) {
        preDependencyEventHandlers.put(currentHandler, Lists.newArrayListWithCapacity(1));
        if(Objects.nonNull(dependencyHandlerItem)){
            List<EventHandler> currentHandlerDependencies=preDependencyEventHandlers.get(currentHandler);
            if(!currentHandlerDependencies.contains(dependencyHandlerItem)){
                preDependencyEventHandlers.get(currentHandler).add(dependencyHandlerItem);
            }
        }
    }


}
