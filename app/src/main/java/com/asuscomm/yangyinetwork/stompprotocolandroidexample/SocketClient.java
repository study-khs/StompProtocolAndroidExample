package com.asuscomm.yangyinetwork.stompprotocolandroidexample;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.java_websocket.WebSocket;

import java.util.HashMap;

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
    private final String TAG = "jaeyoung/"+getClass().getSimpleName();

    public SocketClient() {

        this.mStompClient = Stomp.over(WebSocket.class,"ws://13.124.12.120:8082/hello/websocket");
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
//                        Log.d(TAG, "Stomp connection closed");
                        break;
                }
            }
        });

        String topic = "/topic/greetings/"+"1";
        this.mStompClient.topic(topic).subscribe(new Action1<StompMessage>() {
                                                                  @Override
                                                                  public void call(StompMessage stompMessage) {
                                                                      Log.d(TAG, stompMessage.getPayload());
                                                                  }
                                                              });

        String topic_hobby = "/user/queue/1";
        this.mStompClient.topic(topic_hobby).subscribe(new Action1<StompMessage>() {
            @Override
            public void call(StompMessage stompMessage) {
                Log.d(TAG, "hobby "+stompMessage.getPayload());
            }
        });


        this.mStompClient.connect();

        HashMap<String, String> a = new HashMap<String, String>();
        HashMap<String, String> hobby = new HashMap<String, String>();
        a.put("name", "iam");
        hobby.put("hobby", "hit the jsp");
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        String json_hobby = "";
        try {
            json = mapper.writeValueAsString(a);
            json_hobby = mapper.writeValueAsString(hobby);
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        this.mStompClient.send("/app/1/hello", json).subscribe();
        this.mStompClient.send("/app/1/message", json_hobby).subscribe();

        // ...

//        this.mStompClient.disconnect();

    }
}
