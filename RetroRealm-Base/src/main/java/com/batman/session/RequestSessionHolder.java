package com.batman.session;

import org.springframework.stereotype.Component;

@Component
public class RequestSessionHolder {

    private final ThreadLocal<RetroRequestSession> retroRequestSession = new ThreadLocal<>();

    public void initSession() {
        retroRequestSession.set(new RetroRequestSession());
    }

    public RetroRequestSession getCurrentSession() {
        return retroRequestSession.get();
    }

    public void removeCurrentSession() {
        retroRequestSession.remove();
    }

    public void flushSession() {
        retroRequestSession.remove();
    }
}
