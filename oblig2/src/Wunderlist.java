/***
 * @author Roar Hoksnes Eriksen
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Wunderlist {

	private Task[] tasks;

	public static void main(String[] args) {
        Wunderlist projectPlanner = new Wunderlist();
        projectPlanner.readFile(args);
        projectPlanner.makeProject();
    }

    /***
     * Reads the file, declares the tasks, their time estimates, manpower and
     * dependencies, and stores them in an array representing the graph.
     *
     * @param args Command line arguments
     */
    public void readFile(String [] args) {
        if (args.length == 0) {
            System.out.println("Her har du nok glemt å sende ved et par argumenter kompis!");
            System.exit(1337);
        }
        String filename = args[0];
        int manpower = 0;
        try {
            manpower = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("args[1] er ikke en Integer! Fiks!");
            System.exit(1337);
        }
        File file = new File(filename);
        Scanner read = null;

        try {
            read = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Fil ikke funnet!");
            System.exit(1337);
        }

        int taskCount = read.nextInt();
        tasks = new Task[taskCount];

        read.nextLine();
        while (read.hasNextLine()) {
            int taskID;
            try {
                taskID = read.nextInt();
            } catch (NoSuchElementException e) {
                break;
            }
            String taskName = read.next();
            int timeEstimate = read.nextInt();
            int manpowerReq = read.nextInt();

            /***
             * Checks if task has been made as a result of being a predecessor of another task.
             * Probably not the most generic way of indexing the array, but in this case,
             * where taskID-1 represents the index in the array for each of the three files,
             * I found it reasonable to do it like this instead of using a counter.
             */
            if (tasks[taskID-1] == null) {
                tasks[taskID-1] = new Task(taskID, taskName, timeEstimate, manpowerReq);
            /***
             * Fills additional info to the tasks with only taskID present. This situation happens
             * with tasks that were predecessors to another task before the "creation" of themselves
             */
            } else if (!tasks[taskID-1].hasAllInfo()) {
                tasks[taskID-1].addAdditionalInfo(taskName, timeEstimate, manpowerReq);
            }
            int predecessorID = read.nextInt();
            /***
             * Makes "temporary" tasks of the tasks predecessing the main task on that line
             * (if the temporary task hasn't already been declared)
             *
             */
            int predecessorCnt = 0;

            /***
            * Sets each task's dependencies (indegrees).
            */
            while (predecessorID != 0) {
                if (tasks[predecessorID-1] == null) {
                    tasks[predecessorID-1] = new Task(predecessorID);
                }

                tasks[predecessorID-1].addOutEdge(new Edge(tasks[predecessorID-1], tasks[taskID-1]));
                predecessorID = read.nextInt();

                predecessorCnt++;

            }
            tasks[taskID-1].setCntPredecessors(predecessorCnt);
        }
    }

    /***
     * Initializes the project with the newly created array representing the graph
     *
     */
    public void makeProject() {
        new Project(tasks);
    }
}
