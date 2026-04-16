package com.example.haunted.rules;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.example.haunted.events.InteractionResult;
import com.example.haunted.model.Inventory;
import com.example.haunted.model.Player;
import com.example.haunted.model.Trap;
import com.example.haunted.model.TrapType;

class TrapResolverTest {
	@Test
	void nullTrapReturnsFailureAndNoDamage()
	{
		TrapResolver resolver = new TrapResolver();
		Player player = new Player("Hero", 50, 5, 2, new Inventory(5));
		
		InteractionResult result = resolver.resolveTrap(player, null);
		
		assertFalse(result.isSuccess());
		assertEquals("No trap was triggered.", result.getMessage());
		assertEquals(50,player.getHealth());
	}
	
	@Test
	void disarmedTrapReturnsFailureAndNoDamage()
	{
		TrapResolver resolver = new TrapResolver();
		Player player = new Player("Hero", 50, 5, 2, new Inventory(5));
		
		Trap trap = new Trap("Loose Wires", TrapType.ELECTRIC, 8, false, true);
		
		InteractionResult result = resolver.resolveTrap(player, trap);
		
		assertFalse(result.isSuccess());
		assertEquals("No trap was triggered.", result.getMessage());
		assertEquals(50, player.getHealth());
	}
	
	@Test
	void oneTimeTrapDamagesPlayerAndDisarmsTrap()
	{
		TrapResolver resolver = new TrapResolver();
		Player player = new Player("Hero", 50, 5, 2, new Inventory(5));
		
		Trap trap = new Trap("Loose Wires", TrapType.ELECTRIC, 8, true, true);
		
		InteractionResult result = resolver.resolveTrap(player, trap);
		
		assertTrue(result.isSuccess());
		assertEquals(42, player.getHealth());
		assertFalse(trap.isArmed());
		assertEquals("Trap 'Loose Wires' triggered for 8 damage.", result.getMessage());
	}
	
	@Test
	void reuseableTrapStaysArmedAfterTrigger()
	{
		TrapResolver resolver = new TrapResolver();
		Player player = new Player("Hero", 50, 5, 2, new Inventory(5));
		
		Trap trap = new Trap("Steam Burst", TrapType.STEAM, 5, true, false);
		
		InteractionResult result = resolver.resolveTrap(player, trap);
		
		assertTrue(result.isSuccess());
		assertEquals(45,player.getHealth());
		assertTrue(trap.isArmed());
		
	}
}