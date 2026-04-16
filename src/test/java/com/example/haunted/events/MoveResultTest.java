package com.example.haunted.events;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MoveResultTest {

    @Test
    void constructorStoresSuccessfulMoveFields() {
        MoveResult result = new MoveResult(true, "Moved north.", true, 8);

        assertTrue(result.isSuccess());
        assertEquals("Moved north.", result.getMessage());
        assertTrue(result.isTrapTriggered());
        assertEquals(8, result.getTrapDamage());
    }

    @Test
    void constructorStoresFailedMoveFields() {
        MoveResult result = new MoveResult(false, "Door is locked.", false, 0);

        assertFalse(result.isSuccess());
        assertEquals("Door is locked.", result.getMessage());
        assertFalse(result.isTrapTriggered());
        assertEquals(0, result.getTrapDamage());
    }
}