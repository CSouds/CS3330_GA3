package com.example.haunted.events;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InteractionResultTest {

    @Test
    void constructorStoresSuccessfulInteraction() {
        InteractionResult result = new InteractionResult(true, "Potion used.");

        assertTrue(result.isSuccess());
        assertEquals("Potion used.", result.getMessage());
    }

    @Test
    void constructorStoresFailedInteraction() {
        InteractionResult result = new InteractionResult(false, "Inventory full.");

        assertFalse(result.isSuccess());
        assertEquals("Inventory full.", result.getMessage());
    }
}