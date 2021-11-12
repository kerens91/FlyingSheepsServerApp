package attackmsg;

/**
* AttackMsgInfo class defines the messages information sent between 
* the client and the server when attack occurs.
* The class is created by the AttackMsgGenerator class, when the 
* server needs to inform clients with attack status and information.
* The attack message will have a type - one of the following:
* <ul>
* <li>MSG_TYPE_LIST
* <li>MSG_TYPE_SCREEN
* <li>MSG_TYPE_TITLE
* <li>MSG_TYPE_IMAGES
* </ul>
* Each of the types defines the visual representation in client side.
* The attack message will also have a message string to display.
* The message is built from pre-message + message + post-message,
* while pre-message and post-message are optional, and can be the
* name of the attacker / victim / card.
* 
* @author      Keren Solomon
*/
public class AttackMsgInfo {
	private final int msgType;
	private final int preMsg;
	private final String msg;
	private final int postMsg;
	
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
