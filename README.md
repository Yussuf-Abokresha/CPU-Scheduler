# CPU Scheduling Algorithms Simulator

## Overview  
This Java project simulates various CPU scheduling algorithms, providing detailed output of the scheduling process and performance metrics. It includes implementations of multiple scheduling approaches, each with specialized interval tracking and reporting.

## Features  
- Multiple scheduling algorithm implementations  
- Detailed execution timeline for each algorithm  
- Performance metrics calculation (AWT, ATAT)  
- Custom FCAI scheduling variant  
- Clean, object-oriented design with inheritance  

## Implemented Algorithms  
| Algorithm        | Interval List Class      | Interval CPU Class        |
|------------------|--------------------------|---------------------------|
| FCFS             | `FcaiIntervalList`       | `FcaiIntervalCpu`         |
| Priority         | `PriorityIntervalList`   | `PriorityIntervalCpu`     |
| SJF              | `SjfIntervalList`        | `SjfIntervalCpu`          |
| SRTF             | `SrtfIntervalList`       | (Uses base `IntervalCpu`) |

## Class Structure  
```text
src/
└── core/
    ├── ProcessCpu.java                 # Process representation
    ├── IntervalLists/                  # Scheduling algorithm containers
    │   ├── IntervalList.java           # Abstract base class
    │   ├── FcaiIntervalList.java       # FCFS implementation
    │   ├── PriorityIntervalList.java   # Priority scheduling
    │   ├── SjfIntervalList.java        # Shortest Job First
    │   └── SrtfIntervalList.java       # Shortest Remaining Time First
    └── IntevalCpus/                    # Interval record implementations
        ├── IntervalCpu.java            # Abstract base
        ├── FcaiIntervalCpu.java        # FCFS records
        ├── PriorityIntervalCpu.java    # Priority records
        └── SjfIntervalCpu.java         # SJF records
