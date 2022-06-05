package com.subwaygame.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subwaygame.Entity.ChatMessage;
import com.subwaygame.Entity.ChatRoom;
import com.subwaygame.repository.ChatRoomRepository;
<<<<<<< Updated upstream
=======
import com.subwaygame.service.ChatService;
import com.subwaygame.service.ReadJsonService;
>>>>>>> Stashed changes
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {
    private final ChatRoomRepository chatRoomRepository;
    private final ObjectMapper objectMapper;

<<<<<<< Updated upstream
=======
    private final ChatService chatService;

    private final ReadJsonService readJsonService;





>>>>>>> Stashed changes
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("메세지 전송 = {} : {}",session,message.getPayload());
        String msg = message.getPayload();

        String parsedMsg = parsingMsg(msg); // 메시지만 따로 파싱해서 저장해놈
        log.info("Parsed message = {}", parsedMsg);

<<<<<<< Updated upstream
=======
        if (parsedMsg.equals("게임시작")){ // 게임시작 로직 짜야함
            readJsonService.getJsonAndStart();
        }
        else if (parsedMsg.equals("01호선")){ // 호선 형식 {01호선, 02호선, ..., 09호선, 경의선, 수인분당선, 경춘선, 경강선, 인천선, 인천2호선,
            // 공항철도, 신림선, 신분당선, 용인경전철,서해선, 김포도시철도, 의정부경전철, 우이신설경전철
            readJsonService.setSubwayLine("01호선");
        }
        else if (!readJsonService.checkStation(parsedMsg)){
            log.info("못찾음 ㅠ");
        }

        // 이때까지 말한것 중에서 중복확인(중복이면 True 반환)
        Boolean duplicate = chatService.checkDuplicate(parsedMsg);

>>>>>>> Stashed changes
        ChatMessage chatMessage = objectMapper.readValue(msg,ChatMessage.class);
        ChatRoom chatRoom = chatRoomRepository.findRoomById(chatMessage.getChatRoomId());
        chatRoom.handleMessage(session,chatMessage,objectMapper);



    }

    public String parsingMsg(String msg) throws ParseException{
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
        if (!(jsonObject.get("message") == null)){
            String parsedMsg = jsonObject.get("message").toString();
            return parsedMsg;
        }
        return "";
    }

<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
}
