# Agent-based simulation of a furniture workshop

Semester project for Discrete Simulation (FRI UNIZA, 2025), the third of three:
[Monte Carlo](https://github.com/tkozelek/DIS1_Monte_carlo),
[event-driven](https://github.com/tkozelek/DIS2_Event_driven_simulation) and this agent-based one.

The same workshop as in the event-driven project, built as agents. Orders of tables, chairs and
cupboards arrive, workers from three groups cut, assemble, stain or paint and fit the pieces at
workstations, and move between the storage and the workstations. The simulation measures how long
an order takes with different numbers of workers and workstations.

## How it works

- Built on the OSPABA agent framework from the course: a boss agent, one agent per worker group,
  and agents for movement, workers, workplaces and the environment, talking through messages.
- Random inputs from my own generators (uniform, triangular, exponential, empirical) with separate
  seeds.
- Many replications with statistics per worker group and workstation, charts with JFreeChart, and
  an animation of a single run.
- `SimulationRunner` runs combinations of worker and workstation counts in parallel threads,
  150 replications each, and writes the results to CSV.

## Run it

Open the project in IntelliJ IDEA and run `Main`. It needs the OSPABA library from the course, which
is not included in this repository. Java 24, Maven.
