package com.baixiu.middleware.conductor.callback;

//事件回调接口
public interface EventCallback<Event> {

    public void onSuccess(Event event);

    public void onFail(Event event,Throwable throwable);

}
