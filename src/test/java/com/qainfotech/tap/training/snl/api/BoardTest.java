package com.qainfotech.tap.training.snl.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BoardTest {

	Board boardTest;
	Board boardTestReader;

	@BeforeMethod
	public void loadDB() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException,
			GameInProgressException, MaxPlayersReachedExeption {
		boardTest = new Board();
		boardTest.registerPlayer("srishti");
		boardTest.registerPlayer("purvi");
		boardTest.registerPlayer("aman");

	}

	@Test
	public void check_For_Constructor() throws IOException {
		JSONObject data1 = boardTest.data;
		Board board = new Board(boardTest.uuid);
		JSONObject data2 = board.data;
		assertThat(data1).isNotEqualTo(data2);

	}

	@Test(expectedExceptions = PlayerExistsException.class)
	public void check_Name_Of_Player_Already_Exist() throws FileNotFoundException, UnsupportedEncodingException,
			PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
		boardTest.registerPlayer("srishti");
	}

	@Test(expectedExceptions = MaxPlayersReachedExeption.class)

	public void check_Maximum_Number_Of_Player() throws FileNotFoundException, UnsupportedEncodingException,
			PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
		boardTest.registerPlayer("arpit");
		boardTest.registerPlayer("nachi");
	}

	@Test(expectedExceptions = NoUserWithSuchUUIDException.class)
	public void no_UserWithSuchUUID()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException, InvalidTurnException {

		boardTest.deletePlayer(this.boardTest.getUUID());

	}

	public void to_Check_If_A_Player_Deleted()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException, InvalidTurnException {
		UUID uuid = (UUID) boardTest.getData().getJSONArray("players").getJSONObject(0).get("uuid");
		boardTest.deletePlayer(uuid);

		assertThat(boardTest.getData().length()).isEqualTo(2);

	}

	@Test(expectedExceptions = GameInProgressException.class)
	public void to_Check_If_Game_Alraedy_In_Progress()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException {
		// UUID uuid = (UUID)
		// boardTest.getData().getJSONArray("players").getJSONObject(0).get("uuid");
		// boardTest.deletePlayer(uuid);

		boardTest.rollDice((UUID) ((JSONObject) boardTest.getData().getJSONArray("players").get(0)).get("uuid"));
		boardTest.registerPlayer("Anvit");
	}

	@Test(expectedExceptions = InvalidTurnException.class)
	public void to_Check_Invalid_Turn()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException {

		boardTest.rollDice((UUID) ((JSONObject) boardTest.getData().getJSONArray("players").get(2)).get("uuid"));

	}

	@Test
	public void to_Check_Position_Is_Equal_Or_Not()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException {
		Object initial = ((JSONObject) boardTest.getData().getJSONArray("players").get(0)).getInt("position");
		UUID uuid = (UUID) ((JSONObject) boardTest.getData().getJSONArray("players").get(0)).get("uuid");

		Object dice = boardTest.rollDice(uuid).get("dice");
		Object finalPosition = ((JSONObject) boardTest.getData().getJSONArray("players").get(0)).getInt("position");
		Object rolledDice = boardTest.getData().getJSONArray("steps").getJSONObject((int) initial + (int) dice)
				.get("target");

		assertThat(rolledDice).isEqualTo(finalPosition);
		int h = (int) finalPosition;
		((JSONObject) boardTest.getData().getJSONArray("steps").get(h)).getInt("type");

	}

	@Test
	public void to_Check_Position_Turn()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException {
		boardTest.rollDice((UUID) ((JSONObject) boardTest.getData().getJSONArray("players").get(0)).get("uuid"));
		boardTest.rollDice((UUID) ((JSONObject) boardTest.getData().getJSONArray("players").get(1)).get("uuid"));
		boardTest.rollDice((UUID) ((JSONObject) boardTest.getData().getJSONArray("players").get(2)).get("uuid"));
		assertThat(boardTest.getData().get("turn")).isEqualTo(0);
	}

	@Test
	public void to_Check_Position_Move()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException {
		Object initial = ((JSONObject) boardTest.getData().getJSONArray("players").get(0)).getInt("position");
		UUID uuid = (UUID) ((JSONObject) boardTest.getData().getJSONArray("players").get(0)).get("uuid");
		Object obj = boardTest.rollDice(uuid);
		Object msg = ((JSONObject) obj).get("message");
		Object dice = ((JSONObject) obj).get("dice");
		Object finalPosition = boardTest.getData().getJSONArray("steps").getJSONObject((int) initial + (int) dice)
				.get("target");
		int h = (int) initial + (int) dice;
		int a = ((JSONObject) boardTest.getData().getJSONArray("steps").get(h)).getInt("type");
		if (a == 0) {
			assertThat(msg.toString()).isEqualTo("Player moved to " + finalPosition);
		}

	}

	@Test
	public void to_Check_New_Position()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException {
		((JSONObject) boardTest.getData().getJSONArray("players").get(1)).put("position", 100);
		Object d = ((JSONObject) boardTest.getData().getJSONArray("players").get(0)).getInt("position");
		UUID uuid = (UUID) ((JSONObject) boardTest.getData().getJSONArray("players").get(0)).get("uuid");
		Object obj = boardTest.rollDice(uuid);
		Object msg = ((JSONObject) obj).get("message");
		if ((int) d >= 100)
			assertThat(msg.toString()).isEqualTo("Incorrect roll of dice. Player did not move");

	}

	@Test
	public void to_Check_Position_UUID()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException {
		UUID firstPlayer = (UUID) boardTest.getData().getJSONArray("players").getJSONObject(0).get("uuid");

		assertThat(boardTest.toString()).isNotEqualTo(firstPlayer.toString());
	}

	@Test
	public void To_Check_Snake() throws FileNotFoundException, UnsupportedEncodingException, IOException,
			PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, InvalidTurnException {
		Board board = new Board();
		board.registerPlayer("s1");
		board.registerPlayer("s2");
		board.registerPlayer("s3");
		board.registerPlayer("s4");
		JSONArray steps = new JSONArray();
		steps = board.getData().getJSONArray("steps");
		JSONObject step = new JSONObject();
		for (int j = 1; j < 7; j++) {
			step = steps.getJSONObject(j);
			step.put("type", 1);
			step.put("target", j - 1);
		}
		JSONObject data = board.getData();
		JSONArray playerArray = data.getJSONArray("players");
		for (int i = 0; i < 4; i++) {
			int turn = (int) data.get("turn");
			JSONObject singlePlayer = playerArray.getJSONObject(turn);
			UUID singlePlayerUUID = (UUID) singlePlayer.get("uuid");
			int currentPosition = (int) singlePlayer.get("position");
			JSONObject response = board.rollDice(singlePlayerUUID);
			int newPosition = (int) singlePlayer.get("position");
			assertThat(data.getJSONArray("steps").getJSONObject((int) response.get("dice")).get("target"))
					.isEqualTo(newPosition);
			Object dice = response.get("dice");
			Object msg = response.get("message");
			int position = (int) currentPosition + (int) dice;
			
			int type = ((JSONObject) board.getData().getJSONArray("steps").get(position)).getInt("type");
			if (type == 1) {
				
				assertThat(msg.toString()).isEqualTo("Player was bit by a snake, moved back to " + newPosition);
			}
		}
	}

	@Test
	public void To_Check_ladder() throws FileNotFoundException, UnsupportedEncodingException, IOException,
			PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, InvalidTurnException {
		Board board = new Board();
		board.registerPlayer("s1");
		board.registerPlayer("s2");
		board.registerPlayer("s3");
		board.registerPlayer("s4");
		JSONArray steps = new JSONArray();
		steps = board.getData().getJSONArray("steps");
		JSONObject step = new JSONObject();
		for (int j = 1; j < 7; j++) {
			step = steps.getJSONObject(j);
			step.put("type", 1);
			step.put("target", j * 10);
		}
		JSONObject data = board.getData();
		JSONArray playerArray = data.getJSONArray("players");
		for (int i = 0; i < 4; i++) {
			int turn = (int) data.get("turn");
			JSONObject singlePlayer = playerArray.getJSONObject(turn);
			UUID singlePlayerUUID = (UUID) singlePlayer.get("uuid");
			int currentPosition = (int) singlePlayer.get("position");
			JSONObject response = board.rollDice(singlePlayerUUID);
			int newPosition = (int) singlePlayer.get("position");
			assertThat(data.getJSONArray("steps").getJSONObject((int) response.get("dice")).get("target"))
					.isEqualTo(newPosition);
			Object dice = response.get("dice");
			Object msg = response.get("message");
			int position = (int) currentPosition + (int) dice;
		;
			int type = ((JSONObject) board.getData().getJSONArray("steps").get(position)).getInt("type");
			if (type == 2) {
				
				assertThat(msg.toString()).isEqualTo("Player climbed a ladder, moved to " + newPosition);
			}
		}
	}
}
