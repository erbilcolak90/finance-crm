package com.financecrm.webportal.websocket;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.input.userrole.GetUserRolesByUserIdInput;
import com.financecrm.webportal.services.CustomUserService;
import com.financecrm.webportal.services.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WSService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private UserRoleService userRoleService;

    public void notifyAdmin(WsMessage wsMessage){

        User user = customUserService.findByUserId(wsMessage.getSender());

        if (user != null) {
            List<String> roleList = userRoleService.getUserRolesByUserId(new GetUserRolesByUserIdInput(user.getId()));

            if (roleList.contains("ADMIN")) {
                wsMessage.setSender(user.getName());
                simpMessagingTemplate.convertAndSend("/topic/admin", wsMessage);
            }
        }

    }


}
