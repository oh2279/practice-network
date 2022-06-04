package com.subwaygame.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subwaygame.Entity.ChatMessage;
import com.subwaygame.Entity.ChatRoom;
import com.subwaygame.repository.ChatRoomRepository;
import com.subwaygame.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;


@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {
    private final ChatRoomRepository chatRoomRepository;
    private final ObjectMapper objectMapper;

    private final ChatService chatService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("메세지 전송 = {} : {}",session,message.getPayload());
        String msg = message.getPayload();

        String parsedMsg = parsingMsg(msg); // 메시지만 따로 파싱해서 저장해놈
        log.info("Parsed message = {}", parsedMsg);

        // 이때까지 말한것 중에서 중복확인(중복이면 True 반환)
        Boolean duplicate = chatService.checkDuplicate(parsedMsg);

        ChatMessage chatMessage = objectMapper.readValue(msg,ChatMessage.class);
        ChatRoom chatRoom = chatRoomRepository.findRoomById(chatMessage.getChatRoomId());
        chatRoom.handleMessage(session,chatMessage,objectMapper);
    }

    public String parsingMsg(String msg) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
        if (!(jsonObject.get("message") == null)){
            String parsedMsg = jsonObject.get("message").toString();
            return parsedMsg;
        }
        return "";
    }
}
