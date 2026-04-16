package com.example.haunted.rules;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.example.haunted.model.Item;
import com.example.haunted.model.Monster;
import com.example.haunted.model.Quest;
import com.example.haunted.model.QuestItem;
import com.example.haunted.model.QuestStatus;

//import org.junit.jupiter.api.Test;

class QuestTrackerTest {

	@Test
	void nullItemDoesNotUpdateQuest() {
		QuestTracker tracker = new QuestTracker();
		Quest quest = new Quest("Escape the Basement", "Recover the gradebook and defeat the phantom");
		
		tracker.updateQuestForItem(quest, null);
		
		assertFalse(quest.isGradebookRecovered());
		assertFalse(quest.isComplete());
		assertEquals(QuestStatus.NOT_STARTED, quest.getStatus());
	}
	
	@ParameterizedTest
    @CsvSource({
        "Lost Gradebook, true",
        "lost gradebook, true",
        "Notebook, false",
        "Wrong Item, false"
    })
    void itemNameQuestUpdates(String itemName, boolean shouldRecover) {
        QuestTracker tracker = new QuestTracker();
        Quest quest = new Quest("Escape", "Recover gradebook");
        Item item = new QuestItem(itemName, "test");

        tracker.updateQuestForItem(quest, item);

        assertEquals(shouldRecover, quest.isGradebookRecovered());

        if (shouldRecover) {
            assertEquals(QuestStatus.IN_PROGRESS, quest.getStatus());
        } else {
            assertEquals(QuestStatus.NOT_STARTED, quest.getStatus());
        }
    }

	@Test
	void lostGradebookMarksQuestInProgress()
	{
		QuestTracker tracker = new QuestTracker();
		Quest quest = new Quest("Escape the Basement", "Recover the gradebook and defeat the phantom");
		Item item = new QuestItem("Lost Gradebook", "The right quest item");
		
		tracker.updateQuestForItem(quest, item);
		
		assertTrue(quest.isGradebookRecovered());
		assertFalse(quest.isPhantomDefeated());
		assertFalse(quest.isComplete());
		assertEquals(QuestStatus.IN_PROGRESS, quest.getStatus());
	}
	
	
	@Test
	void nullMonsterDoesNotUpdateQuest()
	{
		QuestTracker tracker = new QuestTracker();
		Quest quest = new Quest("Escape the Basement", "Recover the gradebook and defeat the phantom");
		
		tracker.updateQuestForMonster(quest, null);
		
		assertFalse(quest.isPhantomDefeated());
		assertFalse(quest.isComplete());
		assertEquals(QuestStatus.NOT_STARTED, quest.getStatus());
	}
	
	@ParameterizedTest
	@CsvSource({
	    "Final Exam Phantom, true, true",
	    "final exam phantom, true, true",
	    "Sleep-Deprived TA, true, false",
	    "Final Exam Phantom, false, false"
	})
	void monsterQuestUpdatesDependOnNameAndDeath(
	        String monsterName,
	        boolean shouldKillMonster,
	        boolean shouldMarkDefeated) {

	    QuestTracker tracker = new QuestTracker();
	    Quest quest = new Quest("Escape", "Defeat the phantom");
	    Monster monster = new Monster(monsterName, 40, 10, 4, List.of());

	    if (shouldKillMonster) {
	        monster.takeDamage(999);
	    }

	    tracker.updateQuestForMonster(quest, monster);

	    assertEquals(shouldMarkDefeated, quest.isPhantomDefeated());

	    if (shouldMarkDefeated) {
	        assertEquals(QuestStatus.IN_PROGRESS, quest.getStatus());
	    } else {
	        assertEquals(QuestStatus.NOT_STARTED, quest.getStatus());
	    }
	}
	
	@Test
	void aliveFinalExamPhantomDoesNotUpdateQuest()
	{
		QuestTracker tracker = new QuestTracker();
		Quest quest = new Quest("Escape the Basement", "Recover the gradebook and defeat the phantom");
		Monster monster = new Monster("Final Exam Phantom", 40, 10, 4, List.of());
		
		tracker.updateQuestForMonster(quest, monster);
		
		assertFalse(quest.isPhantomDefeated());
		assertFalse(quest.isComplete());
		assertEquals(QuestStatus.NOT_STARTED, quest.getStatus());
	}
	
	@Test
	void deadFinalExamPhantomWithoutGradebookMarksQuestInProgress()
	{
		QuestTracker tracker = new QuestTracker();
		Quest quest = new Quest("Escape the Basement", "Recover the gradebook and defeat the phantom");
		Monster monster = new Monster("Final Exam Phantom", 40, 10, 4, List.of());
		
		monster.takeDamage(999);
		
		tracker.updateQuestForMonster(quest, monster);
		
		assertFalse(quest.isGradebookRecovered());
		assertTrue(quest.isPhantomDefeated());
		assertFalse(quest.isComplete());
		assertEquals(QuestStatus.IN_PROGRESS, quest.getStatus());
	}
	@Test
	void deadFinalExamPhantomMarksQuestCompletedWhenGradebookAlreadyRecovered() 
	{
		QuestTracker tracker = new QuestTracker();
		Quest quest = new Quest("Escape the Basement", "Recover the gradebook and defeat the phantom");
		Monster monster = new Monster("Final Exam Phantom", 40, 10, 4, List.of());
		
		quest.markGradebookRecovered();
		monster.takeDamage(999);
		
		tracker.updateQuestForMonster(quest, monster);
		
		assertTrue(quest.isGradebookRecovered());
		assertTrue(quest.isPhantomDefeated());
		assertTrue(quest.isComplete());
		assertEquals(QuestStatus.COMPLETED, quest.getStatus());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}
