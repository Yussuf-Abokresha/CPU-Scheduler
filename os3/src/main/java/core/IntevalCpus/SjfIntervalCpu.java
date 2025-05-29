package core.IntevalCpus;

public class SjfIntervalCpu extends IntervalCpu {

    @Override
    public void Print() {
        String formattedString = String.format("%d -> %d | P%d | %d",
                startTime,
                endTime,
                Pnum,
                endTime - startTime);
        System.out.println(formattedString);
    }

}
