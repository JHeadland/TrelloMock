package com.example.trellomock.task;

import java.io.Serializable;
import java.util.EventListener;

public interface TaskUpdateService extends EventListener, Serializable {
    void onEvent();
}
