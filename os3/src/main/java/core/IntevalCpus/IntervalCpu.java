package core.IntevalCpus;

public abstract class IntervalCpu {
    public int startTime;
    public int endTime;
    public int Pnum;
    public int RemainingBurstTime;
    public String ActionDetail;
    public int waitingTime;
    public int turnAroundTime;
    public int actualBurst;
    public int executedTime() {
        return endTime - startTime;
    }

    public abstract void Print();
}
