package database;

import static org.junit.Assert.*;

import org.junit.Test;

public class SqlTest {

	@Test
	public void initDbTest() {
		DriverSQL driver = DriverSQL.getInstance();
		driver.initCardDb();
		fail("Failed to start DB");
	}

	@Test
	public void readFromDbTest() {
		DriverSQL driver = DriverSQL.getInstance();
		driver.printCardDb();
		fail("Failed to get information from DB");
	}
}
