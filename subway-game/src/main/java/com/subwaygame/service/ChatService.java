package com.subwaygame.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private ArrayList<String> subwayList = new ArrayList<>();


    public Boolean checkDuplicate(String parsedMsg) {
        for (String subway : subwayList) {
            if (subway.equals(parsedMsg)) {
                log.info("중복 발생");
                return true;
            }
        }

        subwayList.add(parsedMsg);
        return false;
    }
}
