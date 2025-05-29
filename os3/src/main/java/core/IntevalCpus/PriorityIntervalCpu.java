package core.IntevalCpus;

public class PriorityIntervalCpu extends IntervalCpu {
    public int Priority;

    @Override
    public void Print() {
        String formattedString = String.format("%-4d | P%-6d | %-13d | %-13d  | %-9d| %-11d  | %-13d",
                startTime,
                Pnum,
                endTime - startTime,
                RemainingBurstTime,
                Priority,
                waitingTime,
                turnAroundTime);
        System.out.println(formattedString);
    }
}
