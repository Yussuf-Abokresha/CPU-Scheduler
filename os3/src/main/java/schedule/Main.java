package schedule;

import java.util.LinkedList;
import java.util.Scanner;

import core.ProcessCpu;
import core.IntervalLists.IntervalList;
import core.Scedulers.FCAIScheduler;
import core.Scedulers.PrioritySceduler;
import core.Scedulers.SJFscheduler;
import core.Scedulers.SrtfScheduler;
import gui.DisplayService;

public class Main {
    private static LinkedList<ProcessCpu> copyProcess(LinkedList<ProcessCpu> process) {
        LinkedList<ProcessCpu> processCpus = new LinkedList<ProcessCpu>();
        for (ProcessCpu proc : process) {
            processCpus.add(new ProcessCpu(proc));
        }
        return processCpus;
    }

    public static void main(String[] args) {
        LinkedList<ProcessCpu> processCpus = new LinkedList<ProcessCpu>();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int x = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter: Burst Time Arrival Time Priority Quantum");
        for (int i = 1; i <= x; i++) {
            String[] inpuStrings = scanner.nextLine().split(" ");
            ProcessCpu processCpu = new ProcessCpu();
            processCpu.PNum = i;
            processCpu.BurstTime = Integer.parseInt(inpuStrings[0]);
            processCpu.ArrivalTime = Integer.parseInt(inpuStrings[1]);
            processCpu.Priority = Integer.parseInt(inpuStrings[2]);
            processCpu.Quantum = Integer.parseInt(inpuStrings[3]);
            processCpus.add(processCpu);
        }

        while (true) {
            System.out.println("1) SJF SCHEDULER");
            System.out.println("2) SRTF SCHEDULER");
            System.out.println("3) PRIORITY SCHEDULER");
            System.out.println("4) FCAI SCHEDULER");
            System.err.println("0) Exit");
            System.out.print("CHOOSE A SCHEDULER: ");
            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception ex) {
                choice = -1;
            }
            boolean exitFlag = false, InvalidInput = false;
            IntervalList intervals = null;
            switch (choice) {
                case 0:
                    exitFlag = true;
                    break;
                case 1:
                    intervals = new SJFscheduler(copyProcess(processCpus)).Simulate();
                    break;
                case 2:
                    System.out.print("Enter context switching time: ");
                    int contextSwitching = Integer.parseInt(scanner.nextLine());
                    intervals = new SrtfScheduler(copyProcess(processCpus), contextSwitching).Simulate();
                    break;
                case 3:
                    intervals = new PrioritySceduler(copyProcess(processCpus)).Simulate();
                    break;
                case 4:
                    intervals = new FCAIScheduler(copyProcess(processCpus)).Simulate();
                    break;
                default:
                    InvalidInput = true;
                    System.out.println("Invalid input!");
                    break;
            }
            if (InvalidInput)
                continue;
            if (exitFlag)
                break;
            intervals.print();
            System.out.println("Display Gui ? Y/N : ");
            if (scanner.nextLine().toLowerCase().equals("y")) {
                DisplayService displayService = new DisplayService();
                displayService.CreateDisplay(intervals, processCpus);
            }

        }
        System.out.println("Have a great day :D");
    }
}