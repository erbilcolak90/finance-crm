package com.financecrm.webportal.websocket;

import com.financecrm.webportal.auth.CustomUserDetailsService;
import com.financecrm.webportal.auth.TokenManager;
import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.services.CustomUserService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
@Slf4j
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {


    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private CustomUserService customUserService;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,StompHeaderAccessor.class);

                assert accessor != null;

                if(StompCommand.CONNECT.equals(accessor.getCommand())){
                    log.info("Connect");
                    String token = getJwtFromRequest(accessor);
                    if(token != null && StringUtils.hasText(token) && tokenManager.tokenValidate(token)){
                        String userId = tokenManager.parseUserIdFromToken(token);
                        UserDetails user = customUserDetailsService.loadUserByUsername(userId);
                        val principal = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                        accessor.setUser(principal);
                        accessor.setHeader("Authorization","Bearer " + token);
                        accessor.getMessageHeaders();
                    }
                }
                if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())){
                    log.info("Subscribe " + accessor.getDestination());

                }
                if(StompCommand.DISCONNECT.equals(accessor.getCommand())){
                    log.info("Disconnect");
                }
                return message;
            }
        });
    }

    private String getJwtFromRequest(StompHeaderAccessor accessor) {
        String headerList = accessor.getFirstNativeHeader("Authorization");
        if (StringUtils.hasText(headerList) && headerList.startsWith("Bearer ")) {
            return headerList.substring(7);
        } else {
            return null;
        }
    }

    // sunucunun dinlediği
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
       // registry.addEndpoint("/chat").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/admin").setAllowedOriginPatterns("*").withSockJS();
        //registry.addEndpoint("/user").setAllowedOriginPatterns("*").withSockJS();
    }

    //client ın dinlediği
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic/admin");

    }



}
