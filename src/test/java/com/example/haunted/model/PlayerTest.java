package com.example.haunted.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

public class PlayerTest {
	private Player player;
	
	@BeforeEach
    public void setUp() {
        player = new Player("Connor", 100, 10, 5, new Inventory(10));
        player.setCurrentRoom(new Room("start", "Start", "A room"));
    }
	
	@Test
	void shouldGetName()
	{
		assertEquals("Connor", player.getName());
	}
	
	@Test
	void shouldGetHealth()
	{
		assertEquals(100, player.getHealth());
	}
	
	@Test
	void shouldGetMaxHealth()
	{
		assertEquals(100, player.getMaxHealth());
	}
	
	@Test
	void shouldGetBaseAttack()
	{
		assertEquals(10, player.getBaseAttack());
	}
	
	@Test
	void shouldGetBaseDefense()
	{
		assertEquals(5, player.getBaseDefense());
	}
	
	@Test
	void shouldGetInventory()
	{
		assertNotNull(player.getInventory());
	    assertEquals(10, player.getInventory().getCapacity());
	}
	
	@Test
	void shouldEquipGetWeapon()
	{
		Weapon w = new Weapon("Sword", "Slices people", 15); // arrange
		player.equipWeapon(w);
		assertEquals(w, player.getEquippedWeapon()); // act, assert
	}
	
	@Test
	void shouldEquipGetArmor()
	{
		Armor a = new Armor("Chainmail", "Made of chains", 3); // arrange
		player.equipArmor(a);
		assertEquals(a, player.getEquippedArmor()); // act, assert
	}
	
	@Test
	void shouldSetGetRoom()
	{
		Room s = new Room("Room1", "Kitchen", "Where there is food"); // arrange
		player.setCurrentRoom(s);
		assertEquals(s, player.getCurrentRoom()); // act, assert
	}
	
	
	public static Stream<Arguments> damageCases()
	{
		return Stream.of(
				Arguments.of(10, 90),
				Arguments.of(20, 80),
				Arguments.of(100, 0), // edge case: equal to health
				Arguments.of(150, 0) // edge case: greater than health
				);
	}
	@ParameterizedTest
	@MethodSource("damageCases")
	public void shouldTakeDamage(int damage, int expectedHealth)
	{
		player.takeDamage(damage);
		assertEquals(expectedHealth, player.getHealth());
	}
	
	public static Stream<Arguments> healCases()
	{
		return Stream.of(
				Arguments.of(10, 10, 100), // heal back to max
				Arguments.of(20, 80, 100), // heal over max
				Arguments.of(120, 12, 12), // damage to floor, then heal back
				Arguments.of(50, 20, 70) // normal case
				);
	}
	@ParameterizedTest
	@MethodSource("healCases")
	public void shouldHeal(int damage, int health, int expectedHealth)
	{
		player.takeDamage(damage);
		player.heal(health);
		assertEquals(expectedHealth, player.getHealth());
	}

	public static Stream<Arguments> aliveCases()
	{
		return Stream.of(
				Arguments.of(100, false),
				Arguments.of(10, true),
				Arguments.of(99, true),
				Arguments.of(101, false)
				);
	}
	@ParameterizedTest
	@MethodSource("aliveCases")
	public void aliveTest(int damage, boolean expectedAlive)
	{
		player.takeDamage(damage);
		assertEquals(expectedAlive, player.isAlive());
	}
	
	public static Stream<Arguments> attackPowerCases()
	{
		return Stream.of(
				Arguments.of(new Weapon("Sword", "Slices", 10), 20),
				Arguments.of(new Weapon("Axe", "Chops", 0), 10),
				Arguments.of(null, 10)
				);
	}
	@ParameterizedTest
	@MethodSource("attackPowerCases")
	public void attackPowerTest(Weapon w, int expectedBonus)
	{
		if(w != null)
		{
			player.equipWeapon(w);
		}
		assertEquals(expectedBonus, player.getAttackPower());
	}
	
	public static Stream<Arguments> defensePowerCases()
	{
		return Stream.of(
				Arguments.of(new Armor("Leather", "weakest", 0), 5),
				Arguments.of(new Armor("Diamond", "strongest", 20), 25),
				Arguments.of(null, 5)
				);
	}
	@ParameterizedTest
	@MethodSource("defensePowerCases")
	public void attackPowerTest(Armor a, int expectedBonus)
	{
		if(a != null)
		{
			player.equipArmor(a);
		}
		assertEquals(expectedBonus, player.getDefensePower());
	}
	
	@Test
	void takeDamage_negative_doesNothing() {
	    Player p = new Player("Test", 100, 5, 5, new Inventory(5));
	    p.takeDamage(-10);
	    assertEquals(100, p.getHealth());
	}

	@Test
	void heal_negative_doesNothing() {
	    Player p = new Player("Test", 100, 5, 5, new Inventory(5));
	    p.takeDamage(20); // now 80
	    p.heal(-10);
	    assertEquals(80, p.getHealth());
	}
}
