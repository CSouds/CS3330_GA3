package com.example.haunted.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

public class TrapTest {
	private Trap t;
	
	@BeforeEach
	public void setUp()
	{
		t = new Trap("spikes", TrapType.ELECTRIC, 20, true, true);
	}
	
	@Test
	public void shouldGetName()
	{
		assertEquals("spikes", t.getName());
	}
	
	@Test
	public void shouldGetType()
	{
		assertEquals(TrapType.ELECTRIC, t.getType());
	}
	
	public static Stream<Arguments> trapDamageCases()
	{
		return Stream.of(
				Arguments.of(new Trap("spikes", TrapType.ELECTRIC, 20, true, false), 20),
				Arguments.of(new Trap("spikes", TrapType.ELECTRIC, 2, true, false), 2),
				Arguments.of(new Trap("spikes", TrapType.ELECTRIC, 0, true, false), 0)
				);
	}
	@ParameterizedTest
	@MethodSource("trapDamageCases")
	public void shouldGetDamage(Trap trap, int expectedDamage)
	{
		assertEquals(expectedDamage, trap.getDamage());
	}
	
	@Test
	public void shouldNotBeArmed()
	{
		Trap trap = new Trap("spikes", TrapType.ELECTRIC, 0, false, false);
		assertFalse(trap.isArmed());
	}
	
	@Test
	public void shouldBeArmed()
	{
		assertTrue(t.isArmed());
	}
	
	@Test
	public void shouldNotBeOne()
	{
		Trap trap = new Trap("spikes", TrapType.ELECTRIC, 0, false, false);
		assertFalse(trap.isOneTimeTrigger());
	}
	
	@Test
	public void shouldBeOne()
	{
		assertTrue(t.isOneTimeTrigger());
	}
	
	@Test
	public void shouldDisarm()
	{
		t.disarm();
		assertFalse(t.isArmed());
	}
	
	@Test
	public void disarmAlreadyDisarmedStaysDisarmed()
	{
	    Trap trap = new Trap("spikes", TrapType.ELECTRIC, 10, false, false);
	    trap.disarm();
	    assertFalse(trap.isArmed());
	}
	
	@Test
	public void persistentTrapDisarmNotCalledStaysArmed()
	{
	    Trap trap = new Trap("gas", TrapType.STEAM, 10, true, false);
	    assertFalse(trap.isOneTimeTrigger());
	    assertTrue(trap.isArmed());
	}
	
	@Test
	public void trapTypeValuesExist()
	{
	    assertNotNull(TrapType.valueOf("ELECTRIC"));
	    assertNotNull(TrapType.valueOf("STEAM"));
	}
}
