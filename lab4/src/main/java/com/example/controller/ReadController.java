package com.example.controller;


import com.example.SpringApplication;
import com.example.dataAccess.implementation.ProcessInputData;
import com.example.view.implementation.ConsoleOutput;

import com.example.view.implementation.EventHubOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Controller
public class ReadController {

    @Autowired
    private ProcessInputData processInputData;

    @Autowired
    private EventHubOutput eventHubOutput;

    @Autowired
    private ConsoleOutput consoleOutput;

    @Value("event")
    private String var;


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String setLink(String url) {

        if (readOption().equals("console")) {
            processInputData.getData(url, consoleOutput);
        } else if (readOption().equals("event")) {
            processInputData.getData(url, eventHubOutput);
        }

        return "redirect:/link";
    }

    public String readOption() {
        try {
            File myObj = new File("/Users/liudwig/Desktop/noSQLDB/demo2/src/main/resources/filename.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                return data;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }
}