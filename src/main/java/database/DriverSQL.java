package database;

import java.util.List;

import database.entity.CardEntity;
import database.entity.CardsEntityManager;


public class DriverSQL {
	CardsEntityManager cardsEntityManager;
	
	
	private DriverSQL() {
		cardsEntityManager = new CardsEntityManager();
	}
	
	private static DriverSQL driverSqlInstance = null;
	
	public static DriverSQL getInstance() {
		if (driverSqlInstance == null) {
			driverSqlInstance = new DriverSQL();
		}
		return driverSqlInstance;
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
	
	
	public List<CardEntity> getCardsInfo() {
		List<CardEntity> cards = null;
		cardsEntityManager.connectToDb();
		cards = cardsEntityManager.getCards();
		cardsEntityManager.disConnectFromDb();
		return cards;
	}
	
	
}
