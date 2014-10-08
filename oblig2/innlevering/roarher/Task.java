/***
 * @author Roar Hoksnes Eriksen
 */

import java.util.ArrayList;

public class Task {
	private int id, time, staff, earliestStart, latestStart, cntPredecessors, tmpPredecessors;
    private ArrayList<Task> earliestStartSet;
	private String name;
	private Edge outEdges;
	public int state;
	private boolean allInfo = false;

    /***
     * Class constructor
     *
     * @param id The unique ID of the task
     * @param name The name of the task
     * @param time The time needed for the task to complete
     * @param staff The manpower needed by this task
     */
	public Task(int id, String name, int time, int staff) {
		this.name = name;
		this.id = id;
		this.time = time;
		this.staff = staff;
		allInfo = true;
        earliestStartSet = new ArrayList<Task>();
	}

    /***
     * Class constructor used to declare "temporary" task, before the rest of the variable fields
     * is set in the method addAdditionalInfo.
     *
     * @see #addAdditionalInfo(String, int, int)
     * @param id The unique ID of the task
     */
    public Task(int id) {
		this.id = id;
        earliestStartSet = new ArrayList<Task>();
	}

    /***
     * Adds additional info to the tasks with the second constructor.
     *
     * @see #Task(int)
     * @param name The name of the task
     * @param time The time needed for the task to complete
     * @param staff The manpower needed by this task
     */
    public void addAdditionalInfo(String name, int time, int staff) {
        allInfo = true;
        this.name = name;
        this.time = time;
        this.staff = time;
    }

    /***
     * Checks if this task has all the info needed (the paramaters in the
     * first constructor)
     *
     * @return boolean If all field required has been filled.
     */
	public boolean hasAllInfo() {
		return allInfo;
	}

    /***
     *
     * @return String This task's name.
     */
	public String getName() {
		return name;
	}

    /***
     *
     * @return int This task's unique ID.
     */
    public int getId() {
		return id;
	}

    /***
     * Checks whether or not this task is critical by
     * subtracting the earliest possible start with
     * the latest possible start. If the sum is zero,
     * this task is critical.
     *
     * @return boolean
     */
    public boolean isCritical() {
		return earliestStart-latestStart == 0; 
	}

    /***
     * By knowing the current task's indegrees and keeping a list of which of the
     * predecessors who has been here and updated the earliestStart, I avoid
     * updating earliestStart twice (or more) as by iterating the list in Project,
     * I am very likely to visit the same task twice (or more) with the same predecessor.
     *
     * By also checking when the earliestStartSet list is full (== cntPredecessors), I can
     * make sure that i don't update the tasks dependent on the current task's earliestStart
     * before all predecessors has updated the current node's earliestStart.
     *
     * Probably not the most efficient of algorithms, by at least it works!
     *
     * @param increaseTime An updated Integer representing the earliestStart of the previous node
     * @param prev The previous node (the current node's predecessor)
     */
    public void calculateEarliestStart(int increaseTime, Task prev) {
        if (!earliestStartSet.contains(prev) && prev.id != id) {
            if (earliestStart < increaseTime) {
                earliestStart = increaseTime;
            }
            earliestStartSet.add(prev);
        }

        if (earliestStartSet.size() == cntPredecessors){
            for (Edge tmp = outEdges; tmp != null; tmp = tmp.next) {
                Task dependent = tmp.getTaskTo();
                dependent.calculateEarliestStart(earliestStart + time, this);
            }
        }
    }

    /***
     * The first check is to see if a task doesn't have any dependencies.
     * In that case, the slack  is set to the running time of the
     * project, minus its earliest start and time to complete its work.
     *
     * If it has dependecies, it is necessary to find the child with
     * the lowest earliest start. After that, the tasks slack
     * is set to that value, minus its earliest start and time to complete its
     * work, just like the case above.
     *
     * In both cases, the tasks latest start is its earliest start plus
     * its calculated slack.
     *
     * @param projectRunningTime The total running time of the project.
     */
    public void calculateLatestStart(int projectRunningTime) {
        int slack;
        if (outEdges == null) {
            slack = projectRunningTime - earliestStart - time;
        } else {
            int minOfChildren = Integer.MAX_VALUE;
            for (Edge e = outEdges; e != null; e = e.next) {
                if (e.getTaskTo().earliestStart < minOfChildren) {
                    minOfChildren = e.getTaskTo().earliestStart;
                }
            }
            slack = minOfChildren - earliestStart  - time;
        }
        latestStart = earliestStart + slack;

    }

    /***
     *
     * @return The task's earliest starting time in relation to its predecessors
     */

    public int getEarliestStart() {
        return earliestStart;
    }

    /***
     *
     * @return The task's latest possible start before delaying the project
     */
    public int getLatestStart() {
        return latestStart;
    }

    /***
     *
     * @return int The difference between the tasks latest start time and earliest start time.
     */
    public int getSlack() {
        return latestStart-earliestStart;
    }

    /***
     *
     * @return int The task's required manpower.
     */
    public int getStaff() {
		return staff;
	}

    /***
     *
     * @return int The time the task needs to complete its work
     */
    public int getTime() {
		return time;
	}

    /***
     * Sets the number of predecessors
     *
     * @param cnt The total number of predecessors
     */
    public void setCntPredecessors(int cnt) {
        cntPredecessors = cnt;
        tmpPredecessors = cntPredecessors;
    }

    /***
     * Checks if the task has dependencies (predecessors).
     *
     * @return boolean
     */
    public boolean hasDependencies() {
        return cntPredecessors != 0;
    }

    /***
     *
     * @return int The number of predecessors
     */
    public int getPredecessorsCnt() {
        return cntPredecessors;
    }

    /***
     * Uses a temporarly variable in order to maintain the original number of predecessors for each task.
     *
     * @return int Representing the number of predecessors
     */
    public int decreasePredecessors() {
        return --cntPredecessors;
    }

    public void resetDependencies() {
        cntPredecessors = tmpPredecessors;
    }
    /***
     * Decreases the number of predecessors each of the task's outdegrees has.
     *
     */
    public void decreaseMyChildrensDepencies() {
        for (Edge e = outEdges; e != null; e = e.next) {
            e.getTaskTo().decreasePredecessors();
        }
    }
    /***
     * Adds a edge to the task's linked list of edges.
     *
     * @param edge The edge to be added to the list.
     */
    public void addOutEdge(Edge edge) {
        if (outEdges == null) {
            outEdges = edge;
        } else {
            edge.next = outEdges;
            outEdges = edge;
        }
    }

    /***
     *
     * @return Edge The linked list with the task's edges.
     */
    public Edge getOutEdges() {
        return outEdges;
    }


    /***
     * Iterates the linked list of edges, and prints them.
     *
     */
    public void printEdges() {
        Edge tmpEdge = outEdges;

        System.out.print(name + " med id: " + id + " har følgende naboer: ");
        while (tmpEdge != null) {

            System.out.print(tmpEdge.getTaskTo().getId() + " ");
            tmpEdge = tmpEdge.next;
        }
        System.out.println();
    }
}
