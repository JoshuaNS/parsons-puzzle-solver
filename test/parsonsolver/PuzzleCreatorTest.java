package parsonsolver;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * PuzzleCreatorTest: Test class for PuzzleCreator
 * @author Iain Davidson
 * @since January 20th, 2019
 *
 */

public class PuzzleCreatorTest {
    PuzzleCreator creator;

    @Test
    public void operationTest(){
        creator = new PuzzleCreator();
        System.out.println("Welcome to the puzzle creator. Please import a puzzle or create a new set.");
        creator.createNewSet("Assignment 2");
        System.out.println("Puzzle Select Screen: Please create or select a puzzle to edit, edit properties, or export.");
        creator.createNewPuzzle("Question 1", PuzzleType.DnD);
        System.out.println("Puzzle opened.");
        System.out.println("Please enter your code and distractors");
        //The following lines simulate the receiving of a text input via the user interface and converting it to lines
        Scanner s;
        //get and convert solution code block
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        int index = 1;
        try {
            s = new Scanner(new File("testfiles/solutionCode.txt"));
            while (s.hasNextLine()){
                list.add(s.nextLine());
                list2.add(String.valueOf(index));
                index++;
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        creator.getCurrentPuzzle().setLines(creator.convertLines(list, list2));
        creator.getCurrentPuzzle().setSolutionSet(creator.convertLines(list, list2));
        //get and convert distractor code block
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        index = 1;
        try {
            s = new Scanner(new File("testfiles/distractorCode.txt"));
            while (s.hasNextLine()){
                list.add(s.nextLine());
                list2.add("X" +String.valueOf(index));
                index++;
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        creator.getCurrentPuzzle().setDistractors(creator.convertLines(list, list2));

        System.out.println("Please set puzzle properties:");
        creator.getCurrentPuzzle().setDescription("Problem 1 of Assignment 2");
        creator.getCurrentPuzzle().setIndentRequired(true);
        creator.getCurrentPuzzle().setLanguage("Python");
        System.out.println("Done editing, returning to Puzzle Select...");

        System.out.println("Puzzle Select Screen: Please create or select a puzzle to edit, edit properties, or export.");
        System.out.println("Please set puzzle set properties:");
        creator.getCurrentSet().setRandomOrder(false);
        creator.getCurrentSet().setSequentialCompletion(true);
        System.out.println("Done editing, returning to Puzzle Select...");

        System.out.println("Puzzle Select Screen: Please create or select a puzzle to edit, edit properties, or export.");
        creator.exportSet();
        System.out.println("Set exported, returning to home screen....");
        creator.closeSession();
        System.out.println("Welcome to the puzzle creator. Please import a puzzle or create a new set.");
    }
}
