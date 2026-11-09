package com.happyhour.model.score;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

public class ScoreBoardTest {
    private static final String TEST_FILE = "target/test-scores.txt";

    private ScoreBoard newEmptyBoard() {
        new File(TEST_FILE).delete();
        return new ScoreBoard(TEST_FILE);
    }

    @Test
    void scoresAreSortedHighestFirst() {
        ScoreBoard board = newEmptyBoard();
        board.addScore("Ruben", 20, false);
        board.addScore("Lilou", 50, false);
        board.addScore("Melvin", 35, false);
        assertEquals("Lilou", board.getEntries().get(0).getName());
        assertEquals("Melvin", board.getEntries().get(1).getName());
        assertEquals("Ruben", board.getEntries().get(2).getName());
    }

    @Test
    void onlyTheTenBestAreKept() {
        ScoreBoard board = newEmptyBoard();
        for (int i = 1; i <= 12; i++) {
            board.addScore("Joueur" + i, i, false);
        }
        assertEquals(10, board.getEntries().size());
        assertEquals(12.0, board.getEntries().get(0).getScore());
    }

    @Test
    void scoresAreSavedAndReloaded() {
        ScoreBoard board = newEmptyBoard();
        board.addScore("Lilou", 42.5, true);

        ScoreBoard reloaded = new ScoreBoard(TEST_FILE);
        assertEquals(1, reloaded.getEntries().size());
        assertEquals("Lilou", reloaded.getEntries().get(0).getName());
        assertEquals(42.5, reloaded.getEntries().get(0).getScore());
        assertTrue(reloaded.getEntries().get(0).isDefeat());
    }

    @Test
    void semicolonIsRemovedFromName() {
        ScoreBoard board = newEmptyBoard();
        board.addScore("Li;lou", 10, false);
        assertEquals("Lilou", board.getEntries().get(0).getName());
    }
}