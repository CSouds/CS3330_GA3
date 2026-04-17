package com.example.haunted.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

public class ItemTest {
	private Armor a;
	private Weapon w;
	private Potion p;
	
	@BeforeEach
	public void setUp()
	{
		a = new Armor("Iron", "Medium", 7);
		w = new Weapon("Spear", "Pointy", 15);
		p = new Potion("Big", "Heals full health", 10);
	}
	
	public static Stream<Arguments> nameCases()
	{
	    return Stream.of(
	        Arguments.of(new Armor("Iron", "Medium", 7), "Iron"),
	        Arguments.of(new Weapon("Spear", "Pointy", 15), "Spear"),
	        Arguments.of(new Potion("Big", "Heals", 10), "Big")
	    );
	}
	@ParameterizedTest
	@MethodSource("nameCases")
	public void shouldGetName(Item item, String expectedName)
	{
	    assertEquals(expectedName, item.getName());
	}
	
	@Test
	public void shouldGetDescription()
	{
		assertEquals("Pointy", w.getDescription());
	}
	
	@Test
	public void shouldToString()
	{
		assertEquals("Big", p.toString());
	}
	
	@Test
	public void shouldGetAttackBonus()
	{
		assertEquals(15, w.getAttackBonus());
	}
	
	@Test
	public void shouldGetDefenseBonus()
	{
		assertEquals(7, a.getDefenseBonus());
	}
	
	@Test
	public void shouldGetHealingAmount()
	{
		assertEquals(10, p.getHealingAmount());
	}
	
	public static Stream<Arguments> potionHealingCases()
	{
		return Stream.of(
				Arguments.of(new Potion("Big", "Heals full health", 100), 100),
				Arguments.of(new Potion("Medium", "Heals half health", 50), 100),
				Arguments.of(new Potion("Small", "Heals 20", 20), 70),
				Arguments.of(new Potion("Fake", "Heals nothing", 0), 50)
				);
	}
	@ParameterizedTest
	@MethodSource("potionHealingCases")
	public void testPotionHeal(Potion p, int expectedHealth)
	{
		Player player = new Player("Connor", 100, 10, 5, new Inventory(10));
		player.takeDamage(50);
		p.use(player);
		assertEquals(expectedHealth, player.getHealth());
	}
	
	@Test
	public void shouldCreateKey()
	{
	    Key k = new Key("Gold Key", "Opens the vault");
	    assertEquals("Gold Key", k.getName());
	    assertEquals("Opens the vault", k.getDescription());
	}

	@Test
	public void shouldCreateQuestItem()
	{
	    QuestItem qi = new QuestItem("Lost Gradebook", "The legendary tome");
	    assertEquals("Lost Gradebook", qi.getName());
	}
	
	
	// ai generated for mutation killing last 2%
	@Test
    void inventoryRemoveItemReturnsRemovedItemWhenPresent() {
        // Kills: NullReturnValsMutator on Inventory.removeItem (item present path)
        Inventory inv = new Inventory(5);
        Weapon sword = new Weapon("Sword", "Slices", 5);
        inv.addItem(sword);
 
        Item returned = inv.removeItem("Sword");
 
        assertNotNull(returned, "removeItem should return the removed item, not null");
        assertSame(sword, returned, "removeItem should return the EXACT item that was in the inventory");
        assertEquals("Sword", returned.getName());
    }
 
    @Test
    void inventoryRemoveItemReturnsNullWhenItemMissing() {
        // Kills: any return-value mutant on the empty Optional path
        Inventory inv = new Inventory(5);
        inv.addItem(new Weapon("Sword", "Slices", 5));
 
        Item returned = inv.removeItem("Bow");
 
        assertNull(returned, "removeItem should return null when item is not in inventory");
        // Inventory should be unchanged
        assertTrue(inv.contains("Sword"));
        assertEquals(1, inv.getItems().size());
    }
 
    @Test
    void inventoryRemoveItemActuallyRemovesItemFromList() {
        // Kills: VoidMethodCallMutator on items::remove in ifPresent
        // (if remove call is removed, item stays in the list)
        Inventory inv = new Inventory(5);
        inv.addItem(new Weapon("Sword", "Slices", 5));
        inv.addItem(new Weapon("Axe", "Chops", 6));
 
        Item returned = inv.removeItem("Sword");
 
        assertNotNull(returned);
        assertEquals(1, inv.getItems().size(), "Inventory size should drop after remove");
        assertFalse(inv.contains("Sword"));
        assertTrue(inv.contains("Axe"));
    }
 
    // ============================================================
    //  Room.removeItemByName - return value mutants
    //  Same pattern as Inventory.removeItem. Plus this is a chance to
    //  pin down that the EXACT instance is returned, killing
    //  NullReturnValsMutator and EmptyObjectReturnValsMutator.
    // ============================================================
 
    @Test
    void roomRemoveItemByNameReturnsRemovedItem() {
        // Kills: NullReturnValsMutator on Room.removeItemByName (present path)
        Room room = new Room("r", "Room", "desc");
        Potion potion = new Potion("Coffee Potion", "warm", 15);
        room.addItem(potion);
 
        Item returned = room.removeItemByName("Coffee Potion");
 
        assertNotNull(returned, "Should return the removed item");
        assertSame(potion, returned, "Should return the EXACT instance");
    }
}
