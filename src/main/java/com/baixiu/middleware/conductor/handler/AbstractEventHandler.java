package com.baixiu.middleware.conductor.handler;

/**
 * 抽象事件处理.定义具体时间执行的过程提供两个方法.
 * 方法 1:validate
 * 默认实现 validate 校验事件是否可以执行.默认校验通过.当需要进行事件复写时进行重写.以支持扩展
 * 方法 2:doRealProcess
 * 具体事件执行器.在事件校验通过后进行执行.为抽象方法需要子类具体实现方法执行过程.
 * @param <Event>
 */
public abstract class AbstractEventHandler<Event> implements EventHandler<Event> {

    //校验.默认通过,具体可通过子类进行实现.
    public boolean validate(Event event) {
        return true;
    }

    //after 校验通过后 进行执行
    @Override
    public void process(Event event) {
        doRealProcess(event);
    }
    // 具体执行
    public abstract void doRealProcess(Event event);
}
