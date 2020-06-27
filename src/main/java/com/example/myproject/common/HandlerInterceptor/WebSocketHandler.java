package com.example.myproject.common.HandlerInterceptor;

import com.example.myproject.entity.sys.view.UserLoginView;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Description Websocket处理器
 * @Create: 2020-02-02 22:53
 **/
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private SessionDAO sessionDAO;
    @Autowired
    private RedisTemplate redisTemplate;
    // 已建立连接的用户
    private static final ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();

    /**
     * 处理前端发送的文本信息 js调用websocket.send时候，会调用该方法
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        String userId = (String) session.getAttributes().get("WEBSOCKET_USER_ID");

        // 分割成id和信息内容
        String[] messageInfo = message.getPayload().split("@");
        if (messageInfo.length != 2) {
            sendMessageToUser(userId, new TextMessage("500@服务器出错请稍后再发送吧"));
        } else {
            String target = messageInfo[0];
            String content = messageInfo[1];
            // 遍历所有已连接用户
            for (WebSocketSession user : users) {
                if (user.getAttributes().get("WEBSOCKET_USER_ID").equals(target)) {
                    //遇到匹配用户 连接正常则发送消息
                    if (user.isOpen()) {
                        sendMessageToUser(target, new TextMessage("200@"+content));
                    }else{//若异常则发送失败
                        sendMessageToUser(userId, new TextMessage("404@对方在线异常,发送失败"));
                    }
                    return;
                }
            }
            //未找到匹配用户 发送失败
            sendMessageToUser(userId, new TextMessage("404@对方暂时不在线"));
        }
    }

    /**
     * 当新连接建立的时候，被调用 连接成功时候，会触发页面上onOpen方法
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        users.add(session);
        String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        log.info("用户 {} Connection Established",username);
        //session.sendMessage(new TextMessage(username + " connect"));
    }

    /**
     * 当连接关闭时被调用
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        users.remove(session);
        String userId = (String) session.getAttributes().get("WEBSOCKET_USER_ID");
        if(redisTemplate.hasKey(userId))return;
        String flag = (String) session.getAttributes().get("WEBSOCKET_LOGIN");
        if(!Boolean.valueOf(flag)) {
            Collection<Session> sessions = sessionDAO.getActiveSessions();
            for (Session sess : sessions) {

                SimplePrincipalCollection coll = (SimplePrincipalCollection) sess.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                if (coll == null) continue;
                UserLoginView userLoginView = (UserLoginView) coll.getPrimaryPrincipal();
                if (userId.equals(userLoginView.getId())) {
                    sess.setTimeout(0);
                    sess.stop();
                    sessionDAO.delete(sess);
                }
            }
        }
    }

    /**
     * 传输错误时调用
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        if (session.isOpen()) {
            session.close();
        }
        log.info("用户: {} websocket connection closed......",username);
        users.remove(session);
    }

    /**
     * 给所有在线用户发送消息
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        for (WebSocketSession user : users) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给某个用户发送消息
     * @param userId
     * @param message
     */
    public void sendMessageToUser(String userId, TextMessage message) {
        for (WebSocketSession user : users) {
            if (user.getAttributes().get("WEBSOCKET_USER_ID").equals(userId)) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
