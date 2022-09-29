package com.github.lzyzsd.jsbridge.event;

public class Event {

    public static class OnJsBridgeLoadedEvent {
        public String token;

        public OnJsBridgeLoadedEvent(String token) {
            this.token = token;
        }
    }

    public static class OnJsHandlerReady {
        public String token;

        public OnJsHandlerReady(String token) {
            this.token = token;
        }
    }

    public static class OnBackPress {
        public final static String NAME = "onBackPress";

        public OnBackPress() {
        }
    }
}
