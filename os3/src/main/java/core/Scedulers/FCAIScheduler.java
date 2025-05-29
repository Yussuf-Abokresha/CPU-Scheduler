package core.Scedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import core.ProcessCpu;
import core.IntervalLists.IntervalList;
import core.IntevalCpus.FcaiIntervalCpu;
import core.IntevalCpus.IntervalCpu;
import core.IntervalLists.FcaiIntervalList;

public class FCAIScheduler extends CpuSceduler {
    private IntervalList iList;
    private LinkedList<ProcessCpu> fcaiprocess;

    public FCAIScheduler(LinkedList<ProcessCpu> cpu) {
        super(cpu);
        this.iList = new FcaiIntervalList();
        this.fcaiprocess = new LinkedList<>(process);
    }

    @Override
    public IntervalList Simulate() {
        Map <ProcessCpu, Integer> fcaifactors = new HashMap<>();
        LinkedList <ProcessCpu> ready = new LinkedList<>();
        int time = 0;
        double v1 = 0, v2 = 0;
        v1 = fcaiprocess.getLast().ArrivalTime;
        v2 = fcaiprocess.stream().max(Comparator.comparingInt(proc -> proc.BurstTime)).orElseThrow().BurstTime;
        v1 /= 10.0;
        v2 /= 10.0;
        Map <Integer, int[]> tat = new HashMap<>();
        int size = fcaiprocess.size();
        for(ProcessCpu p : fcaiprocess) {
            tat.put(p.PNum, new int[]{p.ArrivalTime, 0});
        }
        fcaiprocess.sort(Comparator.comparingInt(proc -> proc.ArrivalTime));
        for(ProcessCpu proc : fcaiprocess) {
            fcaifactors.put(proc, (int)Math.ceil(10 - proc.Priority + Math.ceil(proc.ArrivalTime/v1) + Math.ceil(proc.BurstTime/v2)));
        }
        int totalproc = fcaiprocess.size();
        int complete = 0;
        ProcessCpu currentProcess = null;
        while(totalproc > complete) {
            FcaiIntervalCpu icpu = new FcaiIntervalCpu();
            Iterator<ProcessCpu> iterator = fcaiprocess.iterator();
            while (iterator.hasNext()) {
                ProcessCpu proc = iterator.next();
                if (proc.ArrivalTime <= time && !ready.contains(proc) && proc.BurstTime > 0 && currentProcess != proc) {
                    ready.add(proc);
                }
            }
            if(ready.isEmpty()) {
                time++;
                continue;
            }
            
            if (currentProcess == null) {
                currentProcess = ready.peek();
            }

            int executionTime = Math.min((int) Math.ceil(currentProcess.Quantum * 0.4), currentProcess.BurstTime);
            if(icpu.actualBurst == 0) {
                icpu.actualBurst = currentProcess.BurstTime;
            }
            icpu.startTime = time;
            icpu.Pnum = currentProcess.PNum;
            time += executionTime;
            currentProcess.BurstTime -= executionTime;
            icpu.RemainingBurstTime = currentProcess.BurstTime;
            if(currentProcess.BurstTime == 0) {
                icpu.endTime = time;
                icpu.RemainingBurstTime = 0;
                icpu.FcaiFactor = fcaifactors.get(currentProcess);
                icpu.Priority = currentProcess.Priority;
                icpu.prevQ = currentProcess.Quantum;
                icpu.UpdatedQuantam = currentProcess.Quantum;
                icpu.turnAroundTime = time - currentProcess.ArrivalTime;
                iList.add(icpu);
                ready.remove(currentProcess);
                fcaiprocess.remove(currentProcess);
                currentProcess = null;
                complete++;
                continue;
            }
            iterator = fcaiprocess.iterator();
            while (iterator.hasNext()) {
                ProcessCpu proc = iterator.next();
                if (proc.ArrivalTime <= time && !ready.contains(proc) && proc.BurstTime > 0 && currentProcess != proc) {
                    ready.add(proc);
                }
            }
            
            ProcessCpu lowestPriority = ready.stream()
            .min(Comparator.comparingInt(proc -> fcaifactors.get(proc)))
            .orElse(currentProcess);
            boolean preempted = false;
            if(fcaifactors.get(lowestPriority) < fcaifactors.get(currentProcess)){
                    icpu.RemainingBurstTime = currentProcess.BurstTime;
                    fcaifactors.replace(currentProcess,(int) Math.ceil(10 - currentProcess.Priority + (currentProcess.ArrivalTime/v1) + (currentProcess.BurstTime/v2)));
                    icpu.endTime = time;
                    icpu.FcaiFactor = fcaifactors.get(currentProcess);
                    icpu.Priority = currentProcess.Priority;
                    icpu.prevQ = currentProcess.Quantum;
                    currentProcess.Quantum += (currentProcess.Quantum - executionTime);
                    icpu.UpdatedQuantam = currentProcess.Quantum;
                    iList.add(icpu);
                    ready.remove(currentProcess);
                    ready.add(currentProcess);
                    currentProcess = lowestPriority;
                    ready.remove(lowestPriority);
                    continue;
            } else {
                int j = currentProcess.Quantum - executionTime;
                while(j > 0) {
                    executionTime++;
                    time++;
                    currentProcess.BurstTime--;
                    if(currentProcess.BurstTime == 0) {
                        icpu.endTime = time;
                        icpu.RemainingBurstTime = 0;
                        icpu.FcaiFactor = fcaifactors.get(currentProcess);
                        icpu.Priority = currentProcess.Priority;
                        icpu.prevQ = currentProcess.Quantum;
                        icpu.UpdatedQuantam = currentProcess.Quantum;
                        icpu.turnAroundTime = time - currentProcess.ArrivalTime;
                        iList.add(icpu);
                        ready.remove(currentProcess);
                        fcaiprocess.remove(currentProcess);
                        currentProcess = null;
                        complete++;
                        break;
                    }
                    iterator = fcaiprocess.iterator();
                    while (iterator.hasNext()) {
                        ProcessCpu proc = iterator.next();
                        if (proc.ArrivalTime <= time && !ready.contains(proc) && proc.BurstTime > 0 && currentProcess != proc) {
                            ready.add(proc);
                        }
                    }
                    lowestPriority = ready.stream()
                    .min(Comparator.comparingInt(proc -> fcaifactors.get(proc)))
                    .orElse(currentProcess);
                    if(fcaifactors.get(lowestPriority) < fcaifactors.get(currentProcess)){
                        icpu.RemainingBurstTime = currentProcess.BurstTime;
                        fcaifactors.replace(currentProcess,(int) Math.ceil(10 - currentProcess.Priority + (currentProcess.ArrivalTime/v1) + (currentProcess.BurstTime/v2)));
                        icpu.endTime = time;
                        icpu.FcaiFactor = fcaifactors.get(currentProcess);
                        icpu.Priority = currentProcess.Priority;
                        icpu.prevQ = currentProcess.Quantum;
                        currentProcess.Quantum += (currentProcess.Quantum - executionTime);
                        icpu.UpdatedQuantam = currentProcess.Quantum;
                        iList.add(icpu);
                        ready.remove(currentProcess);
                        ready.add(currentProcess);
                        currentProcess = lowestPriority;
                        ready.remove(lowestPriority);
                        preempted = true;
                        break;
                    }   
                    j--;
                }
                if(!preempted && currentProcess != null){
                    icpu.prevQ = currentProcess.Quantum;
                    currentProcess.Quantum += 2;
                    icpu.endTime = time;
                    icpu.RemainingBurstTime = currentProcess.BurstTime;
                    fcaifactors.replace(currentProcess,(int) Math.ceil(10 - currentProcess.Priority + (currentProcess.ArrivalTime/v1) + (currentProcess.BurstTime/v2)));
                    icpu.FcaiFactor = fcaifactors.get(currentProcess);
                    icpu.Priority = currentProcess.Priority;
                    icpu.UpdatedQuantam = currentProcess.Quantum;

                    iList.add(icpu);
                    ready.remove(currentProcess);
                    ready.add(currentProcess);
                    currentProcess = null;
                }
            }
        }
        Iterator <IntervalCpu> l = iList.iterator();
        ListIterator <IntervalCpu> r = iList.listIterator(iList.size());
        while (l.hasNext()) {
            IntervalCpu left = l.next();
            IntervalCpu right = r.previous();
            while(r.hasPrevious()) {
                if(left.Pnum == right.Pnum && left != right && (tat.get(left.Pnum)[1] == 0 && tat.get(right.Pnum)[1] == 0)) {
                    right.turnAroundTime = right.endTime - tat.get(left.Pnum)[0];
                    tat.get(left.Pnum)[1] = 1;
                    tat.get(right.Pnum)[1] = 1;
                    right.waitingTime = right.turnAroundTime - left.actualBurst;
                }
                if (left == right || r.previousIndex() < iList.indexOf(left)) {
                    break;
                }
                right = r.previous();
            }
            r = iList.listIterator(iList.size());
        }
        float avgWaiting = 0, avgTurnAround = 0;
        for(var it : iList) {
            avgWaiting += it.waitingTime;
            avgTurnAround += it.turnAroundTime;
        }
        avgWaiting /= size;
        avgTurnAround /= size;
        iList.averageTurnAroundTime = avgTurnAround;
        iList.averageWaitingTime = avgWaiting;
       
        return iList;
    }
}
