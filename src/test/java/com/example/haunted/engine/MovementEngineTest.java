package com.example.haunted.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.haunted.model.Direction;
import com.example.haunted.model.Inventory;
import com.example.haunted.model.Player;
import com.example.haunted.model.Room;
import com.example.haunted.model.Trap;
import com.example.haunted.model.TrapType;
import com.example.haunted.rules.TrapResolver;

class MovementEngineTest {

    @Test
    void movingToMissingExitFails() {
        MovementEngine engine = new MovementEngine(new TrapResolver());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));
        Room start = new Room("start", "Start", "starting room");
        player.setCurrentRoom(start);

        var result = engine.move(player, Direction.NORTH);

        assertFalse(result.isSuccess());
        assertEquals("There is no room in that direction.", result.getMessage());
        assertEquals(start, player.getCurrentRoom());
    }

    @Test
    void movingToLockedRoomFails() {
        MovementEngine engine = new MovementEngine(new TrapResolver());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));

        Room start = new Room("start", "Start", "starting room");
        Room locked = new Room("locked", "Locked Room", "locked");
        locked.setLocked(true, "Key");
        start.connect(Direction.NORTH, locked);

        player.setCurrentRoom(start);

        var result = engine.move(player, Direction.NORTH);

        assertFalse(result.isSuccess());
        assertEquals("The room is locked.", result.getMessage());
        assertEquals(start, player.getCurrentRoom());
    }

    @Test
    void movingToNormalRoomSucceeds() {
        MovementEngine engine = new MovementEngine(new TrapResolver());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));

        Room start = new Room("start", "Start", "starting room");
        Room hall = new Room("hall", "Hallway", "empty hall");
        start.connect(Direction.EAST, hall);

        player.setCurrentRoom(start);

        var result = engine.move(player, Direction.EAST);

        assertTrue(result.isSuccess());
        assertEquals("Moved to Hallway.", result.getMessage());
        assertEquals(hall, player.getCurrentRoom());
        assertFalse(result.isTrapTriggered());
    }

    @Test
    void movingIntoArmedTrapTriggersTrap() {
        MovementEngine engine = new MovementEngine(new TrapResolver());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));

        Room start = new Room("start", "Start", "starting room");
        Room trapped = new Room("trap", "Trap Room", "danger");
        trapped.setTrap(new Trap("Spikes", TrapType.STEAM, 8, true, true));
        start.connect(Direction.SOUTH, trapped);

        player.setCurrentRoom(start);

        var result = engine.move(player, Direction.SOUTH);

        assertTrue(result.isSuccess());
        assertTrue(result.isTrapTriggered());
        assertEquals(8, result.getTrapDamage());
        assertEquals(42, player.getHealth());
        assertEquals(trapped, player.getCurrentRoom());
    }
    
    @Test
    void movingIntoRoomWithDisarmedTrapDoesNotTriggerTrap() {
        MovementEngine engine = new MovementEngine(new TrapResolver());
        Player player = new Player("Hero", 50, 10, 5, new Inventory(5));

        Room start = new Room("start", "Start", "starting room");
        Room trapped = new Room("trap", "Trap Room", "danger");
        trapped.setTrap(new Trap("Spikes", TrapType.STEAM, 8, false, true));
        start.connect(Direction.SOUTH, trapped);

        player.setCurrentRoom(start);

        var result = engine.move(player, Direction.SOUTH);

        assertTrue(result.isSuccess());
        assertFalse(result.isTrapTriggered());
        assertEquals(0, result.getTrapDamage());
        assertEquals(50, player.getHealth());
        assertEquals(trapped, player.getCurrentRoom());
        assertEquals("Moved to Trap Room.", result.getMessage());
    }
}
