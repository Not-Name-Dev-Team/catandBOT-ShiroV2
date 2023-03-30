package com.catand.catandbotshirov2.plugin;

import com.mikuac.shiro.annotation.AnyMessageHandler;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import org.springframework.stereotype.Component;

@Shiro
@Component
public class HelpPlugin {
	MsgUtils sendMsg;

	@AnyMessageHandler(cmd = "^帮助|help$")
	public void sendHelpMsg(Bot bot, AnyMessageEvent event) {
		sendMsg = MsgUtils.builder().text("这是catand的机器人,当前版本：1.0.7\n" +
				"-主要开发者：Catand\n-协同开发者：JDSALing\n" +
				"-基于 Shiro \n-目前所拥有的功能：\n" +
				"==========常规功能==========\n" +
				"文字转语音(tts)\n" +
				"无情的贴贴机器\n" +
				"QQ头像戳一戳\n" +
				"获取指定用户的QQ头像扔骰子\n" +
				"通过@获取指定QQ号(好没用!)\n" +
				"==========SPDNET相关操作==========\n" +
				"SPDNET相关操作(支持中英注册):\n" +
				"-格式:SPD注册 用户名 xxxx\n" +
				"在群聊中发送'key查询',将会发送Key到你的QQ邮箱中\n" +
				"关于邮箱的特别说明：有一些时候可能会有很多人查询，发送可能会有一定的延迟，另外如果查不到邮件可能在你邮件的垃圾箱中。\n" +
				"SPDNET改名功能: 以后叫我 xxxxxx");
		bot.sendMsg(event, sendMsg.build(), false);
	}
}