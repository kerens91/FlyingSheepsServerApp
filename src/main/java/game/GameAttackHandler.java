package game;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import attackmsg.AttackMsgInfo;
import attackstate.GameAttackState;
import card.AbstractCard;
import clientservershared.AttackMsg;
import eventnotifications.IAttackNotifications;
import game.player.Player;
import globals.Constants;

public class GameAttackHandler {
	private static final Logger logger = LogManager.getLogger(GameAttackHandler.class);
	private GameAttackState attackState;
	
	private static GameAttackHandler gameAttackHandlerInstance;
	
	private GameAttackHandler() {
		attackState = new GameAttackState();
	}
	
	public static GameAttackHandler getInstance() {
		if (gameAttackHandlerInstance == null) {
			gameAttackHandlerInstance = new GameAttackHandler();
		}
		return gameAttackHandlerInstance;
	}
	
	public void registerCallback (IAttackNotifications notificationsCallback) {
    	attackState.registerNotifications(notificationsCallback);
    }
	
	public void nextAttackState() {
    	attackState.getState().leave();
    }
	
	public void executeState() {
    	attackState.getState().execute();
    }
    
    public void finishAttackSuc() {
    	logger.info("setting attack result succeeded");
    	attackState.setAttackResult(Constants.ATTACK_SUCCEEDED);
    	executeState();
    }
    
    public void finishAttackFail() {
    	logger.info("setting attack result failed");
    	attackState.setAttackResult(Constants.ATTACK_FAILED);
    	executeState();
    }
    
    public Player getPlayerVictim() {
    	return attackState.getState().getVictim();
    }
    
    public Player getPlayerAttacker() {
    	return attackState.getState().getAttacker();
    }
    
    public String getAttacker() {
    	return attackState.getAttacker().getId();
    }
 
    public String getVictim() {
    	return attackState.getVictim().getId();
    }
    
    public void setAttackCard(AbstractCard card) {
    	attackState.setAttackCard(card);
    }
    
    public void setHelperCard(AbstractCard card) {
    	attackState.setHelperCard(card);
    }
    
    public void setVictim(Player victim) {
    	attackState.setVictim(victim);
    }
    
    public void setAttacker(Player attacker) {
    	attackState.setAttacker(attacker);
    }
    
    public Boolean isPlayerVictim(Player player) {
    	if (attackState.getVictim().getId().equals(player.getId())) {
			return true;
		}
    	return false;
    }
    
    public Boolean isAttackActive() {
    	return attackState.isAttackActive();
    }
    
    public Boolean isVictimDefended() {
    	return attackState.victimDefended();
    }
    
    private AttackMsg getMsgTwoImg(int destination, String mainImg, String secImg) {
    	AttackMsgInfo info = attackState.getState().getMsgInfo(destination);
    	AttackMsg msg = new AttackMsg(info.getMsgType());
    	msg.setMsgString(generateMsg(info));
    	msg.setMainImg(mainImg);
    	msg.setSecImg(secImg);
    	return msg;
    }
    
    private AttackMsg getMsgTitleImg(int destination, String img) {
    	System.out.println("state = " + attackState.getState().getStateId());
    	AttackMsgInfo info = attackState.getState().getMsgInfo(destination);
    	AttackMsg msg = new AttackMsg(info.getMsgType());
    	msg.setMsgString(generateMsg(info));
    	msg.setMainImg(img);
    	return msg;
    }
    
    public AttackMsg getMsgList(int destination, List<String> list) {
    	System.out.println("state = " + attackState.getState().getStateId());
    	AttackMsgInfo info = attackState.getState().getMsgInfo(destination);
    	AttackMsg msg = new AttackMsg(info.getMsgType());
    	msg.setMsgString(generateMsg(info));
    	msg.setList(list);
    	return msg;
    }
    
    private AttackMsg getMsgScreen(int destination, String img) {
    	AttackMsgInfo info = attackState.getState().getMsgInfo(destination);
    	AttackMsg msg = new AttackMsg(info.getMsgType());
    	msg.setMsgString(generateMsg(info));
    	msg.setMainImg(img);
    	return msg;
    }
    
    private String generatePreMsg(int preMsg) {
    	String pre = null;
    	System.out.println("pre = " + preMsg);
    	if (preMsg == Constants.MSG_ADD_ATT) {
    		pre = attackState.getAttacker().getName();
    	}
    	else if (preMsg == Constants.MSG_ADD_VIC) {
    		pre = attackState.getVictim().getName();
    	}
    	else if (preMsg == Constants.MSG_ADD_CARD) {
    		pre = attackState.getAttackCard().getName();
    	}
    	else {
    		pre = "";
    	}
    	return pre;
    }
    
    private String generatePostMsg(int postMsg) {
    	String post = null;
    	System.out.println("post = " + postMsg);
    	if (postMsg == Constants.MSG_ADD_ATT) {
    		post = attackState.getAttacker().getName();
    	}
    	else if (postMsg == Constants.MSG_ADD_VIC) {
    		post = attackState.getVictim().getName();
    	}
    	else if (postMsg == Constants.MSG_ADD_CARD) {
    		post = attackState.getHelperCard().getName();
    	}
    	else {
    		post = "";
    	}
    	return post;
    }
    
    private String generateMsg(AttackMsgInfo info) {
    	StringBuilder builder = new StringBuilder();
    	System.out.println("generating msg for " + info.getMsg());
    	
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
    public AttackMsg preAttackGetMsg(List<String> list) {
    	return getMsgList(Constants.DEST_ATTACKER, list);
    }
    
    /*
     * This func is called on rock attack succeeded state
     * created the Type.twoImg type message to send to the victim
     */
    public AttackMsg rockAttSucGetVictimMsg() {
    	return getMsgTwoImg(Constants.DEST_VICTIM, attackState.getAttackCard().getImg(), attackState.getHelperCard().getImg());
    }
    
    /*
     * This func is called on rock attack succeeded state
     * created the Type.titleImg type message to send to all players
     */
    public AttackMsg rockAttSucGetAllPlayersMsg() {
    	return getMsgTitleImg(Constants.DEST_ALL, attackState.getAttackCard().getImg());
    }
    
    /*
     * This func is called on steal attack do state
     * to inform the victim that the attack starts
     * created the Type.onScreen type message
     */
    public AttackMsg notifyAttackGetVictimMsg() {
    	return getMsgScreen(Constants.DEST_VICTIM, attackState.getAttackCard().getImg());
    }

    /*
     * This func is called on steal attack do state
     * to inform all players with the attack
     * created the Type.titleImg type message
     */
    public AttackMsg notifyAttackGetAllPlayersMsg() {
    	return getMsgTitleImg(Constants.DEST_ALL, attackState.getAttackCard().getImg());
    }
    
    /*
     * This func is called on steal attack success state
     * to inform victim with the stolen card
     * created the Type.twoImg type message
     */
    public AttackMsg stealAttSucGetVictimMsg() {
    	return getMsgTwoImg(Constants.DEST_VICTIM, attackState.getAttackCard().getImg(), attackState.getHelperCard().getImg());
    }

    /*
     * This func is called on steal attack success state
     * to inform attacker with his new card
     * created the Type.twoImg type message
     */
    public AttackMsg stealAttSucGetAttMsg() {
    	return getMsgTwoImg(Constants.DEST_ATTACKER, attackState.getAttackCard().getImg(), attackState.getHelperCard().getImg());
    }
    
    /*
     * This func is called on steal attack success state
     * to inform all players with attack result
     * created the Type.titleImg type message
     */
    public AttackMsg stealAttSucGetAllPlayersMsg() {
    	return getMsgTitleImg(Constants.DEST_ALL, attackState.getAttackCard().getImg());
    }
    
    /*
     * This func is called on steal attack failed state
     * to inform all players with attack result
     * created the Type.twoImg type message
     */
    public AttackMsg stealAttFailedGetAllPlayersMsg() {
    	return getMsgTwoImg(Constants.DEST_ALL, attackState.getHelperCard().getImg(), attackState.getAttackCard().getImg());
    }

    /*
     * This func is called on river attack success state
     * to inform all players with attack result
     * created the Type.titleImg type message
     */
    public AttackMsg riverAttSucGetAllPlayersMsg() {
    	return getMsgTitleImg(Constants.DEST_ALL, attackState.getAttackCard().getImg());
    }
    
    /*
     * This func is called on tree attack success/fail state
     * to inform all players with attack
     * created the Type.titleImg type message
     */
    public AttackMsg treeAttGetAllPlayersMsg() {
    	return getMsgTitleImg(Constants.DEST_ALL, attackState.getAttackCard().getImg());
    }
    
    /*
     * This func is called on tree attack success state
     * to inform attacker with owners list
     * created the Type.list type message
     */
    public AttackMsg treeAttSucGetAttMsg(List<String> list) {
    	return getMsgList(Constants.DEST_ATTACKER, list);
    }
    
    /*
     * This func is called on tree attack fail state
     * to inform attacker that there are no owners
     * created the Type.titleImg type message
     */
    public AttackMsg treeAttFailGetAttMsg() {
    	return getMsgTitleImg(Constants.DEST_ATTACKER, attackState.getAttackCard().getImg());
    }
    
    /*
     * This func is called on nature disaster attack succeeded state
     * to inform all that player lost game
     * created the Type.titleImg type message
     */
    public AttackMsg natureAttSucGetAllPlayersMsg() {
    	return getMsgTitleImg(Constants.DEST_ALL, attackState.getAttackCard().getImg());
    }
    
    /*
     * This func is called on nature disaster attack failed state
     * to inform all that player defended himself
     * created the Type.titleImg type message
     */
    public AttackMsg natureAttFailGetAllPlayersMsg() {
    	return getMsgTitleImg(Constants.DEST_ALL, attackState.getAttackCard().getImg());
    }
    

    
}
