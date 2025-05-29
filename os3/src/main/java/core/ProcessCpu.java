package core;

public class ProcessCpu {
    public int PNum;
    public int BurstTime;
    public int ArrivalTime;
    public int Priority;
    public int Quantum;

    public ProcessCpu() {

    }

    public ProcessCpu(ProcessCpu other) {
        this.PNum = other.PNum;
        this.BurstTime = other.BurstTime;
        this.ArrivalTime = other.ArrivalTime;
        this.Priority = other.Priority;
        this.Quantum = other.Quantum;
    }
}
