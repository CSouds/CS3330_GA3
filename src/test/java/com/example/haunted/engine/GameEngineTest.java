package com.example.haunted.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.haunted.config.DungeonFactory;
import com.example.haunted.model.Direction;

class GameEngineTest {

    @Test
    void moveDelegatesToMovementEngine() {
        GameEngine engine = DungeonFactory.createGame();

        var result = engine.move(Direction.EAST);

        assertTrue(result.isSuccess());
        assertEquals("lectureHall", engine.getCurrentRoom().getId());
        assertEquals("Abandoned Lecture Hall", engine.getCurrentRoom().getName());
    }

    @Test
    void pickUpItemDelegatesToInteractionEngine() {
        GameEngine engine = DungeonFactory.createGame();

        engine.move(Direction.EAST);
        var result = engine.pickUpItem("Coffee Potion");

        assertTrue(result.isSuccess());
        assertEquals("Picked up Coffee Potion.", result.getMessage());
        assertTrue(engine.getPlayer().getInventory().contains("Coffee Potion"));
    }

    @Test
    void attackReturnsMonsterNotFoundWhenMonsterDoesNotExist() {
        GameEngine engine = DungeonFactory.createGame();

        var result = engine.attack("Fake Monster");
 
        assertFalse(result.isSuccess());
        assertEquals("Monster not found.", result.getMessage());
        assertEquals(0, result.getDamageToMonster());
        assertEquals(0, result.getDamageToPlayer());
    }

    @Test
    void getCurrentRoomMatchesPlayersCurrentRoom() {
        GameEngine engine = DungeonFactory.createGame();

        assertEquals(engine.getPlayer().getCurrentRoom(), engine.getCurrentRoom());
    }

    @Test
    void gameIsNotWonInitially() {
        GameEngine engine = DungeonFactory.createGame();

        assertFalse(engine.isGameWon());
    }

    @Test
    void gameIsNotOverInitially() {
        GameEngine engine = DungeonFactory.createGame();

        assertFalse(engine.isGameOver());
    }

    @Test
    void gameIsOverWhenPlayerDies() {
        GameEngine engine = DungeonFactory.createGame();

        engine.getPlayer().takeDamage(999);

        assertTrue(engine.isGameOver());
    }

    @Test
    void gameIsWonWhenQuestCompleteAndPlayerAlive() {
        GameEngine engine = DungeonFactory.createGame();

        engine.getQuest().markGradebookRecovered();
        engine.getQuest().markPhantomDefeated();

        assertTrue(engine.isGameWon());
    }

    @Test
    void gameIsNotWonWhenQuestCompleteButPlayerDead() {
        GameEngine engine = DungeonFactory.createGame();

        engine.getQuest().markGradebookRecovered();
        engine.getQuest().markPhantomDefeated();
        engine.getPlayer().takeDamage(999);

        assertFalse(engine.isGameWon());
    }
    @Test
    void equipItemDelegatesToInteractionEngine() {
        GameEngine engine = DungeonFactory.createGame();

        engine.move(Direction.EAST); // lectureHall
        engine.move(Direction.EAST); // labStorage
        engine.pickUpItem("Calculator Shield");

        var result = engine.equipItem("Calculator Shield");

        assertTrue(result.isSuccess());
        assertEquals("Equipped armor Calculator Shield.", result.getMessage());
        assertNotNull(engine.getPlayer().getEquippedArmor());
        assertEquals("Calculator Shield", engine.getPlayer().getEquippedArmor().getName());
    }
    @Test
    void useItemDelegatesToInteractionEngine() {
        GameEngine engine = DungeonFactory.createGame();

        engine.move(Direction.EAST); // lectureHall
        engine.getPlayer().takeDamage(20);
        engine.pickUpItem("Coffee Potion");

        var result = engine.useItem("Coffee Potion");

        assertTrue(result.isSuccess());
        assertEquals("Used Coffee Potion.", result.getMessage());
        assertEquals(45, engine.getPlayer().getHealth());
        assertFalse(engine.getPlayer().getInventory().contains("Coffee Potion"));
    }
    @Test
    void unlockRoomDelegatesToInteractionEngine() {
        GameEngine engine = DungeonFactory.createGame();

        engine.move(Direction.EAST); // lectureHall
        engine.move(Direction.EAST); // labStorage
        engine.pickUpItem("Archive Key");
        engine.move(Direction.WEST); // back to lectureHall

        var result = engine.unlockRoom(Direction.NORTH);

        assertTrue(result.isSuccess());
        assertEquals("Unlocked Exam Archive.", result.getMessage());
        assertFalse(engine.getCurrentRoom().getExit(Direction.NORTH).isLocked());
    }
}
