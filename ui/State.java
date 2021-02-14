package ui;
import java.util.ArrayList;

public class State {

    private Boxes positions;
    private State parent;
    private ArrayList<State> children;
    private int depth;

    public void setChildren(ArrayList<State> children) {
        this.children = children;
    }

    public State(Boxes positions, State parent, int depth) {
        this.positions = positions;
        this.parent = parent;

        this.children = new ArrayList<State>();
        this.depth = depth;
    }


    public void setPositions(Boxes positions) {
        this.positions = positions;
    }
    public Boxes getPositions() {
        return positions;
    }



    public ArrayList<State> getChildren() {
        return children;
    }

    public int getDepth() {
        return depth;
    }


    public void generateChildren() {
        for (Boxes pos: this.positions.generateChildren()) {
            this.children.add(new State(pos, this, this.depth + 1));

        }
    }

    public static ArrayList<int[]> backTrackPath(State end) {
        ArrayList<int[]> result = new ArrayList<>();
        while(end != null) {
            result.add(end.positions.getMovement());
            end = end.parent;
        }
        return result;
    }

    public String getItem(int row, int column) {
        return this.getPositions().getPositions()[row].get(column);
    }

}