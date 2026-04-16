package com.example.haunted.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MonsterTest {
	private Monster m;
	
	public static List<Item> createLoot()
	{
		List<Item> loot = new ArrayList<>();
		loot.add(new Armor("Iron", "Medium", 7));
		loot.add(new Weapon("Spear", "Pointy", 15));
		return loot;
	}
	
	@BeforeEach
	void setUp()
	{
		m = new Monster("Creeper", 20, 15, 2, createLoot());
	}
	
	
	@Test
	public void shouldGetName()
	{
		assertEquals("Creeper", m.getName());
	}
	
	public static Stream<Arguments> healthDamageAmountCases()
	{
		return Stream.of(
				Arguments.of(0, 20),
				Arguments.of(10, 10),
				Arguments.of(20, 0),
				Arguments.of(21, 0)
				);
	}
	@ParameterizedTest
	@MethodSource("healthDamageAmountCases")
	public void damageShouldUpdateHealth(int damage, int expectedHealth)
	{
		m.takeDamage(damage);
		assertEquals(expectedHealth, m.getHealth());
	}
	
	public static Stream<Arguments> maxHealthDamageCases()
	{
		return Stream.of(
				Arguments.of(0, 20),
				Arguments.of(10, 20),
				Arguments.of(20, 20),
				Arguments.of(21, 20)
				);
	}
	@ParameterizedTest
	@MethodSource("maxHealthDamageCases")
	public void shouldGetMaxHealth(int damage, int expectedHealth)
	{
		m.takeDamage(damage);
		assertEquals(expectedHealth, m.getMaxHealth());
	}
	
	@Test
	public void shouldGetAttack()
	{
		assertEquals(15, m.getAttack());
	}

	@Test
	public void shouldGetDefense()
	{
		assertEquals(2, m.getDefense());
	}
	
	public static Stream<Arguments> isAliveCases()
	{
		return Stream.of(
				Arguments.of(0, true),
				Arguments.of(10, true),
				Arguments.of(19, true),
				Arguments.of(20, false),
				Arguments.of(21, false)
				);
	}
	@ParameterizedTest
	@MethodSource("isAliveCases")
	public void isAliveTests(int damage, boolean shouldBeAlive)
	{
		m.takeDamage(damage);
		assertEquals(shouldBeAlive, m.isAlive());
	}
	
	@Test
	public void shouldReturnLoot()
	{
		assertEquals(2, m.getLoot().size());
	}
	
	public static Stream<Arguments> rageAttackCases()
	{
		return Stream.of(
				Arguments.of(0, 15),
				Arguments.of(0, 15),
				Arguments.of(10, 30),
				Arguments.of(11, 30),
				Arguments.of(20, 30)
				);
	}
	@ParameterizedTest
	@MethodSource("rageAttackCases")
	public void rageAttackTest(int damage, int expected)
	{
		BossMonster b = new BossMonster("Creeper", 20, 15, 2, createLoot(), 15);
		b.takeDamage(damage);
		assertEquals(expected, b.getCurrentAttack());
	}
}
