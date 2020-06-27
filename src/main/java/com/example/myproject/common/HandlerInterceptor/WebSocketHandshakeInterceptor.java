package com.example.myproject.common.HandlerInterceptor;

import com.example.myproject.entity.sys.view.UserLoginView;
import org.apache.shiro.SecurityUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-02-02 22:49
 **/
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        UserLoginView user = (UserLoginView) SecurityUtils.getSubject().getPrincipal();
        map.put("WEBSOCKET_USERNAME", user.getUsername());
        map.put("WEBSOCKET_USER_ID", user.getId()); //将用户标识放入参数列表后，下一步的websocket处理器可以读取这里面的数据
        map.put("WEBSOCKET_LOGIN", user.getPassword());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
