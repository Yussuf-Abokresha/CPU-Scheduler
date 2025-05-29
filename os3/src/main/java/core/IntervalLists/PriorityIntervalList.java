package core.IntervalLists;

public class PriorityIntervalList extends IntervalList {

    @Override
    protected void printHeader() {
        System.out.println(
                "Time | Process | Executed Time | Remaining Time | Priority | Waiting Time | TurnAround Time");
    }

}
