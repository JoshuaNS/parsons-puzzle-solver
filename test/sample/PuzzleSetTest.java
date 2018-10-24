package sample;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
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
        assertEquals(puzzles.get(0).getName(), "Lab 1 - Puzzle 1");
        assertNotNull(puzzles.get(1));
        assertEquals(puzzles.get(1).getName(), "Lab 1 - Puzzle 2");
    }

    @Test
    //test with Name, sequential, random all not specified
    void importSamplePuzzleInvalid() {
        PuzzleSet ps = new PuzzleSet();
        File f = new File("testfiles/puzzlesamperror2.xml");
        ps.importPuzzleSet(f);
        assertEquals(ps.getName(), "Puzzle Set");
        assertFalse(ps.isRandomOrder());
        assertFalse(ps.isSequentialCompletion());
    }
}