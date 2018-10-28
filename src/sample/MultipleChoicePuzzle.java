package sample;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;

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
        if (!(providedSolution instanceof ArrayList)) {
            System.err.println("Solution wasn't provided as ArrayList");
            return null;
        }
        ArrayList<String> givenSolution = (ArrayList<String>)providedSolution;

        if (getSolutions().size() != givenSolution.size()) {
            return false;
        }
        for (int i = 0; i < getSolutions().size(); i++) {
            if (!givenSolution.get(i).equals(getSolutions().get(i))) {
                return false;
            }
        }
        return true;
    }

    //returns an array of possible answers. The first answer is the correct one
    public ArrayList<ArrayList<String>> buildAnswers(){
        ArrayList<ArrayList<String>> answers = new ArrayList<>();
        answers.add(this.getSolutions());
        for (int i = 0; i < this.getFalseAnswers().size(); i++){
            ArrayList<String> newAnswer = new ArrayList<>();
            //parse string to obtain solution list
            ArrayList<String> answerStrings = new ArrayList<>(Arrays.asList(this.getFalseAnswers().get(i).split(",")));
            for (int j = 0; j < answerStrings.size(); j++) {
                String s = answerStrings.get(j).trim();
                if (s.startsWith("X")) {
                    s = s.substring(s.length() - 1);
                    newAnswer.add(this.getDistractors().get(Integer.parseInt(s) - 1));
                } else {
                    newAnswer.add(this.getSolutions().get(Integer.parseInt(s) - 1));
                }
            }
            answers.add(newAnswer);
        }
        return answers;
    }
}
