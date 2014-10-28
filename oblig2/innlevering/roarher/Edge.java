/***
 * @author Roar Hoksnes Eriksen
 */

public class Edge {
    private Task taskFrom, taskTo;
    public Edge next;

    /***
     *
     * @param taskFrom The task at the start of the edge
     * @param taskTo The task the edge is pointing to
     */
    public Edge (Task taskFrom, Task taskTo) {
        this.taskFrom = taskFrom;
        this.taskTo = taskTo;
    }

    /***
     *
     * @return Task The task the edge is pointing to
     */
    public Task getTaskTo() {
        return taskTo;
    }

    /***
     *
     * @return Task The task at the start of the edge
     */
    public Task getTaskFrom() {
        return taskFrom;
    }
}
