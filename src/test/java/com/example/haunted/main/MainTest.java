package com.example.haunted.main;
import com.example.haunted.Main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Covers Main.main(String[]). Without this test, the entire Main class is
 * uncovered (~5 lines / multiple branches in JaCoCo).
 */
public class MainTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream capturedOut;

    @BeforeEach
    public void redirectStdout() {
        capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut));
    }

    @AfterEach
    public void restoreStdout() {
        System.setOut(originalOut);
    }

    @Test
    public void mainPrintsStartingRoomMoveAndPickupResults() {
        Main.main(new String[]{});

        String output = capturedOut.toString();
        // Starting room name (printed first)
        assertTrue(output.contains("Maintenance Stairwell"),
                "Expected starting room name in output, got: " + output);
        // Move-east result message
        assertTrue(output.contains("Moved to Abandoned Lecture Hall"),
                "Expected move message in output, got: " + output);
        // Pickup result message
        assertTrue(output.contains("Picked up Coffee Potion"),
                "Expected pickup message in output, got: " + output);
    }

    @Test
    public void mainCanBeInvokedWithNullArgsArray() {
        // Defensive: Main.main never reads args, so passing null should not throw.
        // This also exercises the constructor path again from a clean state.
        assertDoesNotThrow(() -> Main.main(null));
    }

    @Test
    public void mainConstructorIsAccessible() {
        // Main has an implicit public no-arg constructor. Some coverage tools
        // count that constructor as a separately-covered method.
        assertNotNull(new Main());
    }
}