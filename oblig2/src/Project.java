/***
 * @author Roar Hoksnes Eriksen
 */

import java.lang.reflect.Array;
import java.util.*;

public class Project {
	private Task[] tasks, tmpTasks;
    private ArrayList<Task> topSort = new ArrayList<Task>();
    private final int RUNNING = 1, FINISHED = 2, NOT_STARTED = 3;
    public int projectRunningTime;

    /***
     * Class constructor
     *
     * @param tasks The array representing the graph of this project
     */
    public Project(Task[] tasks) {
        this.tasks = tasks;
        tmpTasks = tasks;

        resetAllTasks();
        checkIfReliazable();
        sortTopologically();

        resetAllTasks();
        calculateEarliestStart();
        setProjectRunningTime();
        calculateLatestStart();

        resetAllTasks();
        runProject();

        listTasks();
	}

    /***
     * Resets all tasks'e states.
     */
    private void resetAllTasks() {
        for (Task t : tasks) {
            t.state = NOT_STARTED;
            t.resetDependencies();
        }
    }

    /***
     * Finds the task with the largest combined earliest start and running time,
     * and sets that value as the whole project's best possibe running time.
     *
     * @see Task#getEarliestStart()
     * @see Task#getTime()
     */
    private void setProjectRunningTime() {
        int cnt = 0;
        for (Task t : tasks) {
            if (t.getEarliestStart()+t.getTime() > cnt) {
                cnt = t.getEarliestStart()+t.getTime();
            }
        }
        projectRunningTime = cnt;
    }

    /***
     * Initializes the check for cycles in the graph.
     *
     * @see #checkForCycle(Task, Task)
     */
    public void checkIfReliazable() {

        for (Task t : tasks) {
            checkForCycle(t, null);
        }
        System.out.println("No cycle detected. Project is realizable!");
    }

    /***
     * Recursive method to check for cycles in the graph. A task has tree possible states;
     * STARTED (is currently checking for cycles), UNVISITED (hasn't been checked yet) and
     * FINISHED (the task has been checked and cleared for cycles). If entering a unvisited task,
     * the STARTED-mark is set, and by iterating through each neighbours, one can see if a neighbour
     * of the current task, by recursively checking each possible path of this neighbour, is ending
     * up back at the parent-node. If so, the STARTED-mark is set, and a cycle has been detected.
     * If not, the search continues until all the tasks' mark is set to FINISHED.
     *
     * @param task The task in which the check for cycles should start from.
     * @param cameFrom The previous task, used to backtrace a possible cycle.
     * @see #printCycleTrace(Task)
     * @see Edge
     * @see Task#getId()
     */
	public void checkForCycle(Task task, Task cameFrom) {
        Edge tmp = task.getOutEdges();

        if (cameFrom == null)
            cameFrom = task;
        else
            cameFrom.previous = task;

        if (task.state == RUNNING) {
            System.out.println("Cycle detected at task " + task.getId());
            printCycleTrace(task);
            System.exit(1);
        } else if (task.state == NOT_STARTED) {
            task.state = RUNNING;
            for (Edge e = tmp; e != null; e = e.next) {
                checkForCycle(e.getTaskTo(), cameFrom);
            }
            task.state = FINISHED;
        }
	}

    /***
     * Iterates through the linked list of tasks in the cycle created in
     * checkForCycle(), to spot where a cycle has been detected.
     *
     * @param cameFrom The task in which a cycle has been detected
     */
    private void printCycleTrace(Task cameFrom) {
        System.out.println("Printing trace: ");
        Task tmp = cameFrom;
        while (tmp != null) {
            System.out.print(tmp.getId() + " -> ");
            tmp = tmp.previous;
        }

        System.out.println();
    }

    /***
     * Calculates each task's earliest start recursively, by starting at each task with zero indegrees.
     * If the graph has no cycles, one can be sure that every task with an indegree > 0 will
     * be visited if one starts with the task's with zero indegrees.
     *
     * @see Task#calculateEarliestStart(int, Task)
     */
    private void calculateEarliestStart() {

        for (Task t : tasks) {
            if (t.getOutEdges() != null) {
                t.calculateEarliestStart(0, t);
            }
        }
    }

    /***
     *
     * @see Task#calculateLatestStart(Project)
     */
    private void calculateLatestStart() {
        int size = topSort.size();

        for (int i = size - 1; i >= 0; i--) {
            topSort.get(i).calculateLatestStart(this);
        }
    }

    /***
     * Sorts the graph topologically, keeping a list
     * with a increasing number of dependencies (indegrees).
     * Having such a list is necessary for calculating
     * each task's latest start, where the list is iterated
     * through from end to beginning in order to get the correct
     * latest start.
     *
     *
     * @see Edge
     * @see Edge#getTaskTo()
     * @see Task#decreasePredecessors()
     * @see Task#hasDependencies()
     */
    private void sortTopologically() {
        ArrayList<Task> queue = new ArrayList<Task>();

        for (Task t : tasks) {
            if (!t.hasDependencies()) {
                queue.add(t);
            }
        }

        while (!queue.isEmpty()) {
            Task tmp = queue.remove(0);
            topSort.add(tmp);

            for (Edge edge = tmp.getOutEdges(); edge != null; edge = edge.next) {
                Task neighbour = edge.getTaskTo();
                if (neighbour.decreasePredecessors() == 0) {
                    queue.add(neighbour);
                }
            }
        }
    }

    /***
     * Prints the project development. There are many "unnecessary" for loops and if statements
     * checking that every task has their chance to either run or finish at the current
     * time unit.
     *
     * The whole principle of the method is based on topological sort, where only
     * the tasks with no dependencies are added to the queue (and thereby allowed to run), and removed
     * from the queue when they are finished.
     *
     * However, each task's earliest start is also something to consider, so
     * even if a task is in the queue, it isn't necessarily their turn to run.
     * So each task's earliest start and the project current time unit is also something to
     * consider.
     *
     * @see Task#hasDependencies()
     * @see Task#getId()
     * @see Task#getStaff()
     * @see Task#getEarliestStart()
     * @see Task#getTime()
     *
     */
    private void runProject() {
        int timeUnits = 0, currStaff = 0, tmpStaff = 0, tmpTime = 0;

        /***
         * The queue will always be filled with tasks that has no dependencies (indegrees)
         */
        ArrayList<Task> queue = new ArrayList<Task>();

        for (Task t : tasks) {
            if (!t.hasDependencies()) {
                queue.add(t);
            }
        }

        while (timeUnits <= projectRunningTime) {
            boolean increaseTime = true;

            if (timeUnits != tmpTime) {
                System.out.println("\nTime: " + timeUnits + " ************");
            }

            tmpStaff = currStaff;
            tmpTime = timeUnits;

            /***
             * Checking which tasks that are ready to start at the current time unit,
             * and changing their state to RUNNING. Increasing the current
             * working staff on the project
             */


            for (Task t : queue) {
                if (t.getEarliestStart() == timeUnits && t.state == NOT_STARTED) {
                    System.out.println("\t\tStarting:   " + t.getId());
                    t.state = RUNNING;
                    currStaff += t.getStaff();
                }
            }

            /***
             * Checking which tasks that are finished working at the current time unit.
             * Using a iterator-object to be able to modify the queue while iterating through it.
             * By using a Iterator i avoid getting the awesome ConcurrentModifierException, and
             * iterating trough the list an unnecessary number of times. Each task that finishes,
             * is of course removed from the queue.
             *
             * When a task is finished, it will decrease its children's predecessor, just
             * like a topoligical sorting would to. Thus, this part is a (kind of) version
             * of topological sorting.
             */

            ListIterator<Task> queueIterator = queue.listIterator();
            while (queueIterator.hasNext()) {
                Task t = queueIterator.next();
                if (t.getEarliestStart() + t.getTime() == timeUnits && t.state == RUNNING) {
                    System.out.println("\t\tFinished:   " + t.getId());
                    t.state = FINISHED;
                    t.decreaseMyChildrensDepencies();
                    currStaff -= t.getStaff();
                    queueIterator.remove();
                }
            }

            /***
             * Finding tasks with no indegrees that has not been startet yet and adding them to the queue.
             * By not increasing the time, i avoid the risk of "skipping" a task that has been added at a
             * moment when the time 'should' have been updated, and by doing that denying a task its change
             * to work at its earliest start.
             */

            for (Task tt : tasks) {
                if (!tt.hasDependencies() && tt.state == NOT_STARTED) {
                    queue.add(tt);
                    if (tt.getEarliestStart() == timeUnits) {
                        increaseTime = false;
                    }
                }
            }
            if (increaseTime) timeUnits++;

            /***
             * Just to avoid printing current staff when there isn't much happening
             */
            if (tmpStaff != currStaff) {
                System.out.println("\tCurrent staff:  " + currStaff);
                System.out.println("*********************");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        for (Task t : tmpTasks) {
            if (t.state == FINISHED)
                System.out.println("Task: "+ t.getId() + "'s state: " + FINISHED + " (FINISHED).");
            else if (t.state == RUNNING)
                System.out.println("Task: "+ t.getId() + "'s state: " + RUNNING + " (RUNNING).");
            else if (t.state == NOT_STARTED)
                System.out.println("Task: "+ t.getId() + "'s state: " + NOT_STARTED + " (NOT_STARTED).");
        }

    }

    /***
     * Iterates through each task (sorted by ID), and prints the info required
     * from the assignment (3.3).
     *
     * @see Task#getId()
     * @see Task#getName()
     * @see Task#getTime()
     * @see Task#getStaff()
     * @see Task#getSlack()
     * @see Task#getEarliestStart()
     * @see Task#getLatestStart()
     * @see Task#getEarliestStart()
     * @see Task#getOutEdges()
     * @see Edge
     *
     */
    private void listTasks() {
        System.out.println("Printing every task in the project.\n");
        for (Task t : tasks) {
            System.out.println("***** " + t.getId() + " " + t.getName() + " *****");
            System.out.println("------------------------------");
            System.out.println("Time estimate: \t\t\t" + t.getTime());
            System.out.println("Manpower: \t\t\t\t" + t.getStaff());
            System.out.println("Slack: \t\t\t\t\t" + t.getSlack());
            System.out.println("Earliest starting time: " + t.getEarliestStart());
            System.out.println("Latest starting time: \t" + t.getLatestStart());
            System.out.print("Tasks dependent on this task: ");
            for (Edge e = t.getOutEdges(); e != null; e = e.next) {
                System.out.print(e.getTaskTo().getId());
                if (e.next == null) {
                    System.out.print(".");
                } else {
                    System.out.print(", ");
                }
            }
            if (t.getOutEdges() == null) {
                System.out.println("None.");
            }
            System.out.println("\n");
        }
    }
}