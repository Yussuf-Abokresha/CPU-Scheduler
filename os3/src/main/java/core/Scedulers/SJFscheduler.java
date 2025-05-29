package core.Scedulers;

import core.IntervalLists.IntervalList;
import core.IntervalLists.SjfIntervalList;
import core.IntevalCpus.SjfIntervalCpu;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import core.ProcessCpu;

public class SJFscheduler extends CpuSceduler {
    private IntervalList iList;
    private LinkedList<ProcessCpu> sjfprocess;

    public SJFscheduler(LinkedList<ProcessCpu> cpu) {
        super(cpu);
        this.iList = new SjfIntervalList();
        this.sjfprocess = new LinkedList<>(process);
    }

    private static final int AGING_THRESHOLD = 10;
    private static final float AGING_FACTOR = 0.2f;

    @Override
    public IntervalList Simulate() {
        
        LinkedList<ProcessCpu> Q = new LinkedList<>();
        Map<ProcessCpu, Integer> agingFactors = new HashMap<>();
        int time = 0;
        sjfprocess.sort(Comparator.comparingInt((ProcessCpu p) -> p.ArrivalTime).thenComparingInt(p -> p.BurstTime));
        while (!Q.isEmpty() || !sjfprocess.isEmpty()) {
            Iterator<ProcessCpu> iterator = sjfprocess.iterator();
            while (iterator.hasNext()) {
                ProcessCpu pcpu = iterator.next();
                if (pcpu.ArrivalTime <= time) {
                    Q.add(pcpu);
                    agingFactors.put(pcpu, 0);
                    iterator.remove();
                }
            }
            if (Q.isEmpty()) {
                time = sjfprocess.peek().ArrivalTime;
                continue;
            }

            ProcessCpu selectedProcess = null;
            int minAdjustedBurstTime = Integer.MAX_VALUE;

            for (ProcessCpu process : Q) {
                int waitingTime = time - process.ArrivalTime;
                int adjustedBurstTime = process.BurstTime;

                if (waitingTime >= AGING_THRESHOLD) {
                    float agingAdjustment = (waitingTime - AGING_THRESHOLD) * AGING_FACTOR;
                    adjustedBurstTime = Math.max(1, (int) (process.BurstTime - agingAdjustment));
                }

                if (adjustedBurstTime < minAdjustedBurstTime) {
                    minAdjustedBurstTime = adjustedBurstTime;
                    selectedProcess = process;
                }
            }

            ProcessCpu shortestBurst = selectedProcess;
            Q.remove(shortestBurst);
            SjfIntervalCpu iCpu = new SjfIntervalCpu();
            iCpu.startTime = time;
            iCpu.waitingTime = time - shortestBurst.ArrivalTime ;
            if(iCpu.waitingTime < 0 ) {
                iCpu.waitingTime = 0;
            }
            iCpu.endTime = time + shortestBurst.BurstTime;
            iCpu.turnAroundTime = iCpu.endTime - shortestBurst.ArrivalTime;
            if(iCpu.turnAroundTime <= 0) {
                iCpu.turnAroundTime = shortestBurst.BurstTime;
            }
            iCpu.Pnum = shortestBurst.PNum;
            
            iList.add(iCpu);
            time += shortestBurst.BurstTime;
            agingFactors.remove(shortestBurst);
        }
        float avgWaiting = 0, avgTurnAround = 0;
        for(var it : iList) {
            avgWaiting += it.waitingTime;
            avgTurnAround += it.turnAroundTime;
        }
        avgWaiting /= iList.size();
        avgTurnAround /= iList.size();

        iList.averageWaitingTime = avgWaiting;
        iList.averageTurnAroundTime = avgTurnAround;
        return iList;
    }

}
