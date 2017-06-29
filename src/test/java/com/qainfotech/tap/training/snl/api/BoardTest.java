package com.qainfotech.tap.training.snl.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BoardTest {

	Board boardTest;

	@BeforeTest

	public void loadDB() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException,
			GameInProgressException, MaxPlayersReachedExeption {
		boardTest = new Board();
		boardTest.registerPlayer("srishti");
		boardTest.registerPlayer("purvi");
		boardTest.registerPlayer("aman");
		

	}

	
	@Test(expectedExceptions=PlayerExistsException.class)
	public void acheckANameOfPlayerAlreadyExist() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException
	{
		boardTest.registerPlayer("srishti");
	}
@Test(expectedExceptions=MaxPlayersReachedExeption.class)

public void bcheckMaximumNumberOfPlayer() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException
{
	boardTest.registerPlayer("arpit");
	boardTest.registerPlayer("nachi");
}

@Test(expectedExceptions=NoUserWithSuchUUIDException.class)
public void cNoUserWithSuchUUID() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException,
		GameInProgressException, MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException, InvalidTurnException {
           
        boardTest.deletePlayer(this.boardTest.getUUID());
        
 
}

public void dtoCheckIfPlayerDeleted() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException,
GameInProgressException, MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException, InvalidTurnException {
   UUID uuid= (UUID)boardTest.getData().getJSONArray("players").getJSONObject(0).get("uuid");
	   boardTest.deletePlayer(uuid);
	
assertThat(boardTest.getData().length()).isEqualTo(3);


}
/*@Test
public void toCheckRollDice() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException,
GameInProgressException, MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException, InvalidTurnException {
	 UUID uuid= (UUID)boardTest.getData().getJSONArray("players").getJSONObject(1).get("uuid");
	 
	 boardTest.rollDice(uuid);
	 
}
*/
	// assertThat(boardTest.registerPlayer("purvi").length()).isEqualTo(3);

	   @Test(expectedExceptions=GameInProgressException.class)
	public void etoCheckGameAlraedyInProgress() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException
	{
		   UUID uuid= (UUID)boardTest.getData().getJSONArray("players").getJSONObject(0).get("uuid");
		   boardTest.deletePlayer(uuid);
	
	boardTest.rollDice((UUID) ((JSONObject) boardTest.getData().getJSONArray("players").get(0)).get("uuid"));
	boardTest.registerPlayer("Anvit");
	}
	

	   @Test(expectedExceptions=InvalidTurnException.class)
	public void ftoCheckInvalidTurn() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException
	{
		   
	boardTest.rollDice((UUID) ((JSONObject) boardTest.getData().getJSONArray("players").get(2)).get("uuid"));
	
	
	}
	   public void ftoCheckPositionTurn() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException
		{
			   
		boardTest.rollDice((UUID) ((JSONObject) boardTest.getData().getJSONArray("players").get(1)).get("uuid"));
		
		
		
		}
	
	

}
