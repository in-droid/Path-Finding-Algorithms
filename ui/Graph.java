package ui;

import java.util.*;

public class Graph {
    private State startState;
    private HashSet<Boxes> alreadyVisited;
    private Queue<State> queue;
    private Stack<State> stack; //Kiril
    private String statistics;
    private boolean goalFound;
    private int[] stats;

    public Graph(State startState) {
        this.startState = startState;
        this.alreadyVisited = new HashSet<>();
        this.statistics = "";
        this.stats = new int[]{0, 0, 0, 0};

        this.goalFound = false;
    }

    public String getStatistics() {
        return statistics;
    }


    public ArrayList<int[]> bfs(State endState) {
        int developedNodes = 0, maxDepth = 0, maxNodesInMem = 0, iterations = 0;

        ArrayList<int[]> result = new ArrayList<>();
        this.queue = new ArrayDeque<>();
        queue.add(this.startState);
        State currentState;

        while (!queue.isEmpty()) {
            currentState = queue.remove();

            maxDepth = Math.max(maxDepth, currentState.getDepth());
            iterations++;

            if(Arrays.deepEquals(currentState.getPositions().getPositions(),
                    endState.getPositions().getPositions())) {
                System.out.println(Arrays.deepToString(currentState.getPositions().getPositions()));

                result = State.backTrackPath(currentState);
                break;

            } else {
                alreadyVisited.add(currentState.getPositions());
                currentState.generateChildren();
                queue.addAll(currentState.getChildren());
                this.removeAlreadyVisited();

                maxNodesInMem = Math.max(maxNodesInMem, queue.size());
                developedNodes += currentState.getChildren().size();
            }
        }

        this.statistics = String.format("STATISTICS:\n" +
                                         "ITERATIONS: %d,\n" +
                                         "DEVELOPED NODES: %d\n" +
                                          "MAX NODES IN MEMORY: %d\n" +
                                          "MAX REACHED DEPTH: %d",
                                          iterations, developedNodes, maxNodesInMem, maxDepth);
        return result;
    }

    //returns ArrayList for printing the path
    public ArrayList<int[]> dfs(State endState) { //Kiril
        this.stack = new Stack<>();
        stack.push(this.startState);
        State currentState;

        ArrayList<int[]> result = new ArrayList<>();

        int developedNodes = 0, maxDepth = 0, maxNodesInMem = 0, iterations = 0;

        while(!stack.isEmpty()){
            currentState = stack.pop();
            iterations +=1;
            maxDepth = Math.max(maxDepth, currentState.getDepth());
            if(Arrays.deepEquals(currentState.getPositions().getPositions(),
                    endState.getPositions().getPositions())) {

                result = State.backTrackPath(currentState);

                break;
            }
            if(!alreadyVisited.contains(currentState.getPositions())){
                //print code should probably go here
                currentState.generateChildren();
                ArrayList<State> children = currentState.getChildren();

                developedNodes += children.size();
                for(int i=0;i<children.size();i++) {
                    if(!alreadyVisited.contains(currentState.getPositions())){
                        stack.push(children.get(i));
                    }
                }
                alreadyVisited.add(currentState.getPositions());

                maxNodesInMem = Math.max(maxNodesInMem, stack.size());
            }
        }
        this.statistics = String.format("STATISTICS:\n" +
                        "ITERATIONS: %d,\n" +
                        "DEVELOPED NODES: %d\n" +
                        "MAX NODES IN MEMORY: %d\n" +
                        "MAX REACHED DEPTH: %d",
                iterations, developedNodes, maxNodesInMem, maxDepth);

        return result;
    }

    public ArrayList<int[]> aStar(State endState) {
        this.stack = new Stack<>();
        stack.push(this.startState);
        State currentState;
        ArrayList<int[]> result = new ArrayList<>();

        int developedNodes = 0, maxDepth = 0, maxNodesInMem = 0, iterations = 0; // za proverka

        while (!stack.isEmpty()) {
            currentState = stack.pop();
            iterations += 1;
            maxDepth = Math.max(maxDepth, currentState.getDepth());
            if(Arrays.deepEquals(currentState.getPositions().getPositions(),
                    endState.getPositions().getPositions())) {

                result = State.backTrackPath(currentState);
                break;
            }

            if(!alreadyVisited.contains(currentState.getPositions())){
                currentState.generateChildren();
                ArrayList<State> children = currentState.getChildren();

                developedNodes += children.size();

                State bestOne = getBestHeuristic(children,endState);
                children.remove(bestOne);
                for(int i=0;i<children.size();i++){
                    if(!alreadyVisited.contains(currentState.getPositions())){
                        stack.push(children.get(i));
                    }
                }
                stack.push(bestOne);

                alreadyVisited.add(currentState.getPositions());

                maxNodesInMem = Math.max(maxNodesInMem, stack.size());
            }
        }
        this.statistics = String.format("STATISTICS:\n" +
                        "ITERATIONS: %d,\n" +
                        "DEVELOPED NODES: %d\n" +
                        "MAX NODES IN MEMORY: %d\n" +
                        "MAX REACHED DEPTH: %d",
                iterations, developedNodes, maxNodesInMem, maxDepth);

        return result;
    }

    public ArrayList<int[]> id(State endNode) {
        ArrayList<int[]> result = new ArrayList<>();
        int maxDepth = 0;
        while (!goalFound) {
            startState.setChildren(new ArrayList<>());
            result = depthLimitedSearch(endNode, maxDepth);
            maxDepth++;
        }

        this.statistics = String.format("STATISTICS:\n" +
                        "ITERATIONS: %d,\n" +
                        "DEVELOPED NODES: %d\n" +
                        "MAX NODES IN MEMORY: %d\n" +
                        "MAX REACHED DEPTH: %d",
                this.stats[0], this.stats[1], this.stats[2], maxDepth);
        

        return result;

    }

    private ArrayList<int[]> depthLimitedSearch(State endState, int maxDepth) {
        this.stack = new Stack<>();
        this.alreadyVisited = new HashSet<>();
        stack.push(this.startState);
        State currentState;

        ArrayList<int[]> result = new ArrayList<>();
        int maxNodesInMem = 0;
        while (!stack.isEmpty()) {
            currentState = stack.pop();
            if (currentState.getDepth() < maxDepth) {
                this.stats[0] ++;
                if (Arrays.deepEquals(currentState.getPositions().getPositions(),
                        endState.getPositions().getPositions())) {

                    result = State.backTrackPath(currentState);
                    this.goalFound = true;
                    break;

                }
                if (!alreadyVisited.contains(currentState.getPositions())) {
                    //print code should probably go here
                    currentState.generateChildren();
                    ArrayList<State> children = currentState.getChildren();

                    this.stats[1] += children.size();
                    for (int i = 0; i < children.size(); i++) {
                        if (!alreadyVisited.contains(currentState.getPositions())) {
                            stack.push(children.get(i));
                        }
                    }
                    alreadyVisited.add(currentState.getPositions());

                    this.stats[2] = Math.max(this.stats[2],stack.size());
                }
            }
        }
        return result;
    }

    private State getBestHeuristic(ArrayList<State> options, State endState){
        int bestHeuristic = heuristicAlgorithm(options.get(0),endState);
        int currentHeuristic;
        State bestState = options.get(0);

        for(int i = 1;i<options.size();i++){
            if(!alreadyVisited.contains(options.get(i).getPositions())){
                currentHeuristic = heuristicAlgorithm(options.get(i),endState);
                if(currentHeuristic > bestHeuristic){
                    bestState = options.get(i);
                    bestHeuristic = currentHeuristic;
                }
            }
        }

        return bestState;
    }

    private int heuristicAlgorithm(State compareState, State endState){
        int score = 0;
        int height;

        for(int i=0;i<endState.getPositions().getPositions().length;i++){
            height = whoIsSmaller(compareState,endState,i);
            for(int j=1;j<height;j++){
                if(compareState.getItem(i,j-1).equals(endState.getItem(i,j-1))){
                    //if previous item was correct
                    if(compareState.getItem(i,j).equals(endState.getItem(i,j))){
                        //if this item is correct
                        score += 1;
                    }
                    else{
                        //if only previous is correct
                        score += 0;
                    }
                }
                else{
                    //if previous is wrong
                    score -= 1;
                }
            }
        }

        return score;
    }

    private int whoIsSmaller(State compareState, State endState, int index){ //used for heuristic alg
        if(compareState.getPositions().getPositions()[index].size() >
                endState.getPositions().getPositions()[index].size()){
            return endState.getPositions().getPositions()[index].size();
        }
        else {
            return compareState.getPositions().getPositions()[index].size();
        }
    }



    private void removeAlreadyVisited() {
        this.queue.removeIf(state -> this.alreadyVisited.contains(state.getPositions()));
    }

    private void removeAlreadyVisitedStack() {
        this.stack.removeIf(state -> this.alreadyVisited.contains(state.getPositions()));
    }


}