package com.catand.catandbotshirov2.plugin.SPDNet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.File;

@Component
public class SPDNetGetKeyPlugin extends BotPlugin {
	MsgUtils sendMsg;
	static MailSender mailSender = new MailSender("JDSALing@126.com", "JDSALing@126.com", "HJOHDUYCGDQOZYMH", "smtp.126.com");

	//TODO Sever
	static File file = new File("C:\\spd-server-ling\\server\\data\\config.json");

	@Override
	public int onPrivateMessage(Bot bot, PrivateMessageEvent event) {
		String message = event.getRawMessage().replace("\n", "");
		//TODO 检查插件
		if ("key查询".equals(message)) {

			String key;
			key = DigestUtils.md5DigestAsHex(("这是一个加盐前缀:QQ号码:" + event.getUserId()).getBytes()).substring(8, 24);

			try {
				//按照SPDJSON作为模板类读取config.json的数据
				ObjectMapper objectMapper = new ObjectMapper();
				SPDJSON spdJSON;
				spdJSON = objectMapper.readValue(file, new TypeReference<SPDJSON>() {
				});

				//遍历寻找是否有相同key
				for (Account account1 : spdJSON.getAccounts()) {
					//检查key
					if (account1.getKey().equals(key)) {
						try {
							mailSender.sendMail(event.getUserId() + "@qq.com", event.getPrivateSender().getNickname() + "，你的SPDNet key已送达！", "你的SPDNet key是:\n" + key);
						} catch (Exception e) {
							e.printStackTrace();
							sendMsg = MsgUtils.builder().text("发送邮件时出现了意外的错误，请您再试一次");
							bot.sendPrivateMsg(event.getUserId(), sendMsg.build(), false);
							return MESSAGE_BLOCK;
						}
						sendMsg = MsgUtils.builder().text(String.format("你的key是: " + "\n%s", key));
						bot.sendPrivateMsg(event.getUserId(), sendMsg.build(), false);
						return MESSAGE_IGNORE;
					}
				}
				sendMsg = MsgUtils.builder().text("你好像还没注册");

			} catch (Exception e) {
				e.printStackTrace();
				sendMsg = MsgUtils.builder().text("你妈,出BUG了,快去控制台看看日志");
				bot.sendPrivateMsg(event.getUserId(), sendMsg.build(), false);
				return MESSAGE_BLOCK;
			}
			bot.sendPrivateMsg(event.getUserId(), sendMsg.build(), false);
		}
		return MESSAGE_IGNORE;
	}

	@Override
	public int onGroupMessage(Bot bot, GroupMessageEvent event) {
		String message = event.getRawMessage().replace("\n", "");
		//TODO 检查插件
		if ("key查询".equals(message)) {

			String key;
			key = DigestUtils.md5DigestAsHex(("这是一个加盐前缀:QQ号码:" + event.getUserId()).getBytes()).substring(8, 24);

			try {
				//按照SPDJSON作为模板类读取config.json的数据
				ObjectMapper objectMapper = new ObjectMapper();
				SPDJSON spdJSON;
				spdJSON = objectMapper.readValue(file, new TypeReference<SPDJSON>() {
				});

				//遍历寻找是否有相同key
				for (Account account1 : spdJSON.getAccounts()) {
					//检查key
					if (account1.getKey().equals(key)) {
						try {
							mailSender.sendMail(event.getUserId() + "@qq.com", event.getSender().getNickname() + "，你的SPDNet key已送达！", "你的SPDNet key是:\n" + key);
						} catch (Exception e) {
							e.printStackTrace();
							sendMsg = MsgUtils.builder().text("发送邮件时出现了意外的错误，请您再试一次");
							bot.sendPrivateMsg(event.getUserId(), sendMsg.build(), false);
							return MESSAGE_BLOCK;
						}
						sendMsg = MsgUtils.builder().at(event.getUserId()).text(String.format("你的key已经发送到你的QQ邮箱，请注意查收。如果您看不见邮件，很有可能在垃圾箱中。"));
						bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
						return MESSAGE_IGNORE;
					}
				}
				sendMsg = MsgUtils.builder().at(event.getUserId()).text("你好像还没注册");

			} catch (Exception e) {
				e.printStackTrace();
				sendMsg = MsgUtils.builder().text("你妈,出BUG了,快去控制台看看日志");
				bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
				return MESSAGE_BLOCK;
			}
			bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
		}
		return MESSAGE_IGNORE;
	}
}
