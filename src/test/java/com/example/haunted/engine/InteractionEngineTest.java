package com.example.haunted.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.haunted.model.Armor;
import com.example.haunted.model.Direction;
import com.example.haunted.model.Inventory;
import com.example.haunted.model.Key;
import com.example.haunted.model.Player;
import com.example.haunted.model.Potion;
import com.example.haunted.model.Quest;
import com.example.haunted.model.QuestItem;
import com.example.haunted.model.QuestStatus;
import com.example.haunted.model.Room;
import com.example.haunted.model.Weapon;
import com.example.haunted.rules.QuestTracker;

class InteractionEngineTest {

    @Test
    void pickUpMissingItemFails() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Quest quest = new Quest("Escape", "Recover the gradebook");
        Room room = new Room("start", "Start", "starting room");
        player.setCurrentRoom(room);

        var result = engine.pickUpItem(player, quest, "Missing Item");

        assertFalse(result.isSuccess());
        assertEquals("Item not found in the room.", result.getMessage());
        assertTrue(player.getInventory().getItems().isEmpty());
    }

    @Test
    void pickUpItemAddsItToInventoryAndRemovesItFromRoom() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Quest quest = new Quest("Escape", "Recover the gradebook");
        Room room = new Room("start", "Start", "starting room");
        Potion potion = new Potion("Coffee Potion", "Heals player", 15);
        room.addItem(potion);
        player.setCurrentRoom(room);

        var result = engine.pickUpItem(player, quest, "Coffee Potion");

        assertTrue(result.isSuccess());
        assertEquals("Picked up Coffee Potion.", result.getMessage());
        assertTrue(player.getInventory().contains("Coffee Potion"));
        assertTrue(room.findItem("Coffee Potion").isEmpty());
    }

    @Test
    void pickUpItemFailsWhenInventoryIsFullAndReturnsItemToRoom() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(1));
        Quest quest = new Quest("Escape", "Recover the gradebook");
        Room room = new Room("start", "Start", "starting room");

        player.getInventory().addItem(new Potion("Old Potion", "fills inventory", 5));
        room.addItem(new Potion("Coffee Potion", "Heals player", 15));
        player.setCurrentRoom(room);

        var result = engine.pickUpItem(player, quest, "Coffee Potion");

        assertFalse(result.isSuccess());
        assertEquals("Inventory is full.", result.getMessage());
        assertFalse(player.getInventory().contains("Coffee Potion"));
        assertTrue(room.findItem("Coffee Potion").isPresent());
    }

    @Test
    void pickUpLostGradebookUpdatesQuest() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Quest quest = new Quest("Escape", "Recover the gradebook");
        Room room = new Room("start", "Start", "starting room");
        room.addItem(new QuestItem("Lost Gradebook", "Quest item"));
        player.setCurrentRoom(room);

        var result = engine.pickUpItem(player, quest, "Lost Gradebook");

        assertTrue(result.isSuccess());
        assertTrue(quest.isGradebookRecovered());
        assertEquals(QuestStatus.IN_PROGRESS, quest.getStatus());
    }

    @Test
    void useMissingItemFails() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));

        var result = engine.useItem(player, "Coffee Potion");

        assertFalse(result.isSuccess());
        assertEquals("Item not found in inventory.", result.getMessage());
    }

    @Test
    void usingNonPotionFails() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        player.getInventory().addItem(new Weapon("Sword", "weapon", 4));

        var result = engine.useItem(player, "Sword");

        assertFalse(result.isSuccess());
        assertEquals("That item cannot be used.", result.getMessage());
        assertTrue(player.getInventory().contains("Sword"));
    }

    @Test
    void usingPotionHealsPlayerAndRemovesPotion() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        player.takeDamage(20);
        player.getInventory().addItem(new Potion("Coffee Potion", "heals", 15));

        var result = engine.useItem(player, "Coffee Potion");

        assertTrue(result.isSuccess());
        assertEquals("Used Coffee Potion.", result.getMessage());
        assertEquals(45, player.getHealth());
        assertFalse(player.getInventory().contains("Coffee Potion"));
    }

    @Test
    void equipMissingItemFails() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));

        var result = engine.equipItem(player, "Missing");

        assertFalse(result.isSuccess());
        assertEquals("Item not found in inventory.", result.getMessage());
    }

    @Test
    void equippingWeaponSucceeds() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Weapon weapon = new Weapon("Sword", "weapon", 4);
        player.getInventory().addItem(weapon);

        var result = engine.equipItem(player, "Sword");

        assertTrue(result.isSuccess());
        assertEquals("Equipped weapon Sword.", result.getMessage());
        assertEquals(weapon, player.getEquippedWeapon());
        assertEquals(14, player.getAttackPower());
    }

    @Test
    void equippingArmorSucceeds() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Armor armor = new Armor("Shield", "armor", 3);
        player.getInventory().addItem(armor);

        var result = engine.equipItem(player, "Shield");

        assertTrue(result.isSuccess());
        assertEquals("Equipped armor Shield.", result.getMessage());
        assertEquals(armor, player.getEquippedArmor());
        assertEquals(8, player.getDefensePower());
    }

    @Test
    void equippingNonEquippableItemFails() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        player.getInventory().addItem(new Potion("Coffee Potion", "heals", 15));

        var result = engine.equipItem(player, "Coffee Potion");

        assertFalse(result.isSuccess());
        assertEquals("That item cannot be equipped.", result.getMessage());
        assertNull(player.getEquippedWeapon());
        assertNull(player.getEquippedArmor());
    }

    @Test
    void unlockRoomFailsWhenNoExitExists() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Room start = new Room("start", "Start", "starting room");
        player.setCurrentRoom(start);

        var result = engine.unlockRoom(player, Direction.NORTH);

        assertFalse(result.isSuccess());
        assertEquals("There is no room in that direction.", result.getMessage());
    }

    @Test
    void unlockRoomReturnsAlreadyUnlockedWhenRoomIsNotLocked() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Room start = new Room("start", "Start", "starting room");
        Room next = new Room("next", "Next", "open room");
        start.connect(Direction.NORTH, next);
        player.setCurrentRoom(start);

        var result = engine.unlockRoom(player, Direction.NORTH);

        assertTrue(result.isSuccess());
        assertEquals("The room is already unlocked.", result.getMessage());
    }

    @Test
    void unlockRoomFailsWithoutCorrectKey() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Room start = new Room("start", "Start", "starting room");
        Room locked = new Room("locked", "Locked Room", "locked");
        locked.setLocked(true, "Archive Key");
        start.connect(Direction.NORTH, locked);
        player.setCurrentRoom(start);

        var result = engine.unlockRoom(player, Direction.NORTH);

        assertFalse(result.isSuccess());
        assertEquals("You do not have the correct key.", result.getMessage());
        assertTrue(locked.isLocked());
    }

    @Test
    void unlockRoomSucceedsWithCorrectKey() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Room start = new Room("start", "Start", "starting room");
        Room locked = new Room("locked", "Locked Room", "locked");
        locked.setLocked(true, "Archive Key");
        start.connect(Direction.NORTH, locked);
        player.setCurrentRoom(start);
        player.getInventory().addItem(new Key("Archive Key", "opens archive"));

        var result = engine.unlockRoom(player, Direction.NORTH);

        assertTrue(result.isSuccess());
        assertEquals("Unlocked Locked Room.", result.getMessage());
        assertFalse(locked.isLocked());
    }
    
    @Test
    void unlockRoomFailsWhenLockedRoomHasNoRequiredKeyName() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));

        Room start = new Room("start", "Start", "starting room");
        Room locked = new Room("locked", "Locked Room", "locked");
        locked.setLocked(true, null);

        start.connect(Direction.NORTH, locked);
        player.setCurrentRoom(start);

        var result = engine.unlockRoom(player, Direction.NORTH);

        assertFalse(result.isSuccess());
        assertEquals("The room cannot be unlocked.", result.getMessage());
        assertTrue(locked.isLocked());
    }
    @Test
    void unlockRoomFailsWhenMatchingItemIsNotAKey() {
        InteractionEngine engine = new InteractionEngine(new QuestTracker());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));

        Room start = new Room("start", "Start", "starting room");
        Room locked = new Room("locked", "Locked Room", "locked");
        locked.setLocked(true, "Archive Key");

        start.connect(Direction.NORTH, locked);
        player.setCurrentRoom(start);

        // Correct name, wrong type
        player.getInventory().addItem(new Potion("Archive Key", "fake key", 10));

        var result = engine.unlockRoom(player, Direction.NORTH);

        assertFalse(result.isSuccess());
        assertEquals("You do not have the correct key.", result.getMessage());
        assertTrue(locked.isLocked());
    }
}
