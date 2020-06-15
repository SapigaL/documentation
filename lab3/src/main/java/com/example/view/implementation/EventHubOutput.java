package com.example.view.implementation;


import com.example.view.interfaces.IDataOutput;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@EnableBinding(Source.class)
@Component
public class EventHubOutput implements IDataOutput {

    @Autowired
    private Source source;

    @Override
    public void sendData(String data) {
        this.source.output().send(new GenericMessage<>(data));
    }
}
