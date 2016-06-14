package com.myapplication.event;

import com.myapplication.app.lifecycle.LifeCycleComponentManager;

import de.greenrobot.event.EventBus;

public class EventCenter {

    private static final EventBus instance = new EventBus();

    private EventCenter() {
    }

    public static final EventBus getInstance() {
        return instance;
    }

    public static SimpleEventHandler bindContainerAndHandler(Object container, SimpleEventHandler handler) {
        LifeCycleComponentManager.tryAddComponentToContainer(handler, container);
        return handler;
    }


}
