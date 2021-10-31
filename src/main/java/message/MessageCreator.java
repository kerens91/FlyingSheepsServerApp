package message;

import clientservershared.AttackMsg;
import clientservershared.CardModel;
import clientservershared.GameInfo;
import clientservershared.GameOver;

public class MessageCreator {
	private MessageConvertor convertor;
	
	public MessageCreator() {
		convertor = new MessageConvertor();
	}

	/* make sure you get all objects as primitives for the json class */
	public String createJsonMsg(MsgTypeEnum type, Object... objects) {
    	Message msg = new Message(type);
    		for (Object param : objects) {
    			msg.addParam(param);
    		}
        return convertor.msgToJson(msg);
	}
	
	public String createMsg(MsgTypeEnum type) {
		return createJsonMsg(type, "");
	}
	
	public String createMsg(MsgTypeEnum type, int param) {
		return createJsonMsg(type, param);
	}
	
	public String createMsg(MsgTypeEnum type, String param) {
		return createJsonMsg(type, param);
	}
	
	public String createMsg(MsgTypeEnum type, Object... params) {
		return createJsonMsg(type, params);
	}
	
	public String createMsg(MsgTypeEnum type, GameInfo param) {
		String jsonInfo = convertor.gameInfoToJson(param);
		return createJsonMsg(type, jsonInfo);
	}
	
	public String createMsg(MsgTypeEnum type, GameOver param) {
		String jsonInfo = convertor.gameOverToJson(param);
		return createJsonMsg(type, jsonInfo);
	}
	
	public String createMsg(MsgTypeEnum type, CardModel param) {
		String jsonInfo = convertor.cardToJson(param);
		return createJsonMsg(type, jsonInfo);
	}
	
	public String createMsg(MsgTypeEnum type, AttackMsg param) {
		String jsonInfo = convertor.attackMsgToJson(param);
		return createJsonMsg(type, jsonInfo);
	}
	
}
