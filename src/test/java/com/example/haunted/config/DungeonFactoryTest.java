package com.example.haunted.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.haunted.engine.GameEngine;
import com.example.haunted.model.Direction;
import com.example.haunted.model.Room;
class DungeonFactoryTest 
{

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
}
