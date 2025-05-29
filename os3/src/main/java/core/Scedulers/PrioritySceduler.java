package core.Scedulers;

import java.util.Comparator;
import java.util.LinkedList;

import core.ProcessCpu;
import core.IntervalLists.IntervalList;
import core.IntervalLists.PriorityIntervalList;
import core.IntevalCpus.PriorityIntervalCpu;

public class PrioritySceduler extends CpuSceduler {
    private PriorityIntervalList intervals;

    public PrioritySceduler(LinkedList<ProcessCpu> process) {
        super(process);
        this.intervals = new PriorityIntervalList();
    }

    @Override
    public IntervalList Simulate() {
        process.sort(Comparator.comparingInt((ProcessCpu p) -> p.ArrivalTime));

        int currentTime = 0, arrivalTime = 0, burstTime = 0, contextSwitchTime = 1;
        while (!process.isEmpty()) {
            ProcessCpu highestPriorityProcess = null;
            for (ProcessCpu pro : process) {
                if (pro.ArrivalTime <= currentTime) {
                    if (highestPriorityProcess == null || pro.Priority < highestPriorityProcess.Priority) {
                        highestPriorityProcess = pro;
                        arrivalTime = pro.ArrivalTime;
                        burstTime = pro.BurstTime;
                    }
                }
            }

            if (highestPriorityProcess == null) {
                currentTime++;
                continue;
            }

            PriorityIntervalCpu intervalCpu = new PriorityIntervalCpu();
            intervalCpu.Pnum = highestPriorityProcess.PNum;
            intervalCpu.Priority = highestPriorityProcess.Priority;
            intervalCpu.startTime = currentTime;
            intervalCpu.endTime = currentTime + highestPriorityProcess.BurstTime;
            intervalCpu.turnAroundTime = intervalCpu.endTime - arrivalTime;
            intervalCpu.waitingTime = intervalCpu.turnAroundTime - burstTime;

            currentTime = intervalCpu.endTime;

            intervals.add(intervalCpu);
            process.remove(highestPriorityProcess);

            if(!process.isEmpty())
                currentTime += contextSwitchTime;
        }

        float avgWaiting = 0, avgTurnAround = 0;
        for(var it : intervals) {
            avgWaiting += it.waitingTime;
            avgTurnAround += it.turnAroundTime;
        }
        avgWaiting /= intervals.size();
        avgTurnAround /= intervals.size();

        intervals.averageWaitingTime = avgWaiting;
        intervals.averageTurnAroundTime = avgTurnAround;

        return intervals;
    }

}