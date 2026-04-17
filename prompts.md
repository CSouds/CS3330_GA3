# AI Prompts Log — CS 3330 Group Assignment 3

This document contains all prompts used during the development of the test suite for the **Haunted University Basement** project, as required by the academic integrity section of the assignment.

**Group members:** Connor Souders + partner
**Repository:** https://github.com/CSouds/CS3330_GA3.git

---

## Table of Contents

1. [Initial Setup & Templates](#1-initial-setup--templates)
2. [Test File Reviews](#2-test-file-reviews)
3. [Repository Analysis & Coverage](#3-repository-analysis--coverage)
4. [Eclipse / Build Errors](#4-eclipse--build-errors)
5. [EclEmma Coverage Reports](#5-eclemma-coverage-reports)
6. [PIT Mutation Testing](#6-pit-mutation-testing)
7. [Killing Surviving Mutants](#7-killing-surviving-mutants)
8. [Reference: Earlier Assignment 2 Prompts](#8-reference-earlier-assignment-2-prompts)

---

## 1. Initial Setup & Templates

### Prompt 1.1 — Getting started

> Do I really have to make like 1000 files to test everything?
>
> Help me remember how to create the files in Eclipse and give me a basic template for how to write each test.

---

## 2. Test File Reviews

### Prompt 2.1 — PlayerTest review

> How is this for PlayerTest?

```java
package com.example.haunted;

import com.example.haunted.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;

public class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Connor", 100, 10, 5, new Inventory(10));
        player.setCurrentRoom(new Room("start", "Start", "A room"));
    }

    @Test
    void shouldGetName() {
        assertEquals("Connor", player.getName());
    }

    @Test
    void shouldGetHealth() {
        assertEquals(100, player.getHealth());
    }

    @Test
    void shouldGetMaxHealth() {
        assertEquals(100, player.getMaxHealth());
    }

    @Test
    void shouldGetBaseAttack() {
        assertEquals(10, player.getBaseAttack());
    }

    @Test
    void shouldGetBaseDefense() {
        assertEquals(5, player.getBaseDefense());
    }

    @Test
    void shouldGetInventory() {
        assertEquals(new Inventory(10), player.getInventory());
    }

    @Test
    void shouldEquipGetWeapon() {
        Weapon w = new Weapon("Sword", "Slices people", 15);
        player.equipWeapon(w);
        assertEquals(w, player.getEquippedWeapon());
    }

    @Test
    void shouldEquipGetArmor() {
        Armor a = new Armor("Chainmail", "Made of chains", 3);
        player.equipArmor(a);
        assertEquals(a, player.getEquippedArmor());
    }

    @Test
    void shouldSetGetRoom() {
        Room s = new Room("Room1", "Kitchen", "Where there is food");
        player.setCurrentRoom(s);
        assertEquals(s, player.getCurrentRoom());
    }

    public static Stream<Arguments> damageCases() {
        return Stream.of(
            Arguments.of(10, 90),
            Arguments.of(20, 80),
            Arguments.of(100, 0),  // edge case: equal to health
            Arguments.of(150, 0)   // edge case: greater than health
        );
    }

    @ParameterizedTest
    @MethodSource("damageCases")
    public void shouldTakeDamage(int damage, int expectedHealth) {
        player.takeDamage(damage);
        assertEquals(expectedHealth, player.getHealth());
    }

    public static Stream<Arguments> healCases() {
        return Stream.of(
            Arguments.of(10, 10, 100),  // heal back to max
            Arguments.of(20, 80, 100),  // heal over max
            Arguments.of(120, 12),      // damage to floor, then heal back
            Arguments.of(50, 20, 70)    // normal case
        );
    }

    @ParameterizedTest
    @MethodSource("healCases")
    public void shouldTakeDamage(int damage, int health, int expectedHealth) {
        player.takeDamage(damage);
        player.heal(health);
        assertEquals(expectedHealth, player.getHealth());
    }

    public static Stream<Arguments> aliveCases() {
        return Stream.of(
            Arguments.of(100, false),
            Arguments.of(10, true),
            Arguments.of(0, true)
        );
    }

    @ParameterizedTest
    @MethodSource("aliveCases")
    public void aliveTest(int damage, boolean expectedAlive) {
        player.takeDamage(damage);
        assertEquals(expectedAlive, player.isAlive());
    }

    public static Stream<Arguments> attackPowerCases() {
        return Stream.of(
            Arguments.of(new Weapon("Sword", "Slices", 10), 20),
            Arguments.of(new Weapon("Axe", "Chops", 0), 10),
            Arguments.of(null, 10)
        );
    }

    @ParameterizedTest
    @MethodSource("attackPowerCases")
    public void attackPowerTest(Weapon w, int expectedBonus) {
        if (w != null) {
            player.equipWeapon(w);
        }
        assertEquals(expectedBonus, player.getAttackPower());
    }

    public static Stream<Arguments> defensePowerCases() {
        return Stream.of(
            Arguments.of(new Armor("Leather", "weakest", 0), 5),
            Arguments.of(new Armor("Diamond", "strongest", 20), 25),
            Arguments.of(null, 10)
        );
    }

    @ParameterizedTest
    @MethodSource("defensePowerCases")
    public void attackPowerTest(Armor a, int expectedBonus) {
        if (a != null) {
            player.equipArmor(a);
        }
        assertEquals(expectedBonus, player.getDefensePower());
    }
}
```

---

### Prompt 2.2 — Parameterized test with pre-built objects

> How can I test this when the objects are already set up?

```java
public static Stream<Arguments> presentFindCases() {
    return Stream.of(
        Arguments.of("diamond"),
        Arguments.of("Sword"),
        Arguments.of("Axe")
    );
}

@ParameterizedTest
@MethodSource("presentFindCases")
public void presentFindShouldReturnItem(__string__ s) {
    __assertEquals__(__)__
}
```

---

### Prompt 2.3 — InventoryTest review

> How are these tests for Inventory?

(Full InventoryTest.java provided — see git history for original.)

---

### Prompt 2.4 — RoomTest review

> How is this RoomTest?

(Full RoomTest.java provided — see git history for original.)

---

### Prompt 2.5 — MonsterTest review

> How are these tests?

(Full MonsterTest.java provided — see git history for original.)

---

### Prompt 2.6 — ItemTest review

> How are these tests?

(Full ItemTest.java provided — see git history for original.)

---

### Prompt 2.7 — QuestTest review

> How are these tests?

(Full QuestTest.java provided — see git history for original.)

---

### Prompt 2.8 — TrapTest review

> How is this?

(Full TrapTest.java provided — see git history for original.)

---

### Prompt 2.9 — InteractionEngineTest formatting

> What tests would you do for InteractionEngine? Make this look nicer.

(Full InteractionEngineTest.java provided.)

---

## 3. Repository Analysis & Coverage

### Prompt 3.1 — First-pass analysis

> https://github.com/CSouds/CS3330_GA3.git
>
> Check this git repo and find anything that is going to prevent this project having 100%?

### Prompt 3.2 — Repository re-analysis

> Check the repo again. Is there ANYTHING else besides the PIT stuff yet to be corrected that is preventing 100%?

### Prompt 3.3 — Submission readiness check

> Look at the repo now and find ANYTHING that makes it not submit ready.

---

## 4. Eclipse / Build Errors

### Prompt 4.1 — `Main` class not resolving

> I am getting this error in Main for all references to Main.

(Screenshot: `Main cannot be resolved` with quick-fix suggestions for `com.sun.tools.javac.Main` etc.)

### Prompt 4.2 — Missing build path option

> I only see Build Path → Configure Build Path

### Prompt 4.3 — JUnit 5 still missing after adding library

> I had Classpath and Modulepath, I selected Classpath in the steps.

### Prompt 4.4 — Maven re-import didn't help

> I reimported and same error.

### Prompt 4.5 — Malformed POM

(Screenshot: `MarkupNotRecognizedInMisc` — non-parseable POM error.)

### Prompt 4.6 — Constructor argument type error

> Why does this have an error when this is defined?
>
> ```java
> Room s = new Room(5, "Kitchen", "Where there is food");
> ```
>
> ```java
> public Room(String id, String name, String description) {
>     this.id = Objects.requireNonNull(id);
>     ...
> }
> ```

---

## 5. EclEmma Coverage Reports

### Prompt 5.1 — Confirming EclEmma is the right tool

> I got it to work. Is the coverage window with the green percentage bars EclEmma coverage?

### Prompt 5.2 — Filtering tests out of the coverage report

> `src/main/java` is still showing 100%. Is there a way I can give a report on that and not test?

---

## 6. PIT Mutation Testing

### Prompt 6.1 — PIT failing with `NoSuchMethodError`

(Pasted full PIT log showing JUnit Platform version conflict.)

### Prompt 6.2 — PIT failing with `Unsupported class file major version 65`

(Pasted full PIT log — Java 21 class files but PIT can only read up to Java 17.)

### Prompt 6.3 — Tests failing without mutation

(Pasted full PIT log showing 5 test failures preventing mutation analysis.)

### Prompt 6.4 — Tests failing without mutation, reduced

> I just pushed the new code.
>
> (Pasted PIT log showing 1 remaining test failure.)

### Prompt 6.5 — Static method source error

> What is the best way to fix that the method sources are not static?

(InventoryTest.java pasted with non-static `Stream<Arguments>` provider methods.)

---

## 7. Killing Surviving Mutants

### Prompt 7.1 — Initial mutant survivors

> Help me kill these surviving mutants.

(Screenshot: PITclipse view showing 6 SURVIVED mutants in DungeonFactory, Inventory, and Room.)

---

## 8. Reference: Earlier Assignment 2 Prompts

The following prompts are from a related earlier assignment and were referenced for technique. They are included here for completeness as required by the academic integrity policy.

### Prompt 8.1 — Project goal interpretation

> Can you tell me the goal of this assignment and what we start with?
>
> (Attachments: SP2026_CS3330_HW3.pdf, CS3330_HW3_HauntedUniversityBasement.zip)

### Prompt 8.2 — Identifying mutation survivors in Assignment 2

> https://github.com/CSouds/CS3330Assignment2 this repo shows code which is 98% for PIT mutations, what might the 2% be?

### Prompt 8.3 — Specific mutant analysis (Assignment 2)

> (Attachments: RewardUtil.java, QuestBoard.java)

---

## 9. Collaboration

### Prompt 9.1 — Splitting work with partner

> I am working with one partner. Split up the work perfectly even for us to collaborate.

---

*End of prompts log.*
