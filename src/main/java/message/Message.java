package message;

import java.util.ArrayList;

public class Message {
	private MsgTypeEnum msgType;
	private ArrayList<Object> params;
	
	public Message() {
		this.msgType = MsgTypeEnum.UNKNOWN_TYPE;
		this.params = new ArrayList<>();
	}
	
	public Message(MsgTypeEnum msgType) {
		this.msgType = msgType;
		this.params = new ArrayList<>();
	}
	
	public MsgTypeEnum getMsgType() {
		return this.msgType;
	}
	
	public ArrayList<Object> getMsgParams() {
		return this.params;
	}
	
	public void addParam(Object param) {
        params.add(param);
    }

}
