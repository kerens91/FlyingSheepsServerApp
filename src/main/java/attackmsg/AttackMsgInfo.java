package attackmsg;

/*
 * This class defines the attack messages send between client and server
 */
public class AttackMsgInfo {
	private int msgType;
	private int preMsg;
	private String msg;
	private int postMsg;
	
	public AttackMsgInfo(int msgType, int preMsg, String msg, int postMsg) {
		this.msgType = msgType;
		this.preMsg = preMsg;
		this.msg = msg;
		this.postMsg = postMsg;
	}

	public int getMsgType() {
		return msgType;
	}

	public int getPreMsg() {
		return preMsg;
	}

	public String getMsg() {
		return msg;
	}

	public int getPostMsg() {
		return postMsg;
	}

	
}
