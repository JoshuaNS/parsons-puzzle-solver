package sample;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PuzzleTest {

    @Test
    void importSamplePuzzle1() {
        File f = new File("puzzlesamp.xml");
        assertTrue(f.exists());
        Puzzle p = new Puzzle(f, 1);
        List<String> expectedSolution = new ArrayList<>();
        expectedSolution.add("for num in range(1, 21):");
        expectedSolution.add("if num % 3 == 0 and num % 5 == 0:");
        expectedSolution.add("print('FizzBuzz')");
        expectedSolution.add("elif num % 3 == 0:");
        expectedSolution.add("print('Fizz')");
        expectedSolution.add("elif num % 5 == 0:");
        expectedSolution.add("print('Buzz')");
        expectedSolution.add("else:");
        expectedSolution.add("print(num)");

        List<String> expectedDistractors = new ArrayList<>();
        expectedDistractors.add("for num in range(0, 21):");
        expectedDistractors.add("if num % 3 == 0:");
        expectedDistractors.add("elif num % 5 == 0:");
        expectedDistractors.add("elif num % 3 == 0 and num % 5 == 0");

        assertEquals(p.getIndex(), 1);
        assertEquals(p.getDescription(), "Output the numbers 1-20, print “fizz” if the number is divisible by 3, “buzz” if it is divisible by 5, and “fizzbuzz” if it is divisible by both");
        assertEquals(p.getName(),"Lab 1 - Puzzle 1");
        assertEquals(p.getLanguage(), "Python");
        assertEquals(p.getType(), PuzzleType.DnD);
        assertEquals(p.isIndentRequired(), false);

        assertNotNull(p.getSolutions());
        assertLinesMatch(expectedSolution, p.getSolutions());
        assertNotNull(p.getDistractors());
        assertLinesMatch(expectedDistractors, p.getDistractors());
    }
}