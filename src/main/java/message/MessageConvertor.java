package message;

import java.util.ArrayList;

import com.google.gson.Gson;

import clientservershared.AttackMsg;
import clientservershared.CardModel;
import clientservershared.GameInfo;
import clientservershared.GameOver;
import clientservershared.PickedCards;



public class MessageConvertor {
	private Gson gson;
	
	public MessageConvertor() {
		this.gson = new Gson();
	}
	
	public Message jsonToMsg(String jsonString) {
		return gson.fromJson(jsonString, Message.class);
	}
	
	public String msgToJson(Message msg) {
		return gson.toJson(msg);
	}
	
	public String listToJson(ArrayList list) {
		return gson.toJson(list, ArrayList.class);
	}
	
	public ArrayList<Integer> jsonToList(String jsonString) {
		return gson.fromJson(jsonString, ArrayList.class);
	}
    
	public PickedCards jsonToPickedCards(String jsonString) {
        return gson.fromJson(jsonString, PickedCards.class);
    }

	public String gameInfoToJson(GameInfo gameInfo) {
		return gson.toJson(gameInfo, GameInfo.class);
	}

	public String cardToJson(CardModel card) {
		return gson.toJson(card, CardModel.class);
	}

	public String attackMsgToJson(AttackMsg msg) {
		return gson.toJson(msg, AttackMsg.class);
	}

	public String gameOverToJson(GameOver info) {
		return gson.toJson(info, GameOver.class);
	}
}
