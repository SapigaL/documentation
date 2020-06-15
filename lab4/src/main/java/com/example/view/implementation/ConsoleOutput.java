package com.example.view.implementation;


import com.example.view.interfaces.IDataOutput;
import org.springframework.stereotype.Component;

@Component
public class ConsoleOutput implements IDataOutput {
    @Override
    public void sendData(String data) {
        System.out.println(data);
    }
}
