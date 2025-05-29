package core.Scedulers;

import java.util.LinkedList;

import core.ProcessCpu;
import core.IntervalLists.IntervalList;

public abstract class CpuSceduler {
    public LinkedList<ProcessCpu> process;

    public CpuSceduler(LinkedList<ProcessCpu> process) {
        this.process = process;
    }

    public abstract IntervalList Simulate();
}
