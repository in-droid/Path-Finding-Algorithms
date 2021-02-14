package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Seminar {

    static String[][] readMatrix(File file) {
        ArrayList<ArrayList<String>> temp = new ArrayList<>();
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] lineSplit = line.split(",");

                ArrayList<String> level = new ArrayList<>(Arrays.asList(lineSplit));
                temp.add(level);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        String[][] result = new String[temp.get(0).size()][temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            for (int j = 0; j < temp.get(i).size(); j++) {
                result[j][i] = temp.get(i).get(j);

            }
        }

        return result;
    }


    static boolean checkSolution(State startState, State endState, ArrayList<int[]> movements) {
        for(int i = movements.size() - 1; i  > -1; i--) {
            try {
               startState.setPositions(startState.getPositions().movePR(movements.get(i)[0],
                                                                        movements.get(i)[1]));
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        return Arrays.deepEquals(startState.getPositions().getPositions(),
                                 endState.getPositions().getPositions());
    }

    public static void main(String[] args) throws Exception {
        File start = new File(args[0]);
        File end = new File(args[1]);
        String[][] startMatrix = readMatrix(start);
        String[][] endMatrix = readMatrix(end);

        State startState = new State(new Boxes(startMatrix), null, 0);
        State endState = new State(new Boxes(endMatrix), null, 0);

        Graph g = new Graph(startState);
        ArrayList<int[]> path = g.bfs(endState);

        for (int i = path.size() - 1; i > -1; i--) {
            System.out.println(Arrays.toString(path.get(i)));

        }
        System.out.println("---------------------------------");

        System.out.println(checkSolution(startState, endState, path));

        System.out.println(g.getStatistics());
    }

}
