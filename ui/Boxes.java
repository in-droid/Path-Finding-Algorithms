package ui;

import java.util.*;

public class Boxes {

    private Stack<String>[] positions;
    int rows;
    int maxHeight;
    private int[] movement;

    Boxes(String[][] matrix) {
        this.positions = new Stack [ matrix.length];
        this.maxHeight = matrix[0].length;
        for (int i = 0; i < matrix.length; i++) {
            Stack<String> stack = new Stack<>();
            for (int j = matrix[0].length - 1; j > -1; j--) {
                if(matrix[i][j].matches("'[a-zA-Z]+'")) {
                    stack.push(matrix[i][j]);
                }
            }
            this.positions[i] = stack;
        }
        this.movement = new int[]{0, 0};
    }

    Boxes(Stack[] positions, int maxHeight, int[] movement) {
        this.positions = positions;
        this.maxHeight = maxHeight;
        this.movement = movement;
    }


    public Stack<String>[] getPositions() {
        return positions;
    }

    public int[] getMovement() { return movement; }


    public Boxes movePR(int p, int r) throws Exception {
        Stack<String>[] result = new Stack[this.positions.length];
        for (int i = 0; i < this.positions.length; i++) {
            Stack<String> temp = (Stack<String>) (this.positions[i]).clone();
            result[i] = (temp);
        }

        if((this.positions[p]).isEmpty()) {
            throw new Exception("Stack is empty");
        }

        if(this.positions[r].size() >= maxHeight){
            throw  new Exception("Stack is full!");
        }

        String from = result[p].pop();
        result[r].push(from);

        return new Boxes(result, this.maxHeight, new int[]{p, r});
    }

    ArrayList<Boxes> generateChildren() {
        ArrayList<Boxes> children = new ArrayList<>();
        for (int i = 0; i < this.positions.length; i++) {
            for (int j = 0; j < this.positions.length; j++) {
                if (i != j) {
                    try {
                        children.add(this.movePR(i, j));
                    }
                    catch (Exception ignored) {
                    }
                }
            }
        }
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boxes boxes = (Boxes) o;
        return rows == boxes.rows && maxHeight == boxes.maxHeight && Arrays.equals(positions, boxes.positions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, maxHeight);
        result = 31 * result + Arrays.hashCode(positions);
        return result;
    }
}
