package database;

import java.util.List;

import database.entity.CardEntity;
import database.entity.CardsEntityManager;


public class DriverSQL {
	private CardsEntityManager cardsEntityManager;
	private List<CardEntity> cardsInfo;
	
	public DriverSQL() {
		cardsEntityManager = new CardsEntityManager();
	}
	
	public List<CardEntity> getCardsInfo() {
		return cardsInfo;
	}

	public void printCardDb() {
		cardsEntityManager.connectToDb();
		cardsEntityManager.printCardDb();
		cardsEntityManager.disConnectFromDb();
	}
	
	public void initCardDb() {
		cardsEntityManager.connectToDb();
		cardsEntityManager.initiateCardsValues();
		cardsEntityManager.printCardDb();
		cardsEntityManager.disConnectFromDb();
	}
	
	public void getCardsInfoFromDb() {
		cardsEntityManager.connectToDb();
		cardsInfo = cardsEntityManager.getCards();
		cardsEntityManager.disConnectFromDb();
	}
	
}
