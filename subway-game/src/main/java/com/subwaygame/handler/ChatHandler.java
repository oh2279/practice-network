package com.subwaygame.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subwaygame.Entity.ChatMessage;
import com.subwaygame.Entity.ChatRoom;
import com.subwaygame.Entity.MessageType;
import com.subwaygame.repository.ChatRoomRepository;
import com.subwaygame.service.ChatService;
import com.subwaygame.service.ReadJsonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {
    public static int userCount=0;
    public static boolean canPlayGame,gameStart = false;
    private String randomLine;
    private final ChatRoomRepository chatRoomRepository;
    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private final ReadJsonService readJsonService;


    private final Random rand = new Random();
    private final ArrayList<String> subwayLine =new ArrayList<String>(){
        {
            add("01호선"); add("02호선"); add("03호선"); add("04호선"); add("05호선");
            add("06호선"); add("07호선"); add("08호선"); add("09호선");
        }
    };


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("메세지 전송 = {} : {}",session,message.getPayload());
        String msg = message.getPayload();

        String parsedMsg = parsingMsg(msg); // 메시지만 따로 파싱해서 저장해놈
        log.info("Parsed message = {}", parsedMsg);

        ChatMessage chatMessage = objectMapper.readValue(msg,ChatMessage.class);
        ChatRoom chatRoom = chatRoomRepository.findRoomById(chatMessage.getChatRoomId());

        /*if (parsedMsg.equals("게임시작")){ // 게임시작 로직 짜야함
            readJsonService.getJsonAndStart();
        }*/


        if(userCount == 2 && !gameStart){ // 입장이 2명이면 게임시작
            readJsonService.getJsonAndStart();
            chatRoom.handleMessage(session,chatMessage,objectMapper,randomLine);
            chatMessage.setType(MessageType.START);
            // 게임이 시작되면
            gameStart = true;
        }
        else if(gameStart && !canPlayGame){
            Collections.shuffle(subwayLine);
            int randomNum = rand.nextInt(9); // 9미만의 랜덤값
            randomLine = subwayLine.get(randomNum);

            readJsonService.setSubwayLine(randomLine); // 호선 랜덤 지정
            chatRoom.handleMessage(session,chatMessage,objectMapper,randomLine);
            chatMessage.setType(MessageType.SUBWAYLINE);
            canPlayGame = true;
            userCount=0;
        }
        /*else if (parsedMsg.equals("01호선")){ // 호선 형식 {01호선, 02호선, ..., 09호선, 경의선, 수인분당선, 경춘선, 경강선, 인천선, 인천2호선,
            // 공항철도, 신림선, 신분당선, 용인경전철,서해선, 김포도시철도, 의정부경전철, 우이신설경전철
            readJsonService.setSubwayLine("01호선");
        }*/
        else if (!readJsonService.checkStation(parsedMsg)){
            log.info("못찾음 ㅠ");
        }

        // 이때까지 말한것 중에서 중복확인(중복이면 True 반환)
        Boolean duplicate = chatService.checkDuplicate(parsedMsg);

        chatRoom.handleMessage(session,chatMessage,objectMapper,randomLine);
        log.info("유저 수 : {}",userCount); // 유저 수 확인

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
