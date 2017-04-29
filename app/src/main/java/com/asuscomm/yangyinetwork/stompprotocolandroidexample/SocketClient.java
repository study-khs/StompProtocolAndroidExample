package com.asuscomm.yangyinetwork.stompprotocolandroidexample;

import android.util.Log;

import org.java_websocket.WebSocket;

import rx.functions.Action1;
import ua.naiksoftware.stomp.LifecycleEvent;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

/**
 * Created by jaeyoung on 2017. 4. 29..
 */

public class SocketClient {
    private StompClient mStompClient;
    private String TAG = "TAG";

    public SocketClient() {
        this.mStompClient = Stomp.over(WebSocket.class,"ws://13.124.12.120:8082/");
        this.mStompClient.connect();

        this.mStompClient.topic("/topic/greetings").subscribe(new Action1<StompMessage>() {
                                                                  @Override
                                                                  public void call(StompMessage stompMessage) {
                                                                      Log.d(TAG, stompMessage.getPayload());
                                                                  }
                                                              });

        this.mStompClient.send("/topic/greetings", "hi!").subscribe();

        // ...

        this.mStompClient.disconnect();

        mStompClient.lifecycle().subscribe(new Action1<LifecycleEvent>() {
            @Override
            public void call(LifecycleEvent lifecycleEvent) {
                switch (lifecycleEvent.getType()) {

                    case OPENED:
                        Log.d(TAG, "Stomp connection opened");
                        break;

                    case ERROR:
                        Log.e(TAG, "Error", lifecycleEvent.getException());
                        break;

                    case CLOSED:
                        Log.d(TAG, "Stomp connection closed");
                        break;
                }
            }
        });
    }
}
