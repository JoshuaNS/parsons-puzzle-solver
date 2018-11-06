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
            ArrayList<Block> choices = currentPuzz.buildChoices();
            for (int i = 0; i < choices.size(); i++){
                System.out.print(choices.get(i).getLines() +"\n");
            }

            List<Block> providedSolution = new ArrayList<>();
            providedSolution.add(new Block("1","for num in range(1, 21):", currentPuzz));
            providedSolution.add(new Block("1","if num % 3 == 0 and num % 5 == 0:", currentPuzz));
            providedSolution.add(new Block("1","print('FizzBuzz')", currentPuzz));
            providedSolution.add(new Block("1","elif num % 3 == 0:", currentPuzz));
            providedSolution.add(new Block("1","print('Fizz')", currentPuzz));
            providedSolution.add(new Block("1","elif num % 5 == 0:", currentPuzz));
            providedSolution.add(new Block("1","print('Buzz')", currentPuzz));
            providedSolution.add(new Block("1","else:", currentPuzz));
            providedSolution.add(new Block("1","print(num)", currentPuzz));

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
            List<Block> lines = mcPuzz.getLines();
            int choiceAreaCount = 1;
            for (int i = 0; i < lines.size(); i++){
                if (lines.get(i).hasAssociatedBlocks()) {
                    System.out.println("<<AREA " + choiceAreaCount++ + ">>");
                }
                else {
                    System.out.print(lines.get(i).getLines() + "\n");
                }
            }
            System.out.println();
            System.out.println("Choices are:");
            List<Block> solutionSet = mcPuzz.getSolutionSet();
            choiceAreaCount = 1;
            for (int i = 0; i < solutionSet.size(); i++) {
                List<Block> choices = new ArrayList<>();
                choices.add(solutionSet.get(i));
                choices.addAll(solutionSet.get(i).getAssociatedBlocks());
                Collections.shuffle(choices);
                System.out.println("<<AREA " + choiceAreaCount++ + ">>:");
                for (Block b : choices) {
                    System.out.println(b.getLines());
                }
                System.out.println();
            }

            List<List<Block>> answers = ((MultipleChoicePuzzle) currentPuzz).buildAnswers(5);

            int choice = 0; // Select the correct choice

            System.out.print("\nChecking solution...\n");
            boolean result = (boolean) mcPuzz.checkSolution(answers.get(choice));
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