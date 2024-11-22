package com.baixiu.middleware.conductor.script;

import com.baixiu.middleware.conductor.handler.EventHandler;

import java.util.*;
import java.util.concurrent.ExecutorService;

public class EventScriptRuntimeBuilder {

    private final ExecutorService executorService;
    private final EventScript script;

    public EventScriptRuntimeBuilder(EventScript script, ExecutorService executorService) {
            this.script = script;
            this.executorService = executorService;
            this.preparePrototypes();
        }

    private void preparePrototypes() {
        Map<EventHandler, List<EventHandler> dependedHandlerMap = this.copyEventHandlerMap(this.script.getdenpendedEventHandlers());
        Map<EventHandler, List<EventHandler>> dependingHandlerMap = new HashMap();
        Iterator i$ = dependedHandlerMap.keySet().iterator();

        EventHandler eventHandler;
        while(i$.hasNext()) {
            eventHandler = (EventHandler)i$.next();
            dependingHandlerMap.put(eventHandler, new ArrayList(1));
        }

        i$ = dependedHandlerMap.keySet().iterator();

        Iterator i$;
        EventHandler handler;
        while(i$.hasNext()) {
            eventHandler = (EventHandler)i$.next();
            i$ = ((List)dependedHandlerMap.get(eventHandler)).iterator();

            while(i$.hasNext()) {
                handler = (EventHandler)i$.next();
                ((List)dependingHandlerMap.get(handler)).add(eventHandler);
            }
        }

        EventScriptRuntimeBuilder.ScriptEndEventHandler scriptEndEventHandler = new ScriptEndEventHandler();
        List<EventHandler> scriptEndDependingHandlers = new ArrayList(1);
        i$ = dependedHandlerMap.keySet().iterator();

        while(i$.hasNext()) {
            handler = (EventHandler)i$.next();
            List<EventHandler<Event>> dependedHandlers = (List)dependedHandlerMap.get(handler);
            if (dependedHandlers.isEmpty()) {
                scriptEndDependingHandlers.add(handler);
                ((List)dependedHandlerMap.get(handler)).add(scriptEndEventHandler);
            }
        }

        dependedHandlerMap.put(scriptEndEventHandler, new ArrayList(0));
        dependingHandlerMap.put(scriptEndEventHandler, scriptEndDependingHandlers);

        Object process;
        for(i$ = dependedHandlerMap.keySet().iterator(); i$.hasNext(); this.processPrototypeMap.put(handler, process)) {
            handler = (EventHandler)i$.next();
            if (handler != scriptEndEventHandler) {
                process = new EventProcess(handler, ((List)dependingHandlerMap.get(handler)).size(), (List)dependedHandlerMap.get(handler));
            } else {
                process = new ScriptEndEventProcess(handler, ((List)dependingHandlerMap.get(handler)).size(), (List)dependedHandlerMap.get(handler));
            }
        }

    }
}
