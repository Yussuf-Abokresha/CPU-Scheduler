package core.IntevalCpus;

public class SrtfIntervalCpu extends IntervalCpu {

    @Override
    public void Print() {
        String formattedString = String.format("%d -> %d | P%d | %d | %d | %s",
                startTime,
                endTime,
                Pnum,
                endTime - startTime,
                RemainingBurstTime,
                ActionDetail);
        System.out.println(formattedString);
    }

}
