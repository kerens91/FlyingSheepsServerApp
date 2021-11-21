package game.attacks;

import static globals.Constants.*;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import attackmsg.AttackMsgInfo;
import attackstate.GameAttackState;
import clientservershared.AttackMsg;

/**
* This class represents the attack message generator, that is responsible for generating the attack messages.
* 
* The class generates four types of attack messages:
* <ul>
* <li>TwoImg - includes two images. presented as a pop up.
* <li>TitleImg - includes a title string and an image. presented as a pop up.
* <li>List - includes a list. presented as a pop up.
* <li>Screen - includes a title string and an image. presented on client's screen.
* </ul>
* The messages are generated based on the type of the attack, the state and the receiver of the message.
* All attack messages are AttackMsg objects.
* 
* The class is created by the Game class, and is used by the attack resolver and the attack handler.
* 
* @author      Keren Solomon
*/
public class AttackMsgGenerator {
	private static final Logger logger = LogManager.getLogger(AttackMsgGenerator.class);
	private GameAttackState attackState;
	
	/**
	 * Creates an attack message generator to handle the creation of the attack messages.
	 * The AttackMsgGenerator class is created with an attack state member.
	 * 
	 * It is created by the Game class.
	 * 
	 * @param attackState    	represents the GameAttackState that handles the states of the attack.
	 */
	public AttackMsgGenerator(GameAttackState attackState) {
		this.attackState = attackState;
	}
	
	private AttackMsg generateMsgTwoImg(int destination, String mainImg, String secImg) {
    	AttackMsgInfo info = attackState.getState().getMsgInfo(destination);
    	AttackMsg msg = new AttackMsg(info.getMsgType());
    	msg.setMsgString(generateMsg(info));
    	msg.setMainImg(mainImg);
    	msg.setSecImg(secImg);
    	return msg;
    }
    
    private AttackMsg generateMsgTitleImg(int destination, String img) {
    	AttackMsgInfo info = attackState.getState().getMsgInfo(destination);
    	AttackMsg msg = new AttackMsg(info.getMsgType());
    	msg.setMsgString(generateMsg(info));
    	msg.setMainImg(img);
    	return msg;
    }
    
    public AttackMsg generateMsgList(int destination, List<String> list) {
    	AttackMsgInfo info = attackState.getState().getMsgInfo(destination);
    	AttackMsg msg = new AttackMsg(info.getMsgType());
    	msg.setMsgString(generateMsg(info));
    	msg.setList(list);
    	return msg;
    }
    
    private AttackMsg generateMsgScreen(int destination, String img) {
    	AttackMsgInfo info = attackState.getState().getMsgInfo(destination);
    	AttackMsg msg = new AttackMsg(info.getMsgType());
    	msg.setMsgString(generateMsg(info));
    	msg.setMainImg(img);
    	return msg;
    }
    
    private String generatePreMsg(int preMsgType) {
    	logger.info("pre = " + preMsgType);
    	String pre = null;
    	
    	switch (preMsgType) {
		case MSG_ADD_ATT:
			pre = attackState.getAttacker().getName();
			break;
		case MSG_ADD_VIC:
			pre = attackState.getVictim().getName();
			break;
		case MSG_ADD_CARD:
			pre = attackState.getAttackCard().getName();
			break;
		default:
			pre = "";
			break;
		}
    	
    	return pre;
    }
    
    private String generatePostMsg(int postMsgType) {
    	logger.info("post = " + postMsgType);
    	String post = null;
    	
    	switch (postMsgType) {
		case MSG_ADD_ATT:
			post = attackState.getAttacker().getName();
			break;
		case MSG_ADD_VIC:
			post = attackState.getVictim().getName();
			break;
		case MSG_ADD_CARD:
			post = attackState.getHelperCard().getName();
			break;
		default:
			post = "";
			break;
		}
    	
    	return post;
    }
    
    private String generateMsg(AttackMsgInfo info) {
    	StringBuilder builder = new StringBuilder();
    	logger.info("generating msg for " + info.getMsg());
    	
    	builder.append(generatePreMsg(info.getPreMsg()));
    	builder.append(info.getMsg());
    	builder.append(generatePostMsg(info.getPostMsg()));
    	
    	return builder.toString();
    }
    
    /*
     * This func is called on steal/rock attack pre-attack state
     * to ask the attacker who is the victim
     * created the Type.list type message
     */
    public AttackMsg getMsgPreAttack(List<String> list) {
    	return generateMsgList(DEST_ATTACKER, list);
    }
    
    /*
     * This func is called on rock attack succeeded state
     * created the Type.twoImg type message to send to the victim
     */
    public AttackMsg getMsgVicRockAttSuc() {
    	return generateMsgTwoImg(DEST_VICTIM, attackState.getAttackCard().getImg(), attackState.getHelperCard().getImg());
    }
    
    /*
     * This func is called on rock attack succeeded state
     * created the Type.titleImg type message to send to all players
     */
    public AttackMsg getMsgAllRockAttSuc() {
    	return generateMsgTitleImg(DEST_ALL, attackState.getAttackCard().getImg());
    }
    
    /*
     * This func is called on steal attack do state
     * to inform the victim that the attack starts
     * created the Type.onScreen type message
     */
    public AttackMsg getMsgVicAttNotify() {
    	return generateMsgScreen(DEST_VICTIM, attackState.getAttackCard().getImg());
    }

    /*
     * This func is called on steal attack do state
     * to inform all players with the attack
     * created the Type.titleImg type message
     */
    public AttackMsg getMsgAllAttNotify() {
    	return generateMsgTitleImg(DEST_ALL, attackState.getAttackCard().getImg());
    }
    
    /*
     * This func is called on steal attack success state
     * to inform victim with the stolen card
     * created the Type.twoImg type message
     */
    public AttackMsg getMsgVicStealAttSuc() {
    	return generateMsgTwoImg(DEST_VICTIM, attackState.getAttackCard().getImg(), attackState.getHelperCard().getImg());
    }

    /*
     * This func is called on steal attack success state
     * to inform attacker with his new card
     * created the Type.twoImg type message
     */
    public AttackMsg getMsgAttStealAttSuc() {
    	return generateMsgTwoImg(DEST_ATTACKER, attackState.getAttackCard().getImg(), attackState.getHelperCard().getImg());
    }
    
    /*
     * This func is called on steal attack success state
     * to inform all players with attack result
     * created the Type.titleImg type message
     */
    public AttackMsg getMsgAllStealAttSuc() {
    	return generateMsgTitleImg(DEST_ALL, attackState.getAttackCard().getImg());
    }
    
    /*
     * This func is called on steal attack failed state
     * to inform all players with attack result
     * created the Type.twoImg type message
     */
    public AttackMsg getMsgAllStealAttFailed() {
    	return generateMsgTwoImg(DEST_ALL, attackState.getHelperCard().getImg(), attackState.getAttackCard().getImg());
    }

    /*
     * This func is called on river attack success state
     * to inform all players with attack result
     * created the Type.titleImg type message
     */
    public AttackMsg getMsgAllRiverAttSuc() {
    	return generateMsgTitleImg(DEST_ALL, attackState.getAttackCard().getImg());
    }
    
    /*
     * This func is called on tree attack success/fail state
     * to inform all players with attack
     * created the Type.titleImg type message
     */
    public AttackMsg getMsgAllTreeAtt() {
    	return generateMsgTitleImg(DEST_ALL, attackState.getAttackCard().getImg());
    }
    
    /*
     * This func is called on tree attack success state
     * to inform attacker with owners list
     * created the Type.list type message
     */
    public AttackMsg getMsgTreeAttSuc(List<String> list) {
    	return generateMsgList(DEST_ATTACKER, list);
    }
    
    /*
     * This func is called on tree attack fail state
     * to inform attacker that there are no owners
     * created the Type.titleImg type message
     */
    public AttackMsg getMsgTreeAttFail() {
    	return generateMsgTitleImg(DEST_ATTACKER, attackState.getAttackCard().getImg());
    }
    
    /*
     * This func is called on nature disaster attack succeeded state
     * to inform all that player lost game
     * created the Type.titleImg type message
     */
    public AttackMsg getMsgAllNatureAttSuc() {
    	return generateMsgTitleImg(DEST_ALL, attackState.getAttackCard().getImg());
    }
    
    /*
     * This func is called on nature disaster attack failed state
     * to inform all that player defended himself
     * created the Type.titleImg type message
     */
    public AttackMsg getMsgAllNatureAttFail() {
    	return generateMsgTitleImg(DEST_ALL, attackState.getHelperCard().getImg());
    }
    
}
