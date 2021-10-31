package clientservershared;

import java.util.ArrayList;

public class AttackMsg {
	private int msgType;
	private String msgString;
	private ArrayList<String> list;
	private String mainImg;
	private String secImg;
	
	public AttackMsg(int msgType) {
		this.msgType = msgType;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getMsgString() {
		return msgString;
	}

	public void setMsgString(String msgString) {
		this.msgString = msgString;
	}

	public ArrayList<String> getList() {
		return list;
	}

	public void setList(ArrayList<String> list) {
		this.list = list;
	}

	public String getMainImg() {
		return mainImg;
	}

	public void setMainImg(String mainImg) {
		this.mainImg = mainImg;
	}

	public String getSecImg() {
		return secImg;
	}

	public void setSecImg(String secImg) {
		this.secImg = secImg;
	}
	
}
