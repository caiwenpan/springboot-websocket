package com.alan.wechat.entity;

import java.util.Set;

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author alanpan
 * @title: Message
 * @projectName springboot-websocket
 * @description: 聊天消息类
 * @date 2019/4/121:57
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {

	private Integer type;// 消息类型 1代表上线 2代表下线 3代表在线名单 4代表普通消息

	private String username; // 发送人

	private String tousername;// 接受人
	
	private Set<String> onlineUsers;

	private String msg; // 发送消息

	private int onlineCount; // 在线用户数

	public static String jsonStr(Integer type, String username, String tousername,Set<String> onlineUsers ,String msg, int onlineTotal) {
		return JSON.toJSONString(new Message(type, username, tousername,onlineUsers, msg, onlineTotal));
	}

	public enum TypeEnum {
		onLine(1, "在线"), offLine(2, "下线"), onLineList(3, "在线名单"), Other(4, "普通消息");
		private final Integer code;
		private final String info;

		TypeEnum(Integer code, String info) {
			this.code = code;
			this.info = info;
		}

		public Integer getCode() {
			return code;
		}

		public String getInfo() {
			return info;
		}
	}
}
