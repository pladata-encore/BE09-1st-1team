package com.bookmark.library.service;

import java.util.HashMap;
import java.util.Map;

public class Services {
    private static final Map<Class<?>, Object> services = new HashMap<>();

    public static <T> T resolve(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }

    public static <T> void register(T service) {
        services.put(service.getClass(), service);
    }
}
