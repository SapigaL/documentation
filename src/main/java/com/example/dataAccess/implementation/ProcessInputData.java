package com.example.dataAccess.implementation;

import com.example.dataAccess.interafces.IProcessData;
import com.example.view.interfaces.IDataOutput;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


@Component
public class ProcessInputData implements IProcessData {

    @Autowired
    private StringRedisTemplate template;


    private static final boolean USE_SSL = true;
    private static final int MAX_NUMBER = 1000;

    private final static String CACHE_HOSTNAME = "labredis.redis.cache.windows.net";
    private final static String CACHE_KEY = "nbs9Tm88x+uws8+UadeRPcftsZ1QboT97+h9vNaSNSo=";
    private final static String MAP_NAME = "Log1";
    private final static int PORT = 6380;



    @Override
    public void getData(String url, IDataOutput dataOutput) {
        JedisShardInfo info = new JedisShardInfo(CACHE_HOSTNAME, PORT, USE_SSL);
        info.setPassword(CACHE_KEY);
        Jedis jedis = new Jedis(info);


        ValueOperations<String, String> ops = this.template.opsForValue();

        if (this.template.hasKey(url)) {
            //ops.set("url", "Loading");
            jedis.hset(MAP_NAME, "Status","Loading");
        } else {
            //ops.set("url", "Second try");
            System.out.println("data was already recorded");
            jedis.hset(MAP_NAME, "Status","data was already recorded");
            return;
        }

        try {
            URL stockURL = new URL(url);
            try {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(stockURL.openStream()));
                String stringData = null;
                StringBuilder stringBuffer = new StringBuilder();
                String filePart = "";
                int i = 0;
                int j = 0;
                int numberOfRows = 0;
                while ((stringData=buffer.readLine())!=null) {
                    stringBuffer.append(stringData);
                    if (i == 100 + j) {
                        numberOfRows++;
                        filePart = stringBuffer.toString();
                        stringBuffer.setLength(0);
                        dataOutput.sendData(filePart);
                        System.out.println("Rows Loaded" + (j) +"-" + (i));
                        jedis.hset(MAP_NAME, "Rows" +numberOfRows,(j) +"-" + (i));
                        ops.set("url","Rows Loaded" + (j) +"-" + (i));
                        j = i;
                    } else if (buffer.readLine()==null) {
                        numberOfRows++;
                        filePart = stringBuffer.toString();
                        dataOutput.sendData(filePart);
                        ops.set("url","Rows Loaded" + (j) +"-" + (i));
                        jedis.hset(MAP_NAME, "Rows",(j) +"-" + (i));

                    }
                    i++;
                  //  System.out.println("count i: "+ i);
                }
                filePart = stringBuffer.toString();
                dataOutput.sendData(filePart);
                ops.set("url", "Completed");
                jedis.hset(MAP_NAME, "EndStatus","Completed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}