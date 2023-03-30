package com.catand.catandbotshirov2.plugin;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.bo.ArrayMsg;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.enums.MsgTypeEnum;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;

@Shiro
@Component
public class CatandPlugin {
	MsgUtils sendMsg;

	@GroupMessageHandler(cmd = "^(说 |戳|扔骰子|贴贴|涩涩|片片)\s?(.*)?$")
	public void onGroupMessage(Bot bot, GroupMessageEvent event, Matcher matcher) {
		String messageRaw = event.getRawMessage();
		String action = matcher.group();
		switch (action) {
			case "说 ":
				sendMsg = MsgUtils.builder().tts(messageRaw.substring(2));
				bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
				break;
			case "戳":
				List<ArrayMsg> messageChain = (event.getArrayMsg());
				if (messageChain.size() > 1 && MsgTypeEnum.at.equals(messageChain.get(1).getType())) {
					ArrayMsg message1 = messageChain.get(1);
					if (MsgTypeEnum.at.equals(message1.getType())) {
						sendMsg = MsgUtils.builder().poke(Long.parseLong(message1.getData().get("qq")));
						bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
					}
				} else {
					sendMsg = MsgUtils.builder().poke(event.getUserId());
					bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
				}
				break;
			case "扔骰子":
				Random random = new Random();
				Calendar calendar = Calendar.getInstance();
				sendMsg = MsgUtils.builder().at(event.getUserId()).text("扔出了" + (random.nextInt(6) + 1) + "(" + calendar.get(Calendar.MINUTE) + "分" + calendar.get(Calendar.SECOND) + "秒");
				bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
				break;
			case "贴贴":
				sendMsg = MsgUtils.builder().at(event.getUserId()).text("贴贴").img("https://gchat.qpic.cn/gchatpic_new/0/0-0-F0F7B1AE168B0FDA40E9A27362C9462C/0?term=0");
				bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
				break;
			case "涩涩":
				sendMsg = MsgUtils.builder().at(event.getUserId()).text("好哦,走,进屋").img("https://gchat.qpic.cn/gchatpic_new/0/0-0-611D8F347FCA9982F3C913B9F5646FE1/0?term=0");
				bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
				break;
			case "片片":
				sendMsg = MsgUtils.builder().video("https://vdse.bdstatic.com//192d9a98d782d9c74c96f09db9378d93.mp4", "https://gchat.qpic.cn/gchatpic_new/0/0-0-F0F7B1AE168B0FDA40E9A27362C9462C/0?term=0");
				bot.sendGroupMsg(event.getGroupId(), sendMsg.build(), false);
				break;
		}
	}
}
