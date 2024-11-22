package com.baixiu.middleware.conductor.handler.exceute;

import com.baixiu.middleware.conductor.handler.EventHandler;
import java.util.IdentityHashMap;
import java.util.List;

//真正的事件的执行器.
//1.完成 eventHandler 类的实现类的具体实现执行.
//2.让调度的线程池通过线程池 submit 去完成事件的提交.
//3.具体执行过程中去获取当前事件的依赖事件.通过递归完成依赖事件的的执行.这样就能够解决事件依赖的优先执行问题.
public class EventHandlerProcess<Event> implements Runnable {

    private IdentityHashMap<EventHandler<Event>, List<EventHandlerProcess<Event>>>


}
