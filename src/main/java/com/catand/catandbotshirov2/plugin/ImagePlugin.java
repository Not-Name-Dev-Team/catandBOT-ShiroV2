package com.catand.catandbotshirov2.plugin;

import com.mikuac.shiro.bo.ArrayMsg;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImagePlugin extends BotPlugin {
	MsgUtils sendMsg;

	@Override
	public int onGroupMessage(Bot bot, GroupMessageEvent event) {
		String rawMsg = event.getRawMessage();
		if (rawMsg.startsWith("头像")) {
			List<ArrayMsg> messageChain = (event.getArrayMsg());
			if (messageChain.size() > 1 && "at".equals(messageChain.get(1).getType())) {
				ArrayMsg message = messageChain.get(1);
				if ("at".equals(message.getType())) {
					sendMsg = MsgUtils.builder().img("https://q2.qlogo.cn/headimg_dl?dst_uin=" + message.getData().get("qq") + "&spec=5");
					bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
				} else {
					sendMsg = MsgUtils.builder().img("https://q2.qlogo.cn/headimg_dl?dst_uin=" + rawMsg.substring(2) + "&spec=5");
					bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
				}
			} else {
				sendMsg = MsgUtils.builder().img("https://q2.qlogo.cn/headimg_dl?dst_uin=" + rawMsg.substring(2) + "&spec=5");
				bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
			}
		}
		return MESSAGE_IGNORE;
	}
}
