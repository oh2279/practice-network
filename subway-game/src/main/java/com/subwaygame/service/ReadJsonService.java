package com.subwaygame.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReadJsonService {
    private JSONArray data;

    private String setStation;

    private ArrayList<String> stationList = new ArrayList<>();

    public void getJsonAndStart() throws IOException, ParseException{
        JSONParser parser = new JSONParser();
        Reader reader = new FileReader("C:\\Users\\KANG\\Desktop\\CN\\CN_project\\subway-game\\src\\main\\java\\com\\subwaygame\\service\\subwayInfo.json");
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        data = (JSONArray) jsonObject.get("DATA");
        log.info("ReadJson");
        reader.close();
    }

    public Boolean checkStation(String parsedMsg){
        boolean flag = false;
        for (String checkStation : stationList){
            if (checkStation.equals(parsedMsg)){
                flag = true;
                log.info("찾았다..!");
            }
        }
        return flag;
    }

    public void setSubwayLine(String station){
        setStation = station;
        stationList.clear();
        if (data.size() > 0){
            for (int i = 0; i < data.size(); i++){
                JSONObject c = (JSONObject) data.get(i);
                if (c.get("line_num").toString().equals(station)){
                    stationList.add(c.get("station_nm").toString());
                }
            }
        }

    }



}
