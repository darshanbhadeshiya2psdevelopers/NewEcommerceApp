package com.db.newecom.Utills;

import org.greenrobot.eventbus.EventBus;

public class GlobalBus {

    private GlobalBus() {
        throw new UnsupportedOperationException();
    }

    private static EventBus sBus;

    public static EventBus getBus() {
        if (sBus == null)
            sBus = EventBus.getDefault();
        return sBus;
    }

}
