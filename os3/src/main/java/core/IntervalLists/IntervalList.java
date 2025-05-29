package core.IntervalLists;

import java.util.LinkedList;

import core.IntevalCpus.IntervalCpu;

public abstract class IntervalList extends LinkedList<IntervalCpu> {
    protected abstract void printHeader();

    public double averageWaitingTime = 0;
    public double averageTurnAroundTime = 0;

    public void print() {
        printHeader();
        for (IntervalCpu interval : this) {
            interval.Print();
        }
        System.out.println();
        for(IntervalCpu interval : this) {
            if(interval.turnAroundTime != 0 && interval.waitingTime >= 0){
                System.out.println("P" + interval.Pnum + " Waiting Time" + " -> " + interval.waitingTime);
                System.out.println("P" + interval.Pnum + " TurnAround Time" + " -> " + interval.turnAroundTime);
                System.out.println();
            }
        }
        System.out.println(String.format("AWT : %.2f , ATAT : %.2f", averageWaitingTime, averageTurnAroundTime));
    }
}
