package com.example.haunted.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

public class QuestTest {
    private Quest q;

    @BeforeEach
    public void setUp()
    {
        q = new Quest("Pick up sticks", "Pick up all sticks to complete");
    }

    public static Quest createNotStartedQuest()
    {
        Quest notStarted = new Quest("Trees", "Chop down trees");
        return notStarted;
    }

    public static Quest createStartedQuest()
    {
        Quest started = new Quest("Trees", "Chop down trees");
        started.markGradebookRecovered();
        return started;
    }

    public static Quest createCompletedQuest()
    {
        Quest completed = new Quest("Trees", "Chop down trees");
        completed.markGradebookRecovered();
        completed.markPhantomDefeated();
        return completed;
    }

    @Test
    public void shouldGetName()
    {
        assertEquals("Pick up sticks", q.getName());
    }

    @Test
    public void shouldGetDescription()
    {
        assertEquals("Pick up all sticks to complete", q.getDescription());
    }

    public static Stream<Arguments> questStatusCases()
    {
        return Stream.of(
                Arguments.of(createNotStartedQuest(), QuestStatus.NOT_STARTED),
                Arguments.of(createStartedQuest(), QuestStatus.IN_PROGRESS),
                Arguments.of(createCompletedQuest(), QuestStatus.COMPLETED)
                );
    }
    @ParameterizedTest
    @MethodSource("questStatusCases")
    public void questStatusTest(Quest q, QuestStatus expected)
    {
        assertEquals(expected, q.getStatus());
    }

    @Test
    public void gradeBookNotRecovered()
    {
        assertFalse(q.isGradebookRecovered());
    }

    @Test
    public void gradeBookRecovered()
    {
        q.markGradebookRecovered();
        assertTrue(q.isGradebookRecovered());
    }

    @Test
    public void phantomNotDefeated()
    {
        assertFalse(q.isPhantomDefeated());
    }

    @Test
    public void phantomDefeated()
    {
        q.markPhantomDefeated();
        assertTrue(q.isPhantomDefeated());
    }
    
    @Test
    public void updateStatusWithNothingMarkedStaysNotStarted()
    {
        q.updateStatus();
        assertEquals(QuestStatus.NOT_STARTED, q.getStatus());
    }

    @Test
    public void statusShouldUpdateStarted()
    {
        q.markGradebookRecovered();
        assertEquals(QuestStatus.IN_PROGRESS, q.getStatus());
    }

    @Test
    public void statusShouldUpdateCompleted()
    {
        q.markGradebookRecovered();
        q.markPhantomDefeated();
        assertEquals(QuestStatus.COMPLETED, q.getStatus());
    }

    @Test
    public void notCompleted()
    {
        assertFalse(q.isComplete());
    }

    @Test
    public void isCompleted()
    {
        q.markGradebookRecovered();
        q.markPhantomDefeated();
        assertTrue(q.isComplete());
    }
    
    @Test
    public void questStatusValues_exist()
    {
        assertEquals(3, QuestStatus.values().length);
    }
}