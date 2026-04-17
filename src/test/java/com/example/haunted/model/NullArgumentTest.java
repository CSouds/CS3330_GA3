package com.example.haunted.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Exercises every {@code Objects.requireNonNull(...)} branch in the model
 * package. These NPE paths are otherwise never executed and show up as missed
 * branches/lines in JaCoCo, and as surviving mutants in Pitest (the
 * "removed null check" mutator).
 *
 * Each test is a single {@link Executable} that should throw
 * {@link NullPointerException}; running them through one parameterized test
 * keeps the file dense without sacrificing readability.
 */
public class NullArgumentTest {

    /**
     * Every constructor / setter argument that is wrapped in
     * Objects.requireNonNull. Each entry is (label, code-that-must-NPE).
     */
    public static Stream<Arguments> nullArgumentCases() {
        // Reusable non-null fixtures
        Inventory inv = new Inventory(5);
        Room room = new Room("r", "Room", "desc");
        List<Item> emptyLoot = new ArrayList<>();

        return Stream.of(
                // ---- Item (via concrete subclasses, since Item is abstract) ----
                Arguments.of("Potion(null name)",
                        (Executable) () -> new Potion(null, "d", 5)),
                Arguments.of("Potion(null description)",
                        (Executable) () -> new Potion("p", null, 5)),
                Arguments.of("Weapon(null name)",
                        (Executable) () -> new Weapon(null, "d", 5)),
                Arguments.of("Weapon(null description)",
                        (Executable) () -> new Weapon("w", null, 5)),
                Arguments.of("Armor(null name)",
                        (Executable) () -> new Armor(null, "d", 5)),
                Arguments.of("Armor(null description)",
                        (Executable) () -> new Armor("a", null, 5)),
                Arguments.of("Key(null name)",
                        (Executable) () -> new Key(null, "d")),
                Arguments.of("Key(null description)",
                        (Executable) () -> new Key("k", null)),
                Arguments.of("QuestItem(null name)",
                        (Executable) () -> new QuestItem(null, "d")),
                Arguments.of("QuestItem(null description)",
                        (Executable) () -> new QuestItem("q", null)),

                // ---- Monster ----
                Arguments.of("Monster(null name)",
                        (Executable) () -> new Monster(null, 10, 1, 1, emptyLoot)),
                Arguments.of("Monster(null loot)",
                        (Executable) () -> new Monster("m", 10, 1, 1, null)),

                // ---- Player ----
                Arguments.of("Player(null name)",
                        (Executable) () -> new Player(null, 10, 1, 1, inv)),
                Arguments.of("Player(null inventory)",
                        (Executable) () -> new Player("p", 10, 1, 1, null)),
                Arguments.of("Player.setCurrentRoom(null)",
                        (Executable) () -> new Player("p", 10, 1, 1, inv).setCurrentRoom(null)),
                Arguments.of("Player.equipWeapon(null)",
                        (Executable) () -> new Player("p", 10, 1, 1, inv).equipWeapon(null)),
                Arguments.of("Player.equipArmor(null)",
                        (Executable) () -> new Player("p", 10, 1, 1, inv).equipArmor(null)),

                // ---- Quest ----
                Arguments.of("Quest(null name)",
                        (Executable) () -> new Quest(null, "d")),
                Arguments.of("Quest(null description)",
                        (Executable) () -> new Quest("q", null)),

                // ---- Room constructor ----
                Arguments.of("Room(null id)",
                        (Executable) () -> new Room(null, "n", "d")),
                Arguments.of("Room(null name)",
                        (Executable) () -> new Room("i", null, "d")),
                Arguments.of("Room(null description)",
                        (Executable) () -> new Room("i", "n", null)),

                // ---- Room.connect ----
                Arguments.of("Room.connect(null direction, room)",
                        (Executable) () -> room.connect(null, room)),
                Arguments.of("Room.connect(direction, null room)",
                        (Executable) () -> room.connect(Direction.NORTH, null)),

                // ---- Room.addItem / addMonster ----
                Arguments.of("Room.addItem(null)",
                        (Executable) () -> room.addItem(null)),
                Arguments.of("Room.addMonster(null)",
                        (Executable) () -> room.addMonster(null)),

                // ---- Trap ----
                Arguments.of("Trap(null name)",
                        (Executable) () -> new Trap(null, TrapType.ELECTRIC, 5, true, true)),
                Arguments.of("Trap(null type)",
                        (Executable) () -> new Trap("t", null, 5, true, true))
        );
    }

    @ParameterizedTest(name = "[{index}] {0} should throw NPE")
    @MethodSource("nullArgumentCases")
    @DisplayName("All Objects.requireNonNull branches throw NPE on null")
    void nullArgumentsThrowNullPointerException(String label, Executable code) {
        assertThrows(NullPointerException.class, code,
                "Expected NPE for case: " + label);
    }

    // ---- A few extras that don't fit the parameterized table neatly ----

    @Test
    void bossMonsterRejectsNullName() {
        // BossMonster delegates to Monster's requireNonNull on name
        assertThrows(NullPointerException.class,
                () -> new BossMonster(null, 10, 1, 1, new ArrayList<>(), 2));
    }

    @Test
    void bossMonsterRejectsNullLoot() {
        assertThrows(NullPointerException.class,
                () -> new BossMonster("Boss", 10, 1, 1, null, 2));
    }
}