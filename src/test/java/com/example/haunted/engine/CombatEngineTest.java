package com.example.haunted.engine;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.haunted.model.Inventory;
import com.example.haunted.model.Player;
import com.example.haunted.model.Potion;
import com.example.haunted.model.Quest;
import com.example.haunted.model.QuestStatus;
import com.example.haunted.model.Room;
import com.example.haunted.model.Monster;
import com.example.haunted.rules.DamageCalculator;
import com.example.haunted.rules.QuestTracker;

class CombatEngineTest {

    @Test
    void attackFailsWhenMonsterIsNull() {
        CombatEngine engine = new CombatEngine(new DamageCalculator(), new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Quest quest = new Quest("Escape", "Recover the gradebook and defeat the phantom.");
        Room room = new Room("start", "Start", "starting room");
        player.setCurrentRoom(room);

        var result = engine.attack(player, quest, null);

        assertFalse(result.isSuccess());
        assertEquals("Monster not found.", result.getMessage());
        assertEquals(0, result.getDamageToMonster());
        assertEquals(0, result.getDamageToPlayer());
        assertFalse(result.isMonsterDefeated());
        assertTrue(result.getDroppedItems().isEmpty());
    }

    @Test
    void attackFailsWhenPlayerIsAlreadyDefeated() {
        CombatEngine engine = new CombatEngine(new DamageCalculator(), new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Quest quest = new Quest("Escape", "Recover the gradebook and defeat the phantom.");
        Room room = new Room("start", "Start", "starting room");
        Monster monster = new Monster("Ghost", 20, 6, 2, List.of());

        player.setCurrentRoom(room);
        player.takeDamage(999);

        var result = engine.attack(player, quest, monster);

        assertFalse(result.isSuccess());
        assertEquals("Player is defeated.", result.getMessage());
        assertEquals(0, result.getDamageToMonster());
        assertEquals(0, result.getDamageToPlayer());
        assertFalse(result.isMonsterDefeated());
        assertTrue(monster.isAlive());
    }

    @Test
    void attackFailsWhenMonsterIsAlreadyDefeated() {
        CombatEngine engine = new CombatEngine(new DamageCalculator(), new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Quest quest = new Quest("Escape", "Recover the gradebook and defeat the phantom.");
        Room room = new Room("start", "Start", "starting room");
        Monster monster = new Monster("Ghost", 20, 6, 2, List.of());

        player.setCurrentRoom(room);
        monster.takeDamage(999);

        var result = engine.attack(player, quest, monster);

        assertFalse(result.isSuccess());
        assertEquals("Monster is already defeated.", result.getMessage());
        assertEquals(0, result.getDamageToMonster());
        assertEquals(0, result.getDamageToPlayer());
        assertTrue(result.isMonsterDefeated());
    }

    @Test
    void defeatingMonsterDropsLootIntoRoom() {
        CombatEngine engine = new CombatEngine(new DamageCalculator(), new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Quest quest = new Quest("Escape", "Recover the gradebook and defeat the phantom.");
        Room room = new Room("start", "Start", "starting room");
        Potion loot = new Potion("Coffee Potion", "heals", 15);
        Monster monster = new Monster("Ghost", 5, 6, 2, List.of(loot));

        player.setCurrentRoom(room);

        var result = engine.attack(player, quest, monster);

        assertTrue(result.isSuccess());
        assertEquals("Defeated Ghost.", result.getMessage());
        assertEquals(8, result.getDamageToMonster());
        assertEquals(0, result.getDamageToPlayer());
        assertTrue(result.isMonsterDefeated());
        assertEquals(1, result.getDroppedItems().size());
        assertEquals("Coffee Potion", result.getDroppedItems().get(0).getName());
        assertTrue(room.findItem("Coffee Potion").isPresent());
    }

    @Test
    void survivingMonsterCounterattacksPlayer() {
        CombatEngine engine = new CombatEngine(new DamageCalculator(), new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Quest quest = new Quest("Escape", "Recover the gradebook and defeat the phantom.");
        Room room = new Room("start", "Start", "starting room");
        Monster monster = new Monster("Ghost", 20, 9, 2, List.of());

        player.setCurrentRoom(room);

        var result = engine.attack(player, quest, monster);

        assertTrue(result.isSuccess());
        assertEquals("Attacked Ghost.", result.getMessage());
        assertEquals(8, result.getDamageToMonster());
        assertEquals(4, result.getDamageToPlayer());
        assertFalse(result.isMonsterDefeated());
        assertEquals(46, player.getHealth());
        assertEquals(12, monster.getHealth());
        assertTrue(result.getDroppedItems().isEmpty());
    }

    @Test
    void defeatingFinalExamPhantomUpdatesQuestProgress() {
        CombatEngine engine = new CombatEngine(new DamageCalculator(), new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Quest quest = new Quest("Escape", "Recover the gradebook and defeat the phantom.");
        Room room = new Room("start", "Start", "starting room");
        Monster monster = new Monster("Final Exam Phantom", 5, 8, 2, List.of());

        player.setCurrentRoom(room);

        var result = engine.attack(player, quest, monster);

        assertTrue(result.isSuccess());
        assertTrue(result.isMonsterDefeated());
        assertTrue(quest.isPhantomDefeated());
        assertFalse(quest.isComplete());
        assertEquals(QuestStatus.IN_PROGRESS, quest.getStatus());
    }
}
