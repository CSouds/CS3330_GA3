package com.example.haunted.events;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.haunted.model.Item;

class CombatResultTest {

    @Test
    void constructorStoresAllFields() {
        List<Item> droppedItems = new ArrayList<>();
        droppedItems.add(null);

        CombatResult result = new CombatResult(
                true,
                "Monster defeated.",
                12,
                3,
                true,
                droppedItems
        );

        assertTrue(result.isSuccess());
        assertEquals("Monster defeated.", result.getMessage());
        assertEquals(12, result.getDamageToMonster());
        assertEquals(3, result.getDamageToPlayer());
        assertTrue(result.isMonsterDefeated());
        assertEquals(1, result.getDroppedItems().size());
        assertNull(result.getDroppedItems().get(0));
    }

    @Test
    void returnedDroppedItemsListIsUnmodifiable() {
        CombatResult result = new CombatResult(
                true,
                "Monster defeated.",
                12,
                3,
                true,
                new ArrayList<>()
        );

        System.out.println(result.getDroppedItems().getClass());
        assertThrows(UnsupportedOperationException.class, () -> {
            result.getDroppedItems().add(null);
        });
    }

    @Test
    void combatResultMakesDefensiveCopyOfDroppedItems() {
        List<Item> originalDroppedItems = new ArrayList<>();
        originalDroppedItems.add(null);

        CombatResult result = new CombatResult(
                true,
                "Monster defeated.",
                12,
                3,
                true,
                originalDroppedItems
        );

        originalDroppedItems.add(null);

        assertEquals(1, result.getDroppedItems().size());
    }
}