package com.example.haunted.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class RoomTest {
	private Room room;
	private Room room2;
	private Room room3;
	
	@BeforeEach
	public void setup()
	{
		room = new Room("Room1", "Kitchen", "Has food");
		room2 = new Room("Room2", "Dining Room", "Where we eat");
		room3 = new Room("Room3", "Living Room", "Where we hang out");
		room.connect(Direction.SOUTH, room2);
		room.setLocked(true, "Master Key");
	}
	
	public static List<Item> createLoot()
	{
		List<Item> loot = new ArrayList<>();
		loot.add(new Armor("Iron", "Medium", 7));
		loot.add(new Weapon("Spear", "Pointy", 15));
		return loot;
	}
	
	@Test
	public void shouldGetId()
	{
		assertEquals("Room1", room.getId());
	}

	@Test
	public void shouldGetName()
	{
		assertEquals("Kitchen", room.getName());
	}
	
	@Test
	public void shouldGetDescription()
	{
		assertEquals("Has food", room.getDescription());
	}
	
	public static Stream<Arguments> roomConnectCases()
	{
		return Stream.of(
				Arguments.of(Direction.NORTH),
				Arguments.of(Direction.WEST),
				Arguments.of(Direction.SOUTH),
				Arguments.of(Direction.EAST)
				);
	}
	@ParameterizedTest
	@MethodSource("roomConnectCases")
	public void connectShouldConnectRoom(Direction d)
	{
		room2.connect(d, room3);
		
		assertEquals(room3, room2.getExit(d));
	}
	
	@Test
	public void getExitShouldReturnRoom()
	{
		assertEquals(room2, room.getExit(Direction.SOUTH));
	}
	
	@Test
	public void getExitShouldBeEmpty()
	{
		assertNull(room3.getExit(Direction.NORTH));
	}
	
	@Test
	public void getExitsShouldBeFilled()
	{
		room.connect(Direction.EAST, room3);
		Room room4 = new Room("Room4", "Bathroom", "Has toilet");
		room.connect(Direction.WEST, room4);
		Room room5 = new Room("Room5", "Bedroom", "For sleeping");
		room.connect(Direction.NORTH, room5);
		
		Map<Direction, Room> m = room.getExits();
		
		assertEquals(room5, m.get(Direction.NORTH));
		assertEquals(room4, m.get(Direction.WEST));
		assertEquals(room3, m.get(Direction.EAST));
		assertEquals(room2, m.get(Direction.SOUTH));
	}
	
	@Test
	public void	getExitsShouldBeEmpty()
	{
		Map<Direction, Room> m = room3.getExits();
		
		assertEquals(null, m.get(Direction.NORTH));
		assertEquals(null, m.get(Direction.WEST));
		assertEquals(null, m.get(Direction.EAST));
		assertEquals(null, m.get(Direction.SOUTH));
	}
	
	public static Stream<Arguments> addItemCases()
	{
		return Stream.of(
				Arguments.of(new Weapon("Sword", "Slices", 5)),
				Arguments.of(new Weapon("Axe", "Chops", 6)),
				Arguments.of(new Weapon("Bow", "Shoots", 3))				
				);
	}
	@ParameterizedTest
	@MethodSource("addItemCases")
	public void itemShouldBeAdded(Item i)
	{
		room.addItem(i);
		
		assertTrue(room.getItems().contains(i));
	}

	
	public static Stream<Arguments> removeValidItemCases()
	{
		return Stream.of(
				Arguments.of(new Weapon("Sword", "Slices", 5)),
				Arguments.of(new Weapon("Axe", "Chops", 6)),
				Arguments.of(new Weapon("Bow", "Shoots", 3))				
				);
	}
	@ParameterizedTest
	@MethodSource("removeValidItemCases")
	public void itemShouldBeRemoved(Item i)
	{
		String s = i.getName();
		
		room.addItem(new Weapon("Sword", "Slices", 5));
		room.addItem(new Weapon("Axe", "Chops", 6));
		room.addItem(new Weapon("Bow", "Shoots", 3));
		
		room.removeItemByName(s);
		
		assertTrue(room.findItem(s).isEmpty());
	}
	
	@ParameterizedTest
	@MethodSource("removeValidItemCases")
	public void itemRemovedShouldReturn(Item i)
	{
		String s = i.getName();
		
		room.addItem(new Weapon("Sword", "Slices", 5));
		room.addItem(new Weapon("Axe", "Chops", 6));
		room.addItem(new Weapon("Bow", "Shoots", 3));
		
		Item removed = room.removeItemByName(s);
		assertEquals(s, removed.getName());
	}
	
	@Test
	public void itemShouldNotBeRemoved()
	{
		room.addItem(new Weapon("Sword", "Slices", 5));
		room.addItem(new Weapon("Axe", "Chops", 6));
		room.addItem(new Weapon("Bow", "Shoots", 3));
		
		assertNull(room.removeItemByName("Blank"));
	}
	
	public static Stream<Arguments> findValidItemCases()
	{
		return Stream.of(
				Arguments.of("Sword"),
				Arguments.of("Axe"),
				Arguments.of("Bow")				
				);
	}
	@ParameterizedTest
	@MethodSource("findValidItemCases")
	public void itemShouldBeFound(String s)
	{
		room.addItem(new Weapon("Sword", "Slices", 5));
		room.addItem(new Weapon("Axe", "Chops", 6));
		room.addItem(new Weapon("Bow", "Shoots", 3));
		
		assertTrue(room.findItem(s).isPresent());
	}
	
	@Test
	public void invalidShouldNotBeFound()
	{
		assertTrue(room.findItem("Ghost item").isEmpty());
	}
	
	@Test
	public void itemsShouldBeReturned()
	{		
		room.addItem(new Weapon("Sword", "Slices", 5));
		room.addItem(new Weapon("Axe", "Chops", 6));
		room.addItem(new Weapon("Bow", "Shoots", 3));
		
		assertEquals(3, room.getItems().size());
	}
	
	@Test
	public void itemsShouldNotBeReturned()
	{
		assertEquals(0, room.getItems().size());
	}
	
	public static Stream<Arguments> validMonsterAddCases()
	{
		return Stream.of(
				Arguments.of(new Monster("Creeper", 20, 15, 2, createLoot())),
				Arguments.of(new Monster("Enderman", 40, 20, 5, createLoot()))
				);
	}
	@ParameterizedTest
	@MethodSource("validMonsterAddCases")
	public void shouldAddMonster(Monster m)
	{
		room.addMonster(m);
		
		assertTrue(room.findMonster(m.getName()).isPresent());
	}
	
	public static Stream<Arguments> validMonsterFindCases()
	{
		return Stream.of(
				Arguments.of("Creeper"),
				Arguments.of("Enderman")
				);
	}
	@ParameterizedTest
	@MethodSource("validMonsterFindCases")
	public void shouldFindMonster(String s)
	{
		room.addMonster(new Monster("Creeper", 20, 15, 2, createLoot()));
		room.addMonster(new Monster("Enderman", 40, 20, 5, createLoot()));
		
		assertTrue(room.findMonster(s).isPresent());
	}
	
	@Test
	public void shouldNotFindMonster()
	{
		room.addMonster(new Monster("Creeper", 20, 15, 2, createLoot()));
		room.addMonster(new Monster("Enderman", 40, 20, 5, createLoot()));
		
		assertTrue(room.findMonster("").isEmpty());
	}
	
	@Test
	public void shouldReturnMonsters()
	{
		room.addMonster(new Monster("Creeper", 20, 15, 2, createLoot()));
		room.addMonster(new Monster("Enderman", 40, 20, 5, createLoot()));
		
		assertTrue(room.getMonsters().size() == 2);
	}
	
	@Test
	public void shouldReturnEmpty()
	{
		assertTrue(room.getMonsters().size() == 0);
	}
	
	@Test
	public void shouldHaveAlive()
	{
		room.addMonster(new Monster("Creeper", 20, 15, 2, createLoot()));
		room.addMonster(new Monster("Enderman", 40, 20, 5, createLoot()));
		
		assertTrue(room.hasLivingMonsters());
	}
	
	@Test
	public void shouldNotHaveAlive()
	{
		room.addMonster(new Monster("Creeper", 20, 15, 2, createLoot()));
		room.addMonster(new Monster("Enderman", 40, 20, 5, createLoot()));
		
		for(Monster m : room.getMonsters())
		{
			m.takeDamage(1000);
		}
		
		assertFalse(room.hasLivingMonsters());
	}

	@Test
	public void shouldBeLocked()
	{
		room.unlock(room.getRequiredKeyName());
		room.setLocked(true, room.getRequiredKeyName());
		assertEquals(true, room.isLocked());
	}
	
	@Test
	public void shouldUnlock()
	{
		room.setLocked(true, room.getRequiredKeyName());
		room.unlock(room.getRequiredKeyName());
		assertEquals(false, room.isLocked());
	}
	
	@Test
	public void shouldNotUnlock()
	{
		room.setLocked(true, "Master Key");
		room.unlock("nope");
		assertTrue(room.isLocked());
	}
	
	@Test
	public void alreadyUnlocked()
	{
		room.setLocked(false, room.getRequiredKeyName());
		room.unlock("Master Key");
		assertEquals(false, room.isLocked());
	}
	
	@Test
	public void shouldReturnNewKeyName()
	{
		room.setLocked(true, "password");
		assertEquals("password", room.getRequiredKeyName());
	}
	
	@Test
	public void shouldGetTrap()
	{
		Trap t = new Trap("spikes", TrapType.ELECTRIC, 20, true, false);
		room.setTrap(t);
		
		assertEquals(t, room.getTrap());
	}
	
	@Test
	public void shouldChangeTrap()
	{
		Trap t = new Trap("spikes", TrapType.ELECTRIC, 20, true, false);
		room.setTrap(t);
		room.setTrap(new Trap("poison", TrapType.STEAM, 10, false, true));
		
		assertNotEquals(t, room.getTrap());
	}
	@Test
	void findItem_caseInsensitive() {
	    Room room = new Room("id", "name", "desc");
	    room.addItem(new Weapon("Sword", "desc", 10));

	    assertTrue(room.findItem("sword").isPresent());
	    assertTrue(room.findItem("SWORD").isPresent());
	}

	@Test
	void removeItem_caseInsensitive() {
	    Room room = new Room("id", "name", "desc");
	    room.addItem(new Weapon("Sword", "desc", 10));

	    Item removed = room.removeItemByName("sword");
	    assertNotNull(removed);

	    assertTrue(room.findItem("Sword").isEmpty());
	}
}
