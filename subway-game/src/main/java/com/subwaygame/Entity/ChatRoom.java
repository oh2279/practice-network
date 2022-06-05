package com.subwaygame.Entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subwaygame.handler.ChatHandler;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ChatRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    private boolean welcome;
    private String first="";

    public static ChatRoom create(String name){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }

    public void handleMessage(WebSocketSession session, ChatMessage chatMessage,
                              ObjectMapper objectMapper, String randomLine) throws IOException {
        welcome = false;

        if(chatMessage.getType() == MessageType.ENTER){
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getWriter() + "님이 입장하셨습니다.");
            ChatHandler.userCount++;
            if(ChatHandler.userCount==1){
                first = chatMessage.getWriter();
            }
            welcome = true;
        }
        else if(chatMessage.getType() == MessageType.LEAVE){
            sessions.remove(session);
            chatMessage.setMessage(chatMessage.getWriter() + "님이 퇴장하셨습니다.");
            ChatHandler.userCount--;
        }
        else if(chatMessage.getType() == MessageType.START){
            chatMessage.setMessage("게임이 시작되었습니다.");
            send(chatMessage,objectMapper);
            chatMessage.setMessage(first + "님이 '몇호선?' 이라고 물어봐주세요");
        }
        else if(chatMessage.getType() == MessageType.SUBWAYLINE){
            chatMessage.setMessage("아~ " + randomLine + "~ " + randomLine+ "~ " + randomLine + randomLine + randomLine + "!!");
        }
        else if(chatMessage.getType() == MessageType.END){
            chatMessage.setMessage("게임이 종료되었습니다.");
            ChatHandler.userCount=0;
        }
        else{
            chatMessage.setMessage(chatMessage.getWriter() + " : " + chatMessage.getMessage());
        }
        send(chatMessage,objectMapper);
        if(welcome) {
            chatMessage.setMessage("현재 있는 유저 수 : " + ChatHandler.userCount);
            send(chatMessage, objectMapper);
            if(ChatHandler.userCount==2){
                chatMessage.setMessage(chatMessage.getWriter() + "님이 아무 채팅으로 게임을 시작할 수 있습니다.");
                send(chatMessage, objectMapper);
            }
        }
    }

    public void send(ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.
                writeValueAsString(chatMessage.getMessage()));
        for(WebSocketSession sess : sessions){
            sess.sendMessage(textMessage);
        }
    }
}
