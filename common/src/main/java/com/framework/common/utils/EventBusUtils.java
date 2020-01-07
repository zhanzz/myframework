package com.framework.common.utils;

import com.framework.common.data.EventMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

public class EventBusUtils {

    private EventBusUtils() {
    }

    /**
     * 注册 EventBus
     *
     * @param subscriber
     */
    public static void register(Object subscriber) {
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(subscriber)) {
            try{
                eventBus.register(subscriber);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 解除注册 EventBus
     *
     * @param subscriber
     */
    public static void unregister(Object subscriber) {
        EventBus eventBus = EventBus.getDefault();
        if (eventBus.isRegistered(subscriber)) {
            eventBus.unregister(subscriber);
        }
    }

    /**
     * 发送事件消息
     *
     * @param event
     */
    public static void post(Object event) {
        if(event==null){
            return;
        }
        EventBus.getDefault().post(event);
    }

    /**
     * 发送粘性事件消息
     *
     * @param event
     */
    public static void postSticky(Object event) {
        if(event==null){
            return;
        }
        EventBus.getDefault().postSticky(event);
    }
}