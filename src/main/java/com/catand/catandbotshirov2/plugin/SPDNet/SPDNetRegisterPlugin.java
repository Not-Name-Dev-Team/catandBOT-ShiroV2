package com.catand.catandbotshirov2.plugin.SPDNet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.regex.Matcher;

@Shiro
@Component
public class SPDNetRegisterPlugin {
	MsgUtils sendMsg;
	BufferedWriter bufferedWriter;
	static MailSender mailSender = new MailSender("JDSALing@126.com", "JDSALing@126.com", "HJOHDUYCGDQOZYMH", "smtp.126.com");

	//TODO Sever
	static File file = new File("C:\\spd-server-ling\\server\\data\\config.json");

	//判断是否是中文
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
	}

	@GroupMessageHandler(cmd = "^(.*)?SPD注册\s(.*)?$")
	public void sPDRegister(Bot bot, GroupMessageEvent event, Matcher matcher) {
		String message = event.getRawMessage().replace("\n", "");

		int userIndex = message.indexOf("用户名 ");
		Account account;
		//如果"用户名 "存在
		if (userIndex != -1) {

			//构建待加入Account对象
			String name;
			String key;
			name = message.substring(userIndex + 4);
			key = DigestUtils.md5DigestAsHex(("这是一个加盐前缀:QQ号码:" + event.getUserId()).getBytes()).substring(8, 24);
			account = new Account(false, false, key, name, false);

			if (name.contains("[CQ:")) {
				sendMsg = MsgUtils.builder().at(event.getUserId()).text("\n不许用QQ表情,爬!");
				bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
				return;
			}

			try {

				//按照SPDJSON作为模板类读取config.json的数据
				ObjectMapper objectMapper = new ObjectMapper();
				SPDJSON spdJSON;
				spdJSON = objectMapper.readValue(file, new TypeReference<>() {
				});

				//遍历寻找是否有相同用户名或者相同key
				for (Account account1 : spdJSON.getAccounts()) {
					//检查用户名
					if (account1.getNick().equals(name)) {
						sendMsg = MsgUtils.builder().at(event.getUserId()).text("\n有人已经用了这个好名字了\n换一个⑧");
						bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
						return;
					}

					//检查key
					else if (account1.getKey().equals(key)) {
						sendMsg = MsgUtils.builder().at(event.getUserId()).text("\n想同QQ注册多个是吧，不许！");
						bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
						return;
					}
				}

				//修改spdJSON并写回
				spdJSON.getAccounts().add(account);
				//TODO 链接常驻
				bufferedWriter = new BufferedWriter(new FileWriter(file, false));
				bufferedWriter.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(spdJSON));
				bufferedWriter.flush();
				bufferedWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
				sendMsg = MsgUtils.builder().text("你妈,SPD注册功能出BUG了,快去控制台看看日志");
				bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
				return;
			}

			try {
				mailSender.sendMail(event.getUserId() + "@qq.com", event.getSender().getNickname() + "，你的SPDNet key已送达！如果没有请到垃圾箱查看。", "你的SPDNet key是:\n" + key);
			} catch (Exception e) {
				e.printStackTrace();
				sendMsg = MsgUtils.builder().text("发送邮件时出现了意外的错误，请您再试一次");
				bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
				return;
			}
			sendMsg = MsgUtils.builder().at(event.getUserId()).text("创建成功!\n用户名:" + name + "\n你的key已经发送到你的QQ邮箱。如果没有请到垃圾箱查看。");
		} else {
			sendMsg = MsgUtils.builder().at(event.getUserId()).text("语法无效!\n格式:\"SPD注册 用户名 XXX\"");
		}
		bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
	}

	@GroupMessageHandler(cmd = "^(.*)?以后叫我\s(.*)?$")
	public void rename(Bot bot, GroupMessageEvent event, Matcher matcher) {
		String message = event.getRawMessage().replace("\n", "");

		int userIndex = message.indexOf("以后叫我 ");
		Account account;
		//如果"以后叫我"存在
		if (userIndex != -1) {

			//构建待加入Account对象
			String name;
			String key;
			name = message.substring(userIndex + 5);
			key = DigestUtils.md5DigestAsHex(("这是一个加盐前缀:QQ号码:" + event.getUserId()).getBytes()).substring(8, 24);
			account = new Account(false, false, key, name, false);

			if (name.contains("[CQ:")) {
				sendMsg = MsgUtils.builder().at(event.getUserId()).text("\n不许用QQ表情,爬!");
				bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
				return;
			}

			try {
				//按照SPDJSON作为模板类读取config.json的数据
				ObjectMapper objectMapper = new ObjectMapper();
				SPDJSON spdJSON;
				spdJSON = objectMapper.readValue(file, new TypeReference<>() {
				});

				//遍历寻找是否有相同用户名或者相同key
				for (Account account1 : spdJSON.getAccounts()) {
					//检查用户名
					if (account1.getNick().equals(name)) {
						if (account1.getKey().equals(key)) {
							sendMsg = MsgUtils.builder().at(event.getUserId()).text("\n我看看啊-- " + name + "? 你这不是什么都没改吗");
						} else {
							sendMsg = MsgUtils.builder().at(event.getUserId()).text("\n有人已经用了这个好名字了\n换一个⑧");
						}
						bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
						return;
					}

					//检查key
					else if (account1.getKey().equals(key)) {
						if (spdJSON.getAccounts().remove(account1)) {
							sendMsg = MsgUtils.builder().at(event.getUserId()).text("\n好好好, " + name);
						} else {
							throw new RuntimeException("从JSON删除account不成功");
						}
					}
				}

				//修改spdJSON并写回
				spdJSON.getAccounts().add(account);
				//TODO 链接常驻
				bufferedWriter = new BufferedWriter(new FileWriter(file, false));
				bufferedWriter.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(spdJSON));
				bufferedWriter.flush();
				bufferedWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
				sendMsg = MsgUtils.builder().text("你妈,SPD注册功能出BUG了,快去控制台看看日志");
				bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
				return;
			}
			sendMsg = MsgUtils.builder().at(event.getUserId()).text("创建成功!\n用户名:" + name + "\n你的key已经发送到你的QQ邮箱。如果没有请到垃圾箱查看。");
		} else {
			sendMsg = MsgUtils.builder().at(event.getUserId()).text("语法无效!\n格式:\"以后叫我 XXX\"");
		}
		bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
	}
}
