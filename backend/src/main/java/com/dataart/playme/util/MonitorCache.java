package com.dataart.playme.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MonitorCache {

    private final Map<String, Object> monitors = new HashMap<>();

    public Object addMonitor(String monitorName) {
        Object newMonitor = new Object();
        monitors.put(monitorName, newMonitor);
        return newMonitor;
    }

    public Object getMonitorByName(String name) {
        Object o = monitors.get(name);
        if (o == null) {
            return new Object();
        }
        return o;
    }
}
