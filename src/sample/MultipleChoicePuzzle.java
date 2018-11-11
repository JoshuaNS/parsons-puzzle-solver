package sample;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.*;

/**
 * Child class for Multiple Choice Puzzles
 *
 * @author Joshua Seguin, Iain Davidson
 * @since November 6th 2018
 *
 */
public class MultipleChoicePuzzle extends Puzzle {
    List<List<Block>> falseAnswers;
    /**
     * Constructor which takes in a puzzle xml object
     * Sets the solution which is the list of blocks that have associated distractors
     * @param puzzleAtIndex
     */
    public MultipleChoicePuzzle(Element puzzleAtIndex) {
        super(puzzleAtIndex);
        falseAnswers = new ArrayList<>();
        // Get falseAnswers specific to MultipleChoice
	    if (puzzleAtIndex.getElementsByTagName("falseAnswers").item(0) != null) {
            Element falseSolutionNodes = (Element)puzzleAtIndex.getElementsByTagName("falseAnswers").item(0);
            NodeList falseSolutionBlocks =  falseSolutionNodes.getElementsByTagName("answer");

            for (int i = 0; i < falseSolutionBlocks.getLength(); i++) {
                /*
                  Formatted as
                  <falseAnswers>
                    <solution>
                      <id>1</id>
                      <id>2</id>
                      ...
                 */
                NodeList idList = ((Element)falseSolutionBlocks.item(i)).getElementsByTagName("id");
                falseAnswers.add(i, new ArrayList<>());
                for (int j = 0; j < idList.getLength(); j++) {
                    Block blockToAdd = getBlock(idList.item(j).getTextContent());
                    //TODO: Error or exception if null?
                    falseAnswers.get(i).add(j, blockToAdd);
                }
            }
        }
	
        this.setSolutionSet(Collections.unmodifiableList(getLines()));
    }

    /**
     * Method that defines the method of checking if a solution is correct
     * @param providedSolution
     * @return
     */
    @Override
    Object checkSolution(List<Block> providedSolution) {

        if (providedSolution.size() != getSolutionSet().size()) {
            return false;
        }
        for (int i = 0; i < getSolutionSet().size(); i++) {
            if (!providedSolution.get(i).equals(getSolutionSet().get(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * getChoices returns a list of the choices presented to a user in a scrambled state
     * @return
     */
    @Override
    public List<List<Block>> getChoices(){
	    List<List<Block>> choices = new ArrayList<>();
	    choices.add(getSolutionSet());
	    choices.addAll(falseAnswers);
	    Collections.shuffle(choices);
        return choices;
    }
}
