package com.example.haunted.rules;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.example.haunted.model.BossMonster;
import com.example.haunted.model.Inventory;
import com.example.haunted.model.Monster;
import com.example.haunted.model.Player;

class DamageCalculatorTest {

    @ParameterizedTest
    @CsvSource({
        "10, 3, 7",
        "5, 5, 1",
        "4, 10, 1"
    })
    void playerDamageCalculations(int attack, int defense, int expectedDamage) {
        DamageCalculator calc = new DamageCalculator();
        Player player = new Player("Hero", 50, attack, 2, new Inventory(5));
        Monster monster = new Monster("Ghost", 20, 5, defense, List.of());

        int damage = calc.calculatePlayerDamage(player, monster);

        assertEquals(expectedDamage, damage);
    }

	
	@Test
	void normalMonsterUsesBaseAttack()
	{
		DamageCalculator calc = new DamageCalculator();
		Player player = new Player("Hero", 50, 5, 4, new Inventory(5));
		Monster monster = new Monster("Ghost", 20, 8, 2, List.of());
		
		int damage = calc.calculateMonsterDamage(monster,player);
		
		assertEquals(4, damage);
	}
	
	@Test
	void monsterDamageNeverDropsBelowOne()
	{
		DamageCalculator calc = new DamageCalculator();
		Player player = new Player("Hero", 50, 5, 10, new Inventory(5));
		Monster monster = new Monster("Weak ghost", 20, 5, 2, List.of());
		
		int damage = calc.calculateMonsterDamage(monster,player);
		
		assertEquals(1, damage);
	}
	
	@Test
	void bossUsesBaseAttackWhenAboveHalfHealth()
	{
		DamageCalculator calc = new DamageCalculator();
		Player player = new Player("Hero", 50, 5, 4, new Inventory(5));
		BossMonster boss = new BossMonster("Boss", 40, 10, 3, List.of(), 3);

		
		int damage = calc.calculateMonsterDamage(boss,player);
		
		assertEquals(6, damage);
	}
	
	@Test
	void bossUsesEnragedAttackATHalfHealthOrBelow()
	{
		DamageCalculator calc = new DamageCalculator();
		Player player = new Player("Hero", 50, 5, 4, new Inventory(5));
		BossMonster boss = new BossMonster("Boss", 40, 10, 3, List.of(), 3);

		boss.takeDamage(20);
		
		int damage = calc.calculateMonsterDamage(boss,player);
		
		assertEquals(9, damage);
	}
	
	
	
	
	
	
	
	
	
	
	

}
