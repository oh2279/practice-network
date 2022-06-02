package com.subwaygame.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subwaygame.Entity.ChatMessage;
import com.subwaygame.Entity.ChatRoom;
import com.subwaygame.repository.ChatRoomRepository;
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

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("메세지 전송 = {} : {}",session,message.getPayload());
        String msg = message.getPayload();

        String parsedMsg = parsingMsg(msg); // 메시지만 따로 파싱해서 저장해놈
        log.info("Parsed message = {}", parsedMsg);

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

}
