package parsonsolver;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Child class for Drag and Drop Puzzles
 *
 * @author Joshua Seguin, Iain Davidson
 * @since November 6th 2018
 *
 */
public class DragNDropPuzzle extends Puzzle {

    /**
     * Puzzle constructor for creating a new puzzle using the creator
     * @param name
     */
    public DragNDropPuzzle(String name, int index){
        super(name, index, PuzzleType.DnD);
    }

    /**
     * Constructor which takes puzzle uml data as input
     * Sets the DnD solution set from the solution lines
     *
     * @param puzzleXML
     */
    public DragNDropPuzzle(Element puzzleXML) throws InvalidInputFileException {
        super(puzzleXML);
        this.setSolutionSet(Collections.unmodifiableList(this.getLines()));
    }

    /**
     * Method that defines the method of checking if a solution is correct
     *
     * @param providedSolution
     * @return
     */
    @Override
    Object checkSolution(List<Block> providedSolution) {
        this.incAttempts();

        if (getSolutionSet().size() != providedSolution.size()) {
            return false;
        }

        boolean isCorrect = true;
        boolean[] resultArr = new boolean[getSolutionSet().size()];

        for (int i = 0; i < getSolutionSet().size(); i++) {

            if (isIndentRequired()) {
                if (!providedSolution.get(i).getLines().equals(getSolutionSet().get(i).getLines())) {
                    resultArr[i] = false;
                    isCorrect = false;
                } else if (providedSolution.get(i).getSolutionTab() != getSolutionSet().get(i).getTab()) {
                    resultArr[i] = false;
                    isCorrect = false;
                } else {
                    resultArr[i] = true;
                }
            }
            else {
                if (!providedSolution.get(i).getLinesTabbed().equals(getSolutionSet().get(i).getLinesTabbed())) {
                    resultArr[i] = false;
                    isCorrect = false;
                } else {
                    resultArr[i] = true;
                }
            }

        }

        if (isCorrect) {
            this.setCompleted(true);
            return true;
        } else {
            return resultArr;
        }
    }

    @Override
    public List<List<Block>> getChoices() {
        // There is only one possible "choice" for this type of test
        List<List<Block>> arr = new ArrayList<>();
        arr.add(this.getBlocksSet());
        return arr;
    }

    @Override
    protected List<Node> exportExtraToXML(Document document) {
        return null;
    }
}
