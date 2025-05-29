package core.IntevalCpus;

public class FcaiIntervalCpu extends IntervalCpu {
    public int UpdatedQuantam;
    public int Priority;
    public int FcaiFactor;
    public int prevQ;

    @Override
    public void Print() {
        String formattedString = String.format("%d -> %d | P%d | %d | %d | %d -> %d | %d | %d",
                startTime,
                endTime,
                Pnum,
                endTime - startTime,
                RemainingBurstTime,
                prevQ,
                UpdatedQuantam,
                Priority,
                FcaiFactor);
        System.out.println(formattedString);
    }
}
