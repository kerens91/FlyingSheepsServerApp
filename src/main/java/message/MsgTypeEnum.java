package message;

/*
	This is the Enum types of messages between the server and the client
	IT IS IMPORTANT THAT THIS CLASS WILL BE CORRESPONDENT TO THE CLIENT CLASS
	keep updating changed in both files.
	will be read from database
*/
public enum MsgTypeEnum {
	
	/* **********************************************************************************
    IN
    ********************************************************************************** */
	
	/*
	 * Type: CLIENT_REQ_NEW_GAME
	 * Param-0: [int] numOfPlayers
	 */
	CLIENT_REQ_NEW_GAME,
	
	/*
	 * Type: CLIENT_REQ_END_GAME
	 */
	CLIENT_REQ_END_GAME,
	
    /*
     * Type: CLIENT_REQ_JOIN_GAME
     * Param-0: [string] password
     * Param-1: [string] name
     * Param-2: [string] img
     */
	CLIENT_REQ_JOIN_GAME,
	
	/*
     * Type: CLIENT_REQ_PICKED_CARDS
     * Param-0: [ArrayList<Integer>] cards
     */
    CLIENT_REQ_PICKED_CARDS,
	
    /*
     * Type: CLIENT_REQ_DEAL_CARD
     */
    CLIENT_REQ_DEAL_CARD,
    
    /*
     * Type: CLIENT_REQ_ATTACK_REP_VICTIM
     * Param-0: [String] playerToStealFrom
     */
    CLIENT_REQ_ATTACK_REP_VICTIM,
    
    /*
     * Type: CLIENT_REQ_LOSE_ATTACK
     */
    CLIENT_REQ_LOSE_ATTACK,
	
	/* **********************************************************************************
    OUT
    ********************************************************************************** */
	
	/*
	 * Type: SERVER_REP_GAME_EXIST
	 */
	SERVER_REP_GAME_EXIST,
	
	/*
	 * Type: SERVER_REP_GAME_PASS
	 * Param-0: [String] password
	 */
	SERVER_REP_GAME_PASS,
	
	/*
	 * Type: SERVER_REP_INVALID_PASS
	 */
	SERVER_REP_INVALID_PASS,
	
	/*
	 * Type: SERVER_REP_GAME_FULL
	 */
	SERVER_REP_GAME_FULL,
	
	/*
	 * Type: SERVER_REP_WAIT_FOR_GAME_TO_START
	 * Param-0: [int] num of active players
	 */
	SERVER_REP_WAIT_FOR_GAME_TO_START,
	
	/*
     * Type: SERVER_REP_SET_NUM_ACTIVE_PLAYERS
     * Param-0: [int] number of active players in game
     */
    SERVER_REP_SET_NUM_ACTIVE_PLAYERS,
    
	/*
	 * Type: SERVER_REP_START_GAME
	 * Param-0: [String] clientId
	 */
	SERVER_REP_START_GAME,
	
	/*
	 * Type: SERVER_REP_GAME_INFO
	 * Param-0: [GameInfo] info
	 */
	SERVER_REP_GAME_INFO,
	
	/*
	 * Type: SERVER_REP_GAME_HAND_REMOVE_CARD
	 * Param-0: [int] cardId
	 */
	SERVER_REP_GAME_HAND_REMOVE_CARD,
	
	/*
	 * Type: SERVER_REP_GAME_HAND_ADD_CARD
	 * Param-0: [CardModel] card
	 */
	SERVER_REP_GAME_HAND_ADD_CARD,
	
	/*
	 * Type: SERVER_REP_GAME_UPDATE_POINTS
	 * Param-0: [int] points
	 * Param-1: [String] playerId
	 */
	SERVER_REP_GAME_UPDATE_POINTS,
	
	/*
	 * Type: SERVER_REP_ATTACK_MSG
	 * Param-0: [AttackMsg] attackMessage
	 */
	SERVER_REP_ATTACK_MSG,
	
    /*
     * Type: SERVER_REP_CARD_USED
     * Param-0: [CardModel] usedCard
     */
    SERVER_REP_CARD_USED,

    /*
     * Type: SERVER_REP_GAME_OVER
     * Param-0: [GameOver] info
     */
    SERVER_REP_GAME_OVER,
    
    /*
     * Type: SERVER_REP_LOST_GAME
     */
    SERVER_REP_LOST_GAME,
    
    /*
     * Type: SERVER_REP_UPDATE_TURNS
     * Param-0: [String] currentPlayerId
     */
    SERVER_REP_UPDATE_TURNS,
    
	/*
	 * Type: UNKNOWN_TYPE
	 */
	UNKNOWN_TYPE

}
