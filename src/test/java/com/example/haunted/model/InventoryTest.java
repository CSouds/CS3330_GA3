package com.example.haunted.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

public class InventoryTest {
	private Inventory inventory;
	private Inventory fullInventory;
	private Inventory emptyInventory;
	
	@BeforeEach
	public void setUp()
	{
		 fullInventory = new Inventory(4);
		 fullInventory.addItem(new Weapon("Sword", "Slices", 5));
		 fullInventory.addItem(new Weapon("Axe", "Chops", 6));
		 fullInventory.addItem(new Weapon("Bow", "Shoots", 3));
		 fullInventory.addItem(new Armor("Diamond", "strongest", 20));
		 
		 emptyInventory = new Inventory(1);
		 
		 inventory = new Inventory(3);
		 inventory.addItem(new Weapon("Sword", "Slices", 5));
		 inventory.addItem(new Weapon("Axe", "Chops", 6));
		 inventory.addItem(new Weapon("Bow", "Shoots", 3));
	}
	
	private static Inventory createFullInventory() {
	    Inventory inv = new Inventory(4);
	    inv.addItem(new Weapon("Sword", "Slices", 5));
	    inv.addItem(new Weapon("Axe", "Chops", 6));
	    inv.addItem(new Weapon("Bow", "Shoots", 3));
	    inv.addItem(new Armor("Diamond", "strongest", 20));
	    return inv;
	}

	private static Inventory createInventory() {
	    Inventory inv = new Inventory(3);
	    inv.addItem(new Weapon("Sword", "Slices", 5));
	    inv.addItem(new Weapon("Axe", "Chops", 6));
	    inv.addItem(new Weapon("Bow", "Shoots", 3));
	    return inv;
	}

	private static Inventory createEmptyInventory() {
	    return new Inventory(1);
	}
	
	@Test
	public void fullAddShouldReturnFalse()
	{
		assertEquals(false, fullInventory.addItem(new Armor("gold", "weak", 1)));
	}
	
	@Test
	public void fullShouldNotAdd()
	{
		fullInventory.addItem(new Armor("gold", "weak", 1));
		assertTrue(fullInventory.findItem("gold").isEmpty());
	}
	
	@Test
	public void validAddShouldReturnTrue() 
	{
		assertEquals(true, emptyInventory.addItem(new Armor("gold", "weak", 1)));
	}
	
	@Test
	public void validShouldAdd() 
	{
		emptyInventory.addItem(new Armor("gold", "weak", 1));
		assertTrue(emptyInventory.findItem("gold").isPresent());
	}
	
	public static Stream<Arguments> missingFindCases()
	{
		return Stream.of(
				Arguments.of(createInventory()),
				Arguments.of(createEmptyInventory())
				);
	}
	@ParameterizedTest
	@MethodSource("missingFindCases")
	public void missingFindShouldReturnNull(Inventory i)
	{
		assertTrue(i.findItem("gold").isEmpty());
	}
	
	public static Stream<Arguments> presentFindCases()
	{
		return Stream.of(
				Arguments.of("diamond"),
				Arguments.of("Sword"),
				Arguments.of("Axe")
				);
	}
	@ParameterizedTest
	@MethodSource("presentFindCases")
	public void presentFindShouldReturnItem(String s)
	{
		assertTrue(inventory.findItem(s).isPresent());
	}
	
	public static Stream<Arguments> missingContainsCases()
	{
		return Stream.of(
				Arguments.of(createInventory()),
				Arguments.of(createEmptyInventory())
				);
	}
	@ParameterizedTest
	@MethodSource("missingContainsCases")
	public void missingContainsShouldReturnNull(Inventory i)
	{
		assertFalse(i.contains("gold"));
	}
	
	public static Stream<Arguments> presentContainsCases()
	{
		return Stream.of(
				Arguments.of("Bow"),
				Arguments.of("Sword"),
				Arguments.of("Axe")
				);
	}
	@ParameterizedTest
	@MethodSource("presentContainsCases")
	public void presentContainsShouldReturnItem(String s)
	{
		assertTrue(inventory.contains(s));
	}
	
	public static Stream<Arguments> removedItemsCases()
	{
		return Stream.of(
				Arguments.of("Axe"),
				Arguments.of("Sword"),
				Arguments.of("Bow")
				);
	}
	@ParameterizedTest
	@MethodSource("removedItemsCases")
	public void removedItemShouldNotBeFound(String s)
	{
		inventory.removeItem(s);
		assertFalse(inventory.contains(s));
	}
	
	public static Stream<Arguments> fullTestCases()
	{
		return Stream.of(
				Arguments.of(createFullInventory(), true),
				Arguments.of(createInventory(), false),
				Arguments.of(createEmptyInventory(), false)
				);
	}
	@ParameterizedTest
	@MethodSource("fullTestCases")
	public void isFullTest(Inventory i, boolean b)
	{
		assertEquals(b, i.isFull());
	}
	
	public static Stream<Arguments> capacityTestCases()
	{
		return Stream.of(
				Arguments.of(createFullInventory(), 4),
				Arguments.of(createInventory(), 3),
				Arguments.of(createEmptyInventory(), 1)
				);
	}
	@ParameterizedTest
	@MethodSource("capacityTestCases")
	public void capacityTest(Inventory i, int n)
	{
		assertEquals(n, i.getCapacity());
	}
	
	@Test
	public void getItemsTest() {
	    Armor a = new Armor("Netherite", "Actually strongest", 30);
	    inventory.addItem(a);
	    List<Item> l = inventory.getItems();
	    assertTrue(l.contains(a));
	}
}
