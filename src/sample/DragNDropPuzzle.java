package sample;

import org.w3c.dom.Element;

import java.util.Collections;
import java.util.List;

public class DragNDropPuzzle extends Puzzle {

    public DragNDropPuzzle(Element puzzleXML) {
        super(puzzleXML);
        this.setSolutionSet(Collections.unmodifiableList(this.getLines()));
    }

    @Override
    Object checkSolution(List<Block> providedSolution) {
        if (getSolutionSet().size() != providedSolution.size()) {
            return false;
        }
        for (int i = 0; i < getLines().size(); i++) {
            if (!providedSolution.get(i).equals(getSolutionSet().get(i))) {
                return false;
            }
        }
        return true;
    }
}
