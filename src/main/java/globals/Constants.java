package globals;

/*
 * This class holds all properties keys as constants
 */
public class Constants {
	Configs conf = Configs.getInstance();
	public static final String CONFIGS_PATH = "src/main/resources/config.properties";

	/* Server Connection */
	public static final String PORT = "port";
	
	/* Game */
	public static final String HAND_NUM_CARDS 		= "numOfCardsInHand";
	public static final String INVALID_CARD_ID 		= "invalidCardId";
	public static final String MIN_PLAYERS 			= "minimumPlayers";
	public static final int    ALL_PLAYERS_JOINED 	= 0;
	
	public static final int NO_PICKED_CARDS 		= 0;
	public static final int SINGLE_PICKED_CARD 		= 1;
	public static final int COUPLE_PICKED_CARDS 	= 2;
	public static final int NO_CARDS_IN_DECK = 0;
	
	/* Card */
	public static final Boolean CARD_END_TURN = true;
	public static final Boolean CARD_DONT_END_TURN = false;
	
	public static final String TYPE_PIT = "pit";
	public static final String TYPE_CLIFF = "cliff";
	public static final String TYPE_AVALANCHE = "avalanche";
	public static final String TYPE_RIVER = "river";
	public static final String TYPE_ROCK = "rock";
	public static final String TYPE_TREE = "tree";
	public static final String TYPE_FLYING = "flying";
	public static final String TYPE_DOG = "dog";
	public static final String TYPE_STICK = "stick";
	public static final String TYPE_HUSBAND = "husband";
	public static final String TYPE_WIFE = "wife";
	public static final String TYPE_BOMB = "bomb";
	public static final String TYPE_SUPER = "super";
	public static final String TYPE_REGULAR = "regular";
	public static final String TYPE_STEAL = "steal";
	public static final String NAME_STEAL = "Steal Attack";
	
	public static final int TXT_COLOR_BLACK = 0;
	public static final int TXT_COLOR_WHITE = 1;
	public static final int TXT_COLOR_DARK = 2;

	/* Card States */
	public static final Boolean ATTACK_SUCCEEDED = true;
	public static final Boolean ATTACK_FAILED = false;
	
	public static final int CS_STATE_NO = 0;
	public static final int CS_STATE_PRE = 1;
	public static final int CS_STATE_DO = 2;
	public static final int CS_STATE_FIN = 3;
	public static final int CS_STATE_SUC = 4;
	public static final int CS_STATE_FAIL = 5;
	public static final int CS_STATE_NUM = 6;
	
	/* Card messages */

	public static final int DEST_VICTIM = 0;
	public static final int DEST_ATTACKER = 1;
	public static final int DEST_ALL = 2;
	public static final int DEST_NUM = 3;
	
	public static final int MSG_ADD_VIC = 0;
	public static final int MSG_ADD_ATT = 1;
	public static final int MSG_ADD_NULL = 2;
	public static final int MSG_ADD_CARD = 3;
	
	public static final int MSG_TYPE_LIST = 0;
	public static final int MSG_TYPE_SCREEN = 1;
	public static final int MSG_TYPE_TITLE = 2;
	public static final int MSG_TYPE_IMAGES = 3;
	
}
