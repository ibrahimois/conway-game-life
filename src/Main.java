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
//    public static final int gridLength = 25;
    public static final int WIDTH = 110;
    public static final int HEIGHT = 28;
    public static final String OS = System.getProperty("os.name");


    public static void main(String[] args) throws InterruptedException, IOException {

        //Here we create our board with a glider in place, a glider is a shape in the game you can check the previous link.
        //The glider is defined for 50 * 50 grid.
//        List<String> cols = IntStream.range(0, WIDTH * HEIGHT)
//                .mapToObj(i -> (i == 1 || i == WIDTH + 2 || i == WIDTH * 2 || i == (WIDTH * 2) + 1 || i == (WIDTH * 2) + 2
//                        || i == ((WIDTH * HEIGHT) / 2) + WIDTH / 2 || i == (((WIDTH * HEIGHT) / 2) + WIDTH / 2) + 1 || i == (((WIDTH * HEIGHT) / 2) + WIDTH / 2) + 2)  ? "X" : " ")
//                .collect(Collectors.toList());

        //glider gun
        List<String> cols = IntStream.range(0, WIDTH * HEIGHT)
                .mapToObj(i -> (
                        i == (WIDTH) + 26 ||
                        i == (WIDTH * 2) + 26 || i == (WIDTH * 2) + 24 ||
                        i == (WIDTH * 3) + 22 || i == (WIDTH * 3) + 23 || i == (WIDTH * 3) + 36 || i == (WIDTH * 3) + 37 || i == (WIDTH * 3) + 14 || i == (WIDTH * 3) + 15 ||
                        i == (WIDTH * 4) + 13 || i == (WIDTH * 4) + 17 || i == (WIDTH * 4) + 22 || i == (WIDTH * 4) + 23 || i == (WIDTH * 4) + 36 || i == (WIDTH * 4) + 37 ||
                        i == (WIDTH * 5) + 12 || i == (WIDTH * 5) + 18 || i == (WIDTH * 5) + 22 || i == (WIDTH * 5) + 23 || i == (WIDTH * 5) + 2 || i == (WIDTH * 5) + 3 ||
                        i == (WIDTH * 6) + 12 || i == (WIDTH * 6) + 16 || i == (WIDTH * 6) + 18 || i == (WIDTH * 6) + 19 || i == (WIDTH * 6) + 24 || i == (WIDTH * 6) + 26 ||
                                i == (WIDTH * 6) + 2 || i == (WIDTH * 6) + 3 ||

                        i == (WIDTH * 7) + 12 || i == (WIDTH * 7) + 18 || i == (WIDTH * 7) + 26 ||
                        i == (WIDTH * 8) + 13 || i == (WIDTH * 8) + 17 ||
                        i == (WIDTH * 9) + 14 || i == (WIDTH * 9) + 15)  ? "O" : " ")
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
                int numberNeighborsAlive = checkNeighbors(cols, i, looper -1);

                //After getting number of neighbors for cell, we check if it's alive or dead.
                //If it is alive we put into our map of possible changes.
                //We apply a simple condition to not put data we wouldn't be using, for example if a cell is alive and doesn't satisfy the rules,
                // no need to store it for later changes, also we can't change it here cause changes happen over generation and not on cell state
                // at the moment.
                if(cols.get(i).equalsIgnoreCase("O")){
                    if(numberNeighborsAlive != 2 && numberNeighborsAlive != 3) {
                        changes.put(i, cols.get(i));
                    }
                } else {
                    if(numberNeighborsAlive == 3) {
                        changes.put(i, cols.get(i));
                    }
                }

                //Since we are already looping throughout the board, might as well apply our looper divider for visual clearance.
                if(looper == WIDTH) {
                    System.out.println("██");
                    looper = 0;
                }                looper++;
            }

            //We loop throughout our changes and make them.
            for (int key :
                    changes.keySet()) {

                //Swap alive cells with dead ones, and swap dead ones with alive.
                if(changes.get(key).equalsIgnoreCase("O")) {
                    cols.set(key, " ");
                } else {
                    cols.set(key, "O");
                }
            }
            for(int i = 0; i < WIDTH + 4 ; i++) {
            System.out.print("█");
            }
            Thread.sleep(150);
            //Generation reload terminal.
            if (OS.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
            for (int i = 0; i < WIDTH + 4; i++) {
            System.out.print("█");
            }
            System.out.println();
            //Add delay between iterations.
        }
    }

    private static int checkNeighbors(List<String> cols, int i, int looper) {
        List<String> neighbors = new ArrayList<>();

        if(i == 0) {
            topLeftCorner(cols, i, neighbors);
        } else if (i == WIDTH - 1) {
            topRightCorner(cols, i, neighbors);
        } else if (i == (HEIGHT * WIDTH) - (WIDTH)) {
            bottomLeftCorner(cols, i, neighbors);
        } else if (i == (WIDTH * HEIGHT) - 1) {
            bottomRightCorner(cols, i, neighbors);
        } else if (i < WIDTH) {
            topEdge(cols, i, neighbors);
        } else if (i % WIDTH == 0) {
            leftEdge(cols, i, neighbors);
        } else if (i > (HEIGHT * WIDTH) - (WIDTH)) {
            bottomEdge(cols, i, neighbors);
        } else if (i % WIDTH == WIDTH - 1) {
            rightEdge(cols, i, neighbors);
        } else {
            grid(cols, i, neighbors);
        }
        int aliveNeighborsCount = 0;
        for (String col :
                neighbors) {
            if(col.equalsIgnoreCase("O"))
                aliveNeighborsCount++;
        }
        if(looper == 0) {
            System.out.print("██");
        }
        System.out.print(cols.get(i) + "");
        return aliveNeighborsCount;
    }

    private static void rightEdge(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - WIDTH - 1));
        neighbors.add(cols.get(i - WIDTH));
        neighbors.add(cols.get(i - 1));
        neighbors.add(cols.get(i + WIDTH - 1));
        neighbors.add(cols.get(i + WIDTH));
    }

    private static void bottomEdge(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - WIDTH - 1));
        neighbors.add(cols.get(i - WIDTH));
        neighbors.add(cols.get(i - WIDTH + 1));
        neighbors.add(cols.get(i - 1));
        neighbors.add(cols.get(i + 1));
    }

    private static void leftEdge(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - WIDTH));
        neighbors.add(cols.get(i - WIDTH + 1));
        neighbors.add(cols.get(i + 1));
        neighbors.add(cols.get(i + WIDTH));
        neighbors.add(cols.get(i + WIDTH + 1));
    }

    private static void bottomRightCorner(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - WIDTH));
        neighbors.add(cols.get(i - HEIGHT - 1));
        neighbors.add(cols.get(i - 1));
    }

    private static void bottomLeftCorner(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - WIDTH));
        neighbors.add(cols.get(i - WIDTH + 1));
        neighbors.add(cols.get(i + 1));
    }

    private static void topRightCorner(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - 1));
        neighbors.add(cols.get(i + WIDTH - 1));
        neighbors.add(cols.get(i + WIDTH));
    }

    private static void topEdge(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - 1));
        neighbors.add(cols.get(i + 1));
        neighbors.add(cols.get(i + WIDTH - 1));
        neighbors.add(cols.get(i + WIDTH));
        neighbors.add(cols.get(i + WIDTH + 1));
    }

    private static void topLeftCorner(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i + 1));
        neighbors.add(cols.get(i + WIDTH));
        neighbors.add(cols.get(i + WIDTH + 1));
    }

    private static void grid(List<String> cols, int i, List<String> neighbors) {
        neighbors.add(cols.get(i - WIDTH - 1));
        neighbors.add(cols.get(i - WIDTH ));
        neighbors.add(cols.get(i - WIDTH + 1));
        neighbors.add(cols.get(i - 1));
        neighbors.add(cols.get(i + 1));
        neighbors.add(cols.get(i + WIDTH - 1));
        neighbors.add(cols.get(i + WIDTH ));
        neighbors.add(cols.get(i + WIDTH + 1));
    }
}