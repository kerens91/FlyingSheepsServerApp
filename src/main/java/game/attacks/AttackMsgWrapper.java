package game.attacks;

import java.util.ArrayList;
import java.util.List;

import clientservershared.AttackMsg;

/**
* This class represents the attack message wrapper, that defines the structure of an attack message.
* 
* Each attack message is generated by the attack message generator, and transfered to the sockets handler
* in order to send the message to the relevant clients.
* The wrapper contains the attack message itself, along with the relevant client that are the receivers of the message.
* That way, the sockets manager gets the whole message information needed to send it.
* 
* The class is created by the Game class, and is used by the attack handler and attack resolver.
* 
* @author      Keren Solomon
*/
public class AttackMsgWrapper {
	private final List<String> destinations;
	private final AttackMsg msg;
	
	/**
	 * Creates an attack message wrapper to define the attack message information that is used by the socket handler.
	 * The AttackMsgWrapper class is created with a list of destinations (receivers of the message) and the message itself.
	 * 
	 * It is created specific for each attack message that needs to be sent to a list of clients.
	 * 
	 * @param destinations    	represents the list of receivers.
	 * @param msg    			represents the message to be sent.
	 */
	public AttackMsgWrapper(List<String> destinations, AttackMsg msg) {
		this.destinations = destinations;
		this.msg = msg;
	}
	
	/**
	 * Creates an attack message wrapper to define the attack message information that is used by the socket handler.
	 * The AttackMsgWrapper class is created with a single destinations (receiver of the message) and the message itself.
	 * 
	 * It is created specific for each attack message that needs to be sent to a single client.
	 * 
	 * @param destination    	represents the receiver.
	 * @param msg    			represents the message to be sent.
	 */
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
