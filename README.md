# benchmark

## Installation

1. Clone the repository.
2. Run `npm i -D react@19.2.0 react-dom@19.2.0 react-refresh process
clojure -M -m shadow.cljs.devtools.cli watch app`.
3. Go to `http://localhost:8080`.

## UI

Main idea is to evaluate the release quality green or red using regresssion and coefficient of variation thresholds.

Cockpit set parameters for checking the release:

- Grouping - by bench simple or full.
- Regression (%), i.e. 5% means that aggregated mean did not degrage more than 5%. Prev average 100, new average 102 - fine. The regression can be negative.
- Coefficient of variation (%) - check that new release CV does not exceed the threshold.
- Ignore missing benchmark - check if some benchmark (simple of full) is missing in new data.

The pie chart gives the overall release result based on Cockpit parameters.
The unit is benchmark (simple or full).

The benchmark grid show aggregated ones which fell outside the parameters.
When click on the grid line, it drills down to the raw, not-aggregated benchmarks in the detailed grid.

## Data

The original Excel file was saved as two CSV files (`resources/new.csv`, `resources/old.csv`) and converted to Clojure vector of maps in `src/app/data.cljs`. The conversion was done by `src/csv/parser.clj`.
