package game.attacks;

import java.util.ArrayList;
import java.util.List;

import clientservershared.AttackMsg;

public class AttackMsgWrapper {
	private final List<String> destinations;
	private final AttackMsg msg;
	
	public AttackMsgWrapper(List<String> destinations, AttackMsg msg) {
		this.destinations = destinations;
		this.msg = msg;
	}
	
	public AttackMsgWrapper(String destination, AttackMsg msg) {
		this.destinations = new ArrayList<>();
		this.destinations.add(destination);
		this.msg = msg;
	}
	

	public List<String> getDestinations() {
		return destinations;
	}

	public AttackMsg getMsg() {
		return msg;
	}

	
	
}
