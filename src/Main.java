import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
/* this code if demo for the game of life
the rules of the game of life is simple :
    we divide our campus into a grid of cells and based on the state of the cell we follow the rules
  - 1 : If the cell is alive, then it stays alive if it has either 2 or 3 live neighbors
  - 2 : If the cell is dead, then it springs to life only in the case that it has 3 live neighbors
  we check the neighbors in a 3x3 grid.

  for more info look into https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
 */
public class Main {

    //This holds the grid size
    public static final int gridLength = 25;


    public static void main(String[] args) throws InterruptedException, IOException {

        //Here we create our board with a glider in place, a glider is a shape in the game you can check the previous link.
        //The glider is defined for 50 * 50 grid.
        List<String> cols = IntStream.range(0, gridLength * gridLength)
                .mapToObj(i -> (i == 1 || i == 27 || i == 50 || i == 51 || i == 52 ) ? "X" : "*")
                .collect(Collectors.toList());

        //This loop will keep going and each iteration holds a generation.
        while (true) {
            //This value is to divide each line for visual purposes
            int looper = 1;

            //Here we define a map that holds values and indices of cells that might be changed.
            HashMap<Integer, String> changes = new HashMap<>();

            //Looping through our board to calculate neighbors for each cell.
            for (int i = 0; i < cols.size(); i++) {

                //Calculation happens in checkNeighbors method.
                int numberNeighborsAlive = checkNeighbors(cols, i);

                //After getting number of neighbors for cell, we check if it's alive or dead.
                //If it is alive we put into our map of possible changes.
                //We apply a simple condition to not put data we wouldn't be using, for example if a cell is alive and doesn't satisfy the rules,
                // no need to store it for later changes, also we can't change it here cause changes happen over generation and not on cell state
                // at the moment.
                if(cols.get(i).equalsIgnoreCase("X")){
                    if(numberNeighborsAlive != 2 && numberNeighborsAlive != 3) {
                        changes.put(i, cols.get(i));
                    }
                } else {
                    if(numberNeighborsAlive == 3) {
                        changes.put(i, cols.get(i));
                    }
                }

                //Since we are already looping throughout the board, might as well apply our looper divider for visual clearance.
                if(looper == gridLength) {
                    System.out.println();
                    looper = 0;
                }
                looper++;
            }

            //We loop throughout our changes and make them.
            for (int key :
                    changes.keySet()) {

                //Swap alive cells with dead ones, and swap dead ones with alive.
                if(changes.get(key).equalsIgnoreCase("X")) {
                    cols.set(key, "*");
                } else {
                    cols.set(key, "X");
                }
            }
            Thread.sleep(150);
            //Generation reload terminal.
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
            //Add delay between iterations.
        }
    }

    private static int checkNeighbors(List<String> cols, int i) {
        List<String> neighbors = new ArrayList<>();

        if(i == 0) {
            topLeftCorner(cols, i, neighbors);
        } else if (i == gridLength - 1) {
            topRightCorner(cols, i, neighbors);
        } else if (i == gridLength * (gridLength - 1)) {
            bottomLeftCorner(cols, i, neighbors);
        } else if (i == (gridLength * gridLength) - 1) {
            bottomRightCorner(cols, i, neighbors);
        } else if (i < gridLength) {
            topEdge(cols, i, neighbors);
        } else if (i % gridLength == 0) {
            leftEdge(cols, i, neighbors);
        } else if (i > gridLength * (gridLength - 1)) {
            bottomEdge(cols, i, neighbors);
        } else if (i % gridLength == gridLength - 1) {
            rightEdge(cols, i, neighbors);
        } else {
            grid(cols, i, neighbors);
        }
        int aliveNeighborsCount = 0;
        int looper = 1;
        for (String col :
                neighbors) {
            if(col.equalsIgnoreCase("X"))
                aliveNeighborsCount++;
        }
        System.out.print(cols.get(i) + "");
        return aliveNeighborsCount;
    }

    private static void rightEdge(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - gridLength - 1));
        neighbors.add(cols.get(i - gridLength));
        neighbors.add(cols.get(i - 1));
        neighbors.add(cols.get(i + gridLength - 1));
        neighbors.add(cols.get(i + gridLength));
    }

    private static void bottomEdge(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - gridLength - 1));
        neighbors.add(cols.get(i - gridLength));
        neighbors.add(cols.get(i - gridLength + 1));
        neighbors.add(cols.get(i - 1));
        neighbors.add(cols.get(i + 1));
    }

    private static void leftEdge(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - gridLength));
        neighbors.add(cols.get(i - gridLength + 1));
        neighbors.add(cols.get(i + 1));
        neighbors.add(cols.get(i + gridLength));
        neighbors.add(cols.get(i + gridLength + 1));
    }

    private static void bottomRightCorner(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - gridLength));
        neighbors.add(cols.get(i - gridLength - 1));
        neighbors.add(cols.get(i - 1));
    }

    private static void bottomLeftCorner(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - gridLength));
        neighbors.add(cols.get(i - gridLength + 1));
        neighbors.add(cols.get(i + 1));
    }

    private static void topRightCorner(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - 1));
        neighbors.add(cols.get(i + gridLength - 1));
        neighbors.add(cols.get(i + gridLength));
    }

    private static void topEdge(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - 1));
        neighbors.add(cols.get(i + 1));
        neighbors.add(cols.get(i + gridLength - 1));
        neighbors.add(cols.get(i + gridLength));
        neighbors.add(cols.get(i + gridLength + 1));
    }

    private static void topLeftCorner(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i + 1));
        neighbors.add(cols.get(i + gridLength));
        neighbors.add(cols.get(i + gridLength + 1));
    }

    private static void grid(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - gridLength - 1));
        neighbors.add(cols.get(i - gridLength ));
        neighbors.add(cols.get(i - gridLength + 1));
        neighbors.add(cols.get(i - 1));
        neighbors.add(cols.get(i + 1));
        neighbors.add(cols.get(i + gridLength - 1));
        neighbors.add(cols.get(i + gridLength ));
        neighbors.add(cols.get(i + gridLength + 1));
    }
}