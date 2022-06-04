package com.subwaygame.controller;

import com.subwaygame.Entity.ChatRoom;
import com.subwaygame.dto.ChatRoomFormDto;
import com.subwaygame.repository.ChatRoomRepository;
import com.subwaygame.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/game")
    public String rooms(Model model){
        model.addAttribute("rooms",chatRoomRepository.findAllRoom());
        return "rooms";
    }

    @GetMapping("/rooms/{id}")
    public String room(@PathVariable String id, Principal principal, Model model){
        ChatRoom room = chatRoomRepository.findRoomById(id);

        String email = principal.getName();

        model.addAttribute("room",room);
        model.addAttribute("email", email);
        return "room";
    }

    @GetMapping("/new")
    public String make(Model model){
        ChatRoomFormDto form = new ChatRoomFormDto();
        model.addAttribute("form",form);
        return "newRoom";
    }

    @GetMapping("/room/new")
    public String makeRoom(ChatRoomFormDto form){
        chatRoomRepository.createChatRoom(form.getName());

        return "redirect:/game";
    }

}