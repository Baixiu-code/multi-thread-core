package com.baixiu.middleware.conductor.handler;

/**
 * 事件处理器.支持泛型 T.
 * 以此约定在编排事件中需要统一所有事件处理上下文
 * @author baixiu
 */
public interface EventHandler<Event> {
    
    void process(Event event);
    
}
