# Coin Counter Parallelization

This project is part of the Parallel and Concurrent Programming course (2023/2024). The objective is to parallelize a recursive algorithm to determine the optimal way for a vending machine to give change, leveraging the computational power of multi-core processors.

## Goals

1. **Parallelize the sequential coin change algorithm** to improve performance using different parallelization strategies.
2. **Evaluate the performance** of each parallelization approach.
3. **Fine-tune the parallelization** for optimal performance on the given hardware.

## Key Features

1. **Fork/Join without ForkJoinPool**:
    - Utilizes a custom RecursiveTask for parallel computation.
    - Employs a divide-and-conquer strategy to solve the problem in parallel.

2. **ForkJoinPool**:
    - Manages task distribution automatically with a pool of threads.
    - Leverages the Fork/Join framework's work-stealing algorithm.

3. **ForkJoinPool with CompletableFuture**:
    - Integrates CompletableFuture for finer control over task execution.
    - Enables better tracking and coordination of tasks with asynchronous execution.

## Parallelization Strategies

### Fork/Join without ForkJoinPool

- **Strategy**: Uses RecursiveTask to manually handle task execution and result retrieval.
- **Reason**: Allows fine-tuned thread management for the specific task, leveraging work-stealing for workload balancing.
- **Implementation**: 
  - Creates and forks tasks in the main class.
  - Splits computation into two tasks: include and exclude the current coin.
  - Joins the results to determine the maximum change.

### ForkJoinPool

- **Strategy**: Uses a thread pool to manage task distribution.
- **Reason**: Automatically leverages multiple threads based on the machine's core count.
- **Implementation**:
  - Submits CoinTask to the thread pool.
  - Waits for task completion and returns the result.

### ForkJoinPool with CompletableFuture

- **Strategy**: Combines ForkJoinPool with CompletableFuture for asynchronous task management.
- **Reason**: Provides finer control over task creation and execution, enabling better scheduling and load balancing.
- **Implementation**:
  - Associates tasks with CompletableFuture.
  - Creates task pipelines for enhanced parallelism and throughput.

## Tools and Technologies

- **Java**: Programming language used for implementing the algorithm.
- **Fork/Join Framework**: Java's concurrency framework for parallel execution.
- **CompletableFuture**: Java's class for asynchronous programming.
- **Intel Core i7-12700H**: Processor used for performance testing (14 cores, 20 threads).

## Fine-tuning for Performance

- **Depth-based Condition**: Switches to sequential execution when recursion depth reaches 15, preventing excessive overhead from task creation and synchronization.
- **Surplus Task Count**: Adjusts workload distribution by executing tasks sequentially if queued tasks exceed a threshold, though this was less effective than the depth-based condition alone.

## Report

For a detailed explanation of the parallelization strategies, performance results, and fine-tuning techniques, please refer to the [project report](coin_report_55313.pdf).
