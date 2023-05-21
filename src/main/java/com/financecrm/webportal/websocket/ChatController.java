package com.financecrm.webportal.websocket;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.input.userrole.GetUserRolesByUserIdInput;
import com.financecrm.webportal.services.CustomUserService;
import com.financecrm.webportal.services.RoleService;
import com.financecrm.webportal.services.UserRoleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Controller
@CrossOrigin
public class ChatController {

    // Mesajı özelleştirip gönderebilmek için SimpMessagingTemlate yapısını kullanıyoruz.
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private WSService wsService;

    @Autowired
    private HttpServletRequest request;

    //registerendpoint
    @MessageMapping("/chat")
    //@SendTo("/topic")
    public void chatEndpoint(@Payload WsMessage wsMessage) {
        User user = customUserService.findByUserId(wsMessage.getSender());
        if (user != null) {
            System.out.println(wsMessage);
            simpMessagingTemplate.convertAndSend("/topic", wsMessage);
        }

    }

    @MessageMapping("/admin")
    public void adminNotification(@Payload WsMessage wsMessage) {
        wsService.notifyAdmin(wsMessage);
    }

}

