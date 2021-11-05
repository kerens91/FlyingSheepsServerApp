package clientservershared;

import java.util.ArrayList;
import java.util.List;

public class AttackMsg {
	private int msgType;
	private String msgString;
	private List<String> list;
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

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list2) {
		this.list = list2;
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
