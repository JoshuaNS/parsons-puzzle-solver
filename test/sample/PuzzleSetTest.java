package sample;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleSetTest {
    @Test
    void importSamplePuzzle1() {
        PuzzleSet ps = new PuzzleSet("Lab 1");
        File f = new File("testfiles/puzzlesamp.xml");
        ps.importPuzzleSet(f);
        ArrayList<Puzzle> puzzles = ps.getPuzzles();
        assertNotNull(puzzles.get(0));
        assertEquals("Lab 1 - Puzzle 1", puzzles.get(0).getName());
        assertNotNull(puzzles.get(1));
        assertEquals("Lab 1 - Puzzle 2", puzzles.get(1).getName());
    }

    @Test
    //test with Name, sequential, random all not specified
    void importSamplePuzzleInvalid() {
        PuzzleSet ps = new PuzzleSet();
        File f = new File("testfiles/puzzlesamperror2.xml");
        ps.importPuzzleSet(f);
        assertEquals("Puzzle Set", ps.getName());
        assertFalse(ps.isRandomOrder());
        assertFalse(ps.isSequentialCompletion());
    }
}