package core.Scedulers;

import java.util.*;
import core.ProcessCpu;
import core.IntervalLists.IntervalList;
import core.IntervalLists.SrtfIntervalList;
import core.IntevalCpus.SrtfIntervalCpu;

public class SrtfScheduler extends CpuSceduler {
    private SrtfIntervalList intervals;
    private int contextSwitching;
    private static final double AGING_FACTOR = 0.1; // Reduces priority by 10% for each time unit waited

    public SrtfScheduler(LinkedList<ProcessCpu> process, int contextSwitching) {
        super(process);
        this.contextSwitching = contextSwitching;         
        this.intervals = new SrtfIntervalList();     
    }      

    private static class ProcessWithAge {
        ProcessCpu process;
        int waitingTime;
        
        ProcessWithAge(ProcessCpu process) {
            this.process = process;
            this.waitingTime = 0;
        }

        double getAdjustedBurstTime() {
            return process.BurstTime * Math.max(0.1, 1.0 - (waitingTime * AGING_FACTOR));
        }

        void incrementWaitingTime() {
            waitingTime++;
        }
    }

    @Override     
    public IntervalList Simulate() {         
        LinkedList<ProcessCpu> originalProcesses = new LinkedList<>();
        for (ProcessCpu p : process) {
            originalProcesses.add(new ProcessCpu(p));
        }

        process.sort(Comparator.comparingInt(p -> p.ArrivalTime));          

        PriorityQueue<ProcessWithAge> readyQueue = new PriorityQueue<>(             
            Comparator.comparingDouble(ProcessWithAge::getAdjustedBurstTime)        
        );          

        int currentTime = 0;
        int contextSwitchTime = 0;          

        LinkedList<ProcessWithAge> waitingProcesses = new LinkedList<>();
        for (ProcessCpu p : process) {
            waitingProcesses.add(new ProcessWithAge(p));
        }

        while (!waitingProcesses.isEmpty() || !readyQueue.isEmpty()) {             
            // Add newly arrived processes to ready queue
            while (!waitingProcesses.isEmpty() && waitingProcesses.peek().process.ArrivalTime <= currentTime) {                 
                readyQueue.add(waitingProcesses.poll());             
            }              

            if (readyQueue.isEmpty()) {                 
                currentTime++;                 
                continue;             
            }              

            // Update waiting time for all processes in ready queue except the current one
            for (ProcessWithAge p : readyQueue) {
                p.incrementWaitingTime();
            }

            ProcessWithAge currentProcess = readyQueue.poll();           
            SrtfIntervalCpu interval = new SrtfIntervalCpu();              

            interval.startTime = currentTime + contextSwitchTime;             
            interval.Pnum = currentProcess.process.PNum;              

            boolean preempted = false;              

            while (currentProcess.process.BurstTime > 0) {                 
                currentTime++;                 
                currentProcess.process.BurstTime--;                  

                while (!waitingProcesses.isEmpty() && waitingProcesses.peek().process.ArrivalTime <= currentTime) {                     
                    ProcessWithAge newProcess = waitingProcesses.poll();                     
                    if (newProcess.getAdjustedBurstTime() < currentProcess.getAdjustedBurstTime()) {                         
                        readyQueue.add(newProcess);                         
                        preempted = true;                         
                        break;                     
                    } else {                         
                        readyQueue.add(newProcess);                     
                    }                 
                }                  

                if (preempted) break;             
            }              

            // Rest of the implementation remains the same...
            interval.endTime = currentTime;             
            interval.RemainingBurstTime = currentProcess.process.BurstTime;             
            interval.ActionDetail = preempted                     
                ? "Preempted by P" + (readyQueue.peek() != null ? readyQueue.peek().process.PNum : "N/A")                     
                : "Process completed";              

            ProcessCpu originalProcess = originalProcesses.stream()
                .filter(p -> p.PNum == currentProcess.process.PNum)
                .findFirst()
                .orElseThrow();

            interval.turnAroundTime = interval.endTime - originalProcess.ArrivalTime;
            interval.waitingTime = interval.turnAroundTime - originalProcess.BurstTime;

            intervals.add(interval);              

            if (preempted && currentProcess.process.BurstTime > 0) {                 
                readyQueue.add(currentProcess);             
            }              

            contextSwitchTime = preempted ? contextSwitching : 0;         
        }          

        // Calculate averages
        float avgWaitingTime = 0, avgTurnAroundTime = 0;         
        for (var it : intervals) {             
            avgWaitingTime += it.waitingTime;             
            avgTurnAroundTime += it.turnAroundTime;         
        }         
        intervals.averageWaitingTime = avgWaitingTime / intervals.size();         
        intervals.averageTurnAroundTime = avgTurnAroundTime / intervals.size();          

        return intervals;     
    } 
}