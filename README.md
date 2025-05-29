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
    │   ├── FCAI_IntervalList.java      # FCAI implementation
    │   ├── PriorityIntervalList.java   # Priority scheduling
    │   ├── SjfIntervalList.java        # Shortest Job First
    │   └── SrtfIntervalList.java       # Shortest Remaining Time First
    └── IntevalCpus/                    # Interval record implementations
        ├── IntervalCpu.java            # Abstract base
        ├── FCAI_IntervalCpu.java       # FCAI records
        ├── PriorityIntervalCpu.java    # Priority records
        └── SjfIntervalCpu.java         # SJF records



# FCAI Scheduling Algorithm

Traditional CPU scheduling algorithms, like Round Robin (RR) or Priority Scheduling, often suffer from starvation or inefficiency when handling a mix of short- and long-burst processes with varying priorities. 

**FCAI Scheduling** is an adaptive scheduling algorithm that addresses these limitations by combining **Priority**, **Arrival Time**, and **Remaining Burst Time** into a single metric—the **FCAI Factor**. This factor dynamically manages the **execution order** and **quantum allocation** of processes.

---

## Key Features

### Dynamic FCAI Factor

A calculated metric for each process using:

- **Priority (P)**
- **Arrival Time (AT)**
- **Remaining Burst Time (RBT)**

**Formula:**

```

FCAI Factor = (10 − Priority) + (Arrival Time / V1) + (Remaining Burst Time / V2)

```

Where:

- `V1 = (Last arrival time of all processes) / 10`
- `V2 = (Max burst time of all processes) / 10`

---

### Quantum Allocation Rules

- Each process is assigned an initial quantum.
- When processes are preempted or re-queued, their quantum is updated dynamically:

  - `Q = Q + 2` → if the process completes its quantum but still has remaining work.
  - `Q = Q + unused quantum` → if the process is preempted before completing its quantum.

---

## Execution Strategy

- Each process runs **non-preemptively for the first 40%** of its quantum.
- After 40% execution, **preemption is allowed**, enabling adaptive rescheduling based on updated FCAI Factors.

---

## Example Use Cases

- Handling mixed workloads with diverse priority levels and burst times.
- Improving fairness and reducing starvation in multi-process systems.
- Adaptive and responsive scheduling for time-sensitive applications.

---

## Notes

- This algorithm is part of **CS341 – Operating Systems 1, Assignment #3**.
- Suitable for simulations and academic demonstration of adaptive CPU scheduling.

---


