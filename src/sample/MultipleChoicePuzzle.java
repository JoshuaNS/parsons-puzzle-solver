package sample;

import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultipleChoicePuzzle extends Puzzle {

    public MultipleChoicePuzzle(Element puzzleAtIndex) {
        super(puzzleAtIndex);
        List<Block> solution = new ArrayList<>();
        for (Block b : this.getLines()) {
            if (b.getAssociatedBlocks().size() != 0) {
                solution.add(b);
            }
        }
        this.setSolutionSet(Collections.unmodifiableList(solution));
    }

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

    //returns an array of possible answers. The first answer is the correct one
//    public ArrayList<ArrayList<String>> buildAnswers(){
//        ArrayList<ArrayList<String>> answers = new ArrayList<>();
//        answers.add(this.getLines());
//        for (int i = 0; i < this.getFalseAnswers().size(); i++){
//            ArrayList<String> newAnswer = new ArrayList<>();
//            //parse string to obtain solution list
//            ArrayList<String> answerStrings = new ArrayList<>(Arrays.asList(this.getFalseAnswers().get(i).split(",")));
//            for (int j = 0; j < answerStrings.size(); j++) {
//                String s = answerStrings.get(j).trim();
//                if (s.startsWith("X")) {
//                    s = s.substring(s.length() - 1);
//                    newAnswer.add(this.getDistractors().get(Integer.parseInt(s) - 1));
//                } else {
//                    newAnswer.add(this.getLines().get(Integer.parseInt(s) - 1));
//                }
//            }
//            answers.add(newAnswer);
//        }
//        return answers;
//        return null;
//    }
}
