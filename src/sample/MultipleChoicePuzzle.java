package sample;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class MultipleChoicePuzzle extends Puzzle {

    public MultipleChoicePuzzle(Element puzzleAtIndex) {
        super(puzzleAtIndex);

        // False Answers
        if (puzzleAtIndex.getElementsByTagName("falseAnswers").item(0) != null) {
            this.setFalseAnswers(new ArrayList<>());
            Element falseSolutionNodes = (Element)puzzleAtIndex.getElementsByTagName("falseAnswers").item(0);
            NodeList falseSolutionBlocks =  falseSolutionNodes.getElementsByTagName("answer");
            for (int i = 0; i < falseSolutionBlocks.getLength(); i++) {
                this.getFalseAnswers().add(falseSolutionBlocks.item(i).getTextContent());
            }
        }
    }

    @Override
    Object checkSolution(Object providedSolution) {
        return null;
    }
}
