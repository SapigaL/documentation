package com.example.dataAccess.interafces;

import com.example.view.interfaces.IDataOutput;

public interface IProcessData {
    void getData(String url, IDataOutput dataOutput);
}
