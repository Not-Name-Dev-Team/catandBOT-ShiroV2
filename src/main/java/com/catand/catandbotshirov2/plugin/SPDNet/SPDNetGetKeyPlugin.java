package com.catand.catandbotshirov2.plugin.SPDNet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikuac.shiro.annotation.AnyMessageHandler;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.File;

@Shiro
@Component
public class SPDNetGetKeyPlugin{
	MsgUtils sendMsg;
	static MailSender mailSender = new MailSender("JDSALing@126.com", "JDSALing@126.com", "HJOHDUYCGDQOZYMH", "smtp.126.com");

	//TODO Sever
	static File file = new File("C:\\spd-server-ling\\server\\data\\config.json");

	@AnyMessageHandler
	public void onGroupMessage(Bot bot, AnyMessageEvent event) {
		String message = event.getRawMessage().replace("\n", "");
		if ("key查询".equals(message)) {

			String key;
			key = DigestUtils.md5DigestAsHex(("这是一个加盐前缀:QQ号码:" + event.getUserId()).getBytes()).substring(8, 24);

			try {
				//按照SPDJSON作为模板类读取config.json的数据
				ObjectMapper objectMapper = new ObjectMapper();
				SPDJSON spdJSON;
				spdJSON = objectMapper.readValue(file, new TypeReference<>() {});

				//遍历寻找是否有相同key
				for (Account account1 : spdJSON.getAccounts()) {
					//检查key
					if (account1.getKey().equals(key)) {
						try {
							mailSender.sendMail(event.getUserId() + "@qq.com", event.getSender().getNickname() + "，你的SPDNet key已送达！", "你的SPDNet key是:\n" + key);
						} catch (Exception e) {
							e.printStackTrace();
							sendMsg = MsgUtils.builder().text("发送邮件时出现了意外的错误，请您再试一次");
							bot.sendMsg(event, sendMsg.build(), false);
							return;
						}
						sendMsg = MsgUtils.builder().at(event.getUserId()).text("你的key已经发送到你的QQ邮箱，请注意查收。如果您看不见邮件，很有可能在垃圾箱中。");
						bot.sendMsg(event, sendMsg.build(), false);
						return;
					}
				}
				sendMsg = MsgUtils.builder().at(event.getUserId()).text("你好像还没注册");

			} catch (Exception e) {
				e.printStackTrace();
				sendMsg = MsgUtils.builder().text("你妈,出BUG了,快去控制台看看日志");
				bot.sendMsg(event, sendMsg.build(), false);
				return;
			}
			bot.sendMsg(event, sendMsg.build(), false);
		}
	}
}
