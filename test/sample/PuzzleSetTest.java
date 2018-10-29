package sample;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Test
    void operationTest(){
        System.out.print("Welcome to the puzzle solver. Please import a puzzle.\n");
        File f = new File("testfiles/puzzlesamp.xml");
        System.out.print("Importing file......\n");
        PuzzleSet ps = new PuzzleSet(f);
        System.out.print("Import successful.\nPuzzles found:\n");
        for (int i = 0; i < ps.getPuzzles().size(); i++){
            System.out.print(ps.getPuzzles().get(i).getName() +", index " +ps.getPuzzles().get(i).getIndex() +"\n");
        }
        System.out.print("Enter an index to open a puzzle:\n");
        int input = 1;
        System.out.print("Loading puzzle with index " +input +"...\n");
        Puzzle currentPuzz = ps.getPuzzle(input);
        System.out.print("Puzzle loaded.\n\n");
        if (currentPuzz.getType().equals(PuzzleType.DnD)){
            System.out.print("Drag into the correct order:\n");
            ArrayList<String> choices = currentPuzz.buildChoices();
            for (int i = 0; i < choices.size(); i++){
                System.out.print(choices.get(i) +"\n");
            }

            List<String> providedSolution = new ArrayList<>();
            providedSolution.add("for num in range(1, 21):");
            providedSolution.add("if num % 3 == 0 and num % 5 == 0:");
            providedSolution.add("print('FizzBuzz')");
            providedSolution.add("elif num % 3 == 0:");
            providedSolution.add("print('Fizz')");
            providedSolution.add("elif num % 5 == 0:");
            providedSolution.add("print('Buzz')");
            providedSolution.add("else:");
            providedSolution.add("print(num)");

            System.out.print("\nChecking solution...\n");
            boolean result = (boolean) currentPuzz.checkSolution(providedSolution);
            if (result){
                assertTrue(result);
                System.out.print("Congratulations! Puzzle solved.\n");
            }
            System.out.print("Returning to menu...\n");
        }
        System.out.print("Enter an index to open a puzzle:\n");
        input = 2;
        System.out.print("Loading puzzle with index " +input +"...\n");
        currentPuzz = ps.getPuzzle(input);
        System.out.print("Puzzle loaded.\n\n");
        if (currentPuzz.getType().equals(PuzzleType.MC)){
            MultipleChoicePuzzle mcPuzz = (MultipleChoicePuzzle) currentPuzz;
            System.out.print("Choose the correct choice:\n");
            ArrayList<String> choices = mcPuzz.buildChoices();
            for (int i = 0; i < choices.size(); i++){
                System.out.print(choices.get(i) +"\n");
            }

            ArrayList<ArrayList<String>> answers = (ArrayList<ArrayList<String>>)mcPuzz.buildAnswers();

            for (int i = 0; i < answers.size(); i++){
                System.out.print("\nAnswer " +(i+1) +"\n");
                for (int j = 0; j < answers.get(i).size(); j++) {
                    System.out.print(answers.get(i).get(j) +"\n");
                }
            }
            int choice = 1;

            System.out.print("\nChecking solution...\n");
            boolean result = (boolean) mcPuzz.checkSolution(answers.get(choice-1));
            if (result){
                assertTrue(result);
                System.out.print("Congratulations! Puzzle solved.\n");
            }
            System.out.print("Returning to menu...\n");
        }
        System.out.print("Enter an index to open a puzzle:\n");
        input = 0;
        System.out.print("Exiting program.\n");
    }
}