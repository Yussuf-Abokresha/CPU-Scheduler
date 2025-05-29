package gui;

import core.ProcessCpu;

public class ProcessTableViewer {
    private String color;
    private ProcessCpu process;

    public ProcessTableViewer(ProcessCpu process, String color) {
        this.color = color;
        this.process = process;
    }

    public int getPNum() {
        return process.PNum;
    }

    public String getColor() {
        return color;
    }

    public int getPriority() {
        return process.Priority;
    }
}
