package com.lipari.bank.service;

import com.lipari.bank.exception.BankException;
import com.lipari.bank.pattern.TransferCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Executes multiple bank transfers concurrently.
 *
 * @author Valerio
 * @since 1.0
 */
public class ConcurrentBatchService {

  /**
   * Executes all transfer commands using a fixed-size thread pool.
   *
   * @param commands transfer commands to execute
   * @throws BankException if a transfer fails or batch execution is interrupted
   */
  public void executeBatchTransfers(List<TransferCommand> commands) {
    ExecutorService executor = Executors.newFixedThreadPool(4);
    AtomicInteger completed = new AtomicInteger();
    List<Future<?>> futures = new ArrayList<>();
    long start = System.nanoTime();

    try {
      for (TransferCommand command : commands) {
        futures.add(executor.submit(() -> {
          command.execute();
          completed.incrementAndGet();
        }));
      }

      executor.shutdown();

      if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
        executor.shutdownNow();
        throw new BankException("Batch execution timed out", "BATCH_EXECUTION_TIMEOUT");
      }

      for (Future<?> future : futures) {
        future.get();
      }
    } catch (InterruptedException exception) {
      executor.shutdownNow();
      Thread.currentThread().interrupt();

      throw new BankException("Batch execution interrupted", "BATCH_EXECUTION_INTERRUPTED");
    } catch (ExecutionException exception) {
      executor.shutdownNow();

      BankException batchException = new BankException("Batch transfer execution failed", "BATCH_EXECUTION_FAILED");
      batchException.initCause(exception.getCause());
      throw batchException;
    } finally {
      if (!executor.isTerminated()) {
        executor.shutdownNow();
      }
    }

    long end = System.nanoTime();
    long elapsedMillis = (end - start) / 1_000_000;

    System.out.println("Completati " + completed.get() + "/" + commands.size() + " trasferimenti in " + elapsedMillis + " ms");
  }
}
