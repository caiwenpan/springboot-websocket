package com.alan.wechat.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alan.wechat.entity.Message;
import com.alibaba.fastjson.JSON;
/**
 * @author alanpan
 * @title: WebSocketWeChatServer
 * @projectName springboot-websocket
 * @description: WebSocket 聊天服务端
 * @date 2019/4/122:07
 */
@Component
@ServerEndpoint("/wechat/{username}")//WebSocket服务端 需指定端点的访问路径
public class WebSocketWeChatServer {


    // 记录全部在线会话信息 使用线程安全 Map
    private static Map<String, Session> onlineSessions
            = new ConcurrentHashMap<>();
    private static Map<String, WebSocketWeChatServer> clients = new ConcurrentHashMap<String, WebSocketWeChatServer>();

    /**
     * @return void
     * @Author alan.Pan
     * @Description //当客户端打开连接
     * @Date 22:12 2019/4/1
     * @Param [session]
     **/
    @OnOpen
    public void onOpen(@PathParam("username") String username,Session session) {
        //添加会话对象
    	
        onlineSessions.put(username, session);
      //messageType 1代表上线 2代表下线 3代表在线名单 4代表普通消息
        //先给所有人发送通知，说我上线了
        //更新在线人数
        sendMessageToAll(Message.jsonStr(Message.TypeEnum.onLine.getCode(), username, "",null,"", onlineSessions.size()));
        //给自己发一条消息：告诉自己现在都有谁在线
        Set<String> set = onlineSessions.keySet();
        //移除掉自己
        sendMessageTo(Message.jsonStr(Message.TypeEnum.onLineList.getCode(), "", "",set,"", onlineSessions.size()), username);
    }

    /**
     * @return void
     * @Author alan.Pan
     * @Description //当客户端发送消息
     * @Date 22:13 2019/4/1
     * @Param [session, msg]
     **/
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
    	
        Message message = JSON.parseObject(jsonStr, Message.class);
		/* synchronized (session) { */
        	 //所有人
            if (!StringUtils.isEmpty(message.getTousername())&& message.getTousername().equals("所有"))
            {
                sendMessageToAll(Message.jsonStr(Message.TypeEnum.Other.getCode(), message.getUsername(),message.getTousername(),null, message.getMsg(), onlineSessions.size()));
    		} 
            else
    		{	//发给某一个人
                sendMessageTo(Message.jsonStr(Message.TypeEnum.Other.getCode(), message.getUsername(),message.getTousername(),null, message.getMsg(), onlineSessions.size()),message.getTousername());
    		}
		/* } */
       
    }

    /**
     * @return void
     * @Author alan.Pan
     * @Description //关闭连接
     * @Date 22:20 2019/4/1
     * @Param []
     **/
    @OnClose
    public void onClose(Session session) {
        //从 在线人数中移除 对象
        onlineSessions.remove(session.getId());
        // 更新在线人数
        Set<String> set = onlineSessions.keySet();
        //告诉所有人 自己下线了
        sendMessageToAll(Message.jsonStr(Message.TypeEnum.offLine.getCode(), "", "", set,  "",onlineSessions.size()));
    }
    /**
     * 当通信发生异常：打印错误日志
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    
    /**
     * session.getBasicRemote().sendText(message);  //同步发送
     * session.getAsyncRemote().sendText(message); //异步发送
     * 发送给单个人 
     * @param msg
     * @param username
     */
    private static  void sendMessageTo(String msg,String username)
    {
    	try {
    		for (Map.Entry<String, Session> entity: onlineSessions.entrySet())
    		{
    			if (entity.getKey().equals(username)) 
    				entity.getValue().getAsyncRemote().sendText(msg);
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * 发送信息给所有人
     */
    private static  void  sendMessageToAll(String msg) {
    	try {
	    	for (Session session : onlineSessions.values())
	    	{
	    		session.getAsyncRemote().sendText(msg);
			}
    	} catch (Exception e) {
    		e.printStackTrace();
		}
		
    }
}