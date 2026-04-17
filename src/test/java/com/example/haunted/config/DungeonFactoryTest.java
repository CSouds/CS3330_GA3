package com.example.haunted.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.haunted.engine.GameEngine;
import com.example.haunted.model.Armor;
import com.example.haunted.model.BossMonster;
import com.example.haunted.model.Direction;
import com.example.haunted.model.Monster;
import com.example.haunted.model.Potion;
import com.example.haunted.model.Room;
import com.example.haunted.model.Trap;
import com.example.haunted.model.TrapType;
import com.example.haunted.model.Weapon;
class DungeonFactoryTest 
{

	@Test
	void createGame_setsPlayerStatsCorrectly() {
	    GameEngine engine = DungeonFactory.createGame();

	    assertEquals("Student Explorer", engine.getPlayer().getName());
	    assertEquals(50, engine.getPlayer().getHealth());
	    assertEquals(50, engine.getPlayer().getMaxHealth());
	    assertEquals(7, engine.getPlayer().getBaseAttack());
	    assertEquals(2, engine.getPlayer().getBaseDefense());
	}
	
	@Test
	void playerStartsInStairwell()
	{
		GameEngine engine = DungeonFactory.createGame();
		
		assertEquals("stairwell", engine.getCurrentRoom().getId());
		assertEquals("Maintenance Stairwell", engine.getCurrentRoom().getName());
	}
	
	@Test
	void stairwellConnectsEastToLectureHall()
	{
		GameEngine engine = DungeonFactory.createGame();
		
		Room eastRoom = engine.getCurrentRoom().getExit(Direction.EAST);
		
		assertNotNull(eastRoom);
		assertEquals("lectureHall", eastRoom.getId());
	}

	@Test
	void examArchiveStartsLockedWithArchiveKey()
	{
		GameEngine engine = DungeonFactory.createGame();
		
		Room lectureHall = engine.getCurrentRoom().getExit(Direction.EAST);
		Room examArchive = lectureHall.getExit(Direction.NORTH);
		
		assertTrue(examArchive.isLocked());
		assertEquals("Archive Key", examArchive.getRequiredKeyName());
	}
	
	@Test
	void brokenElevatorHasElectricTrapForEightDamage()
	{
		GameEngine engine = DungeonFactory.createGame();

	    Room lectureHall = engine.getCurrentRoom().getExit(Direction.EAST);
	    Room brokenElevator = lectureHall.getExit(Direction.SOUTH);

	    assertNotNull(brokenElevator.getTrap());
	    assertEquals("Loose Wires Trap", brokenElevator.getTrap().getName());
	    assertEquals(8, brokenElevator.getTrap().getDamage());
	}
	@Test
	void brokenElevatorTrapFullyConfigured() {
	    GameEngine engine = DungeonFactory.createGame();

	    Room lectureHall = engine.getCurrentRoom().getExit(Direction.EAST);
	    Room brokenElevator = lectureHall.getExit(Direction.SOUTH);

	    Trap trap = brokenElevator.getTrap();

	    assertNotNull(trap);
	    assertEquals("Loose Wires Trap", trap.getName());
	    assertEquals(TrapType.ELECTRIC, trap.getType());
	    assertEquals(8, trap.getDamage());
	    assertTrue(trap.isArmed());
	    assertTrue(trap.isOneTimeTrigger());
	}
	@Test
	void finalChamberStartsLockedAndContainsFinalBoss()
	{
		GameEngine engine = DungeonFactory.createGame();
		
		Room lectureHall = engine.getCurrentRoom().getExit(Direction.EAST);
		Room examArchive = lectureHall.getExit(Direction.NORTH);
		Room deanVault = examArchive.getExit(Direction.EAST);
		Room finalChamber = deanVault.getExit(Direction.NORTH);
		
		assertTrue(finalChamber.isLocked());
		assertEquals("Vault Key", finalChamber.getRequiredKeyName());
		
		assertTrue(finalChamber.findMonster("Final Exam Phantom").isPresent());
	}
	
	@Test
	void createGame_placesItemsCorrectly() {
	    GameEngine engine = DungeonFactory.createGame();

	    Room stairwell = engine.getCurrentRoom();
	    Room lectureHall = stairwell.getExit(Direction.EAST);
	    Room labStorage = lectureHall.getExit(Direction.EAST);
	    Room serverCloset = labStorage.getExit(Direction.NORTH);
	    Room examArchive = lectureHall.getExit(Direction.NORTH);
	    Room deanVault = examArchive.getExit(Direction.EAST);

	    assertTrue(lectureHall.findItem("Coffee Potion").isPresent());
	    assertTrue(labStorage.findItem("Archive Key").isPresent());
	    assertTrue(labStorage.findItem("Calculator Shield").isPresent());
	    assertTrue(serverCloset.findItem("Stapler of Justice").isPresent());
	    assertTrue(examArchive.findItem("Lost Gradebook").isPresent());
	    assertTrue(deanVault.findItem("Vault Key").isPresent());
	}
	
	@Test
	void createGame_itemStatsCorrect() {
	    GameEngine engine = DungeonFactory.createGame();

	    Room stairwell = engine.getCurrentRoom();
	    Room lectureHall = stairwell.getExit(Direction.EAST);
	    Room labStorage = lectureHall.getExit(Direction.EAST);
	    Room serverCloset = labStorage.getExit(Direction.NORTH);

	    Potion coffee = (Potion) lectureHall.findItem("Coffee Potion").orElseThrow();
	    Armor shield = (Armor) labStorage.findItem("Calculator Shield").orElseThrow();
	    Weapon stapler = (Weapon) serverCloset.findItem("Stapler of Justice").orElseThrow();

	    assertEquals(15, coffee.getHealingAmount());
	    assertEquals(3, shield.getDefenseBonus());
	    assertEquals(4, stapler.getAttackBonus());
	}
	
	@Test
	void createGame_placesMonstersCorrectly() {
	    GameEngine engine = DungeonFactory.createGame();

	    Room stairwell = engine.getCurrentRoom();
	    Room lectureHall = stairwell.getExit(Direction.EAST);
	    Room labStorage = lectureHall.getExit(Direction.EAST);
	    Room serverCloset = labStorage.getExit(Direction.NORTH);
	    Room examArchive = lectureHall.getExit(Direction.NORTH);
	    Room deanVault = examArchive.getExit(Direction.EAST);
	    Room finalChamber = deanVault.getExit(Direction.NORTH);

	    assertTrue(lectureHall.findMonster("Sleep-Deprived TA").isPresent());
	    assertTrue(serverCloset.findMonster("Spreadsheet Golem").isPresent());
	    assertTrue(examArchive.findMonster("Plagiarism Ghost").isPresent());
	    assertTrue(deanVault.findMonster("Registrar Wraith").isPresent());
	    assertTrue(finalChamber.findMonster("Final Exam Phantom").isPresent());
	}
	
	@Test
	void createGame_monsterStatsCorrect() {
	    GameEngine engine = DungeonFactory.createGame();

	    Room stairwell = engine.getCurrentRoom();
	    Room lectureHall = stairwell.getExit(Direction.EAST);
	    Room labStorage = lectureHall.getExit(Direction.EAST);
	    Room serverCloset = labStorage.getExit(Direction.NORTH);
	    Room examArchive = lectureHall.getExit(Direction.NORTH);
	    Room deanVault = examArchive.getExit(Direction.EAST);
	    Room finalChamber = deanVault.getExit(Direction.NORTH);

	    Monster ta = lectureHall.findMonster("Sleep-Deprived TA").orElseThrow();
	    Monster golem = serverCloset.findMonster("Spreadsheet Golem").orElseThrow();
	    Monster ghost = examArchive.findMonster("Plagiarism Ghost").orElseThrow();
	    Monster wraith = deanVault.findMonster("Registrar Wraith").orElseThrow();
	    Monster boss = finalChamber.findMonster("Final Exam Phantom").orElseThrow();

	    assertEquals(18, ta.getMaxHealth());
	    assertEquals(6, ta.getAttack());
	    assertEquals(1, ta.getDefense());

	    assertEquals(28, golem.getMaxHealth());
	    assertEquals(7, golem.getAttack());
	    assertEquals(4, golem.getDefense());

	    assertEquals(22, ghost.getMaxHealth());
	    assertEquals(8, ghost.getAttack());
	    assertEquals(2, ghost.getDefense());

	    assertEquals(30, wraith.getMaxHealth());
	    assertEquals(9, wraith.getAttack());
	    assertEquals(3, wraith.getDefense());

	    assertInstanceOf(BossMonster.class, boss);
	    assertEquals(40, boss.getMaxHealth());
	}
	
	@Test
	void createGame_monsterLootCorrect() {
	    GameEngine engine = DungeonFactory.createGame();

	    Room stairwell = engine.getCurrentRoom();
	    Room lectureHall = stairwell.getExit(Direction.EAST);
	    Room labStorage = lectureHall.getExit(Direction.EAST);
	    Room serverCloset = labStorage.getExit(Direction.NORTH);
	    Room examArchive = lectureHall.getExit(Direction.NORTH);
	    Room deanVault = examArchive.getExit(Direction.EAST);

	    Monster ta = lectureHall.findMonster("Sleep-Deprived TA").orElseThrow();
	    Monster golem = serverCloset.findMonster("Spreadsheet Golem").orElseThrow();
	    Monster wraith = deanVault.findMonster("Registrar Wraith").orElseThrow();

	    assertEquals("Coffee Potion", ta.getLoot().get(0).getName());
	    assertEquals("Dry Erase Sword", golem.getLoot().get(0).getName());
	    assertEquals("Graduation Gown Armor", wraith.getLoot().get(0).getName());
	}
	
	@Test
	void createGame_connectsAllRoomsCorrectly() {
	    GameEngine engine = DungeonFactory.createGame();

	    Room stairwell = engine.getCurrentRoom();
	    Room lectureHall = stairwell.getExit(Direction.EAST);
	    Room labStorage = lectureHall.getExit(Direction.EAST);
	    Room brokenElevator = lectureHall.getExit(Direction.SOUTH);
	    Room examArchive = lectureHall.getExit(Direction.NORTH);
	    Room serverCloset = labStorage.getExit(Direction.NORTH);
	    Room deanVault = examArchive.getExit(Direction.EAST);
	    Room finalChamber = deanVault.getExit(Direction.NORTH);

	    assertEquals("stairwell", stairwell.getId());
	    assertEquals("lectureHall", lectureHall.getId());
	    assertEquals("labStorage", labStorage.getId());
	    assertEquals("brokenElevator", brokenElevator.getId());
	    assertEquals("examArchive", examArchive.getId());
	    assertEquals("serverCloset", serverCloset.getId());
	    assertEquals("deanVault", deanVault.getId());
	    assertEquals("finalChamber", finalChamber.getId());

	    // reverse connections
	    assertSame(stairwell, lectureHall.getExit(Direction.WEST));
	    assertSame(lectureHall, labStorage.getExit(Direction.WEST));
	    assertSame(lectureHall, brokenElevator.getExit(Direction.NORTH));
	    assertSame(labStorage, serverCloset.getExit(Direction.SOUTH));
	    assertSame(lectureHall, examArchive.getExit(Direction.SOUTH));
	    assertSame(examArchive, deanVault.getExit(Direction.WEST));
	    assertSame(serverCloset, deanVault.getExit(Direction.SOUTH));
	    assertSame(deanVault, finalChamber.getExit(Direction.SOUTH));
	}
}
