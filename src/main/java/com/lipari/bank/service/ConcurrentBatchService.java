package com.lipari.bank.service;

import com.lipari.bank.pattern.TransferCommand;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentBatchService {

  public void executeBatchTransfers(List<TransferCommand> commands) {

    ExecutorService executor = Executors.newFixedThreadPool(4);
    AtomicInteger completed = new AtomicInteger();

    long start = System.nanoTime();

    for (TransferCommand command : commands) {
      executor.submit(() -> {
        command.execute();
        completed.incrementAndGet();
      });
    }

    executor.shutdown();

    try {
      executor.awaitTermination(1, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException(
          "Batch execution interrupted",
          e
      );
    }

    long end = System.nanoTime();

    long elapsedMillis = (end - start) / 1_000_000;

    System.out.println(
        "Completati "
            + completed.get()
            + "/"
            + commands.size()
            + " trasferimenti in "
            + elapsedMillis
            + " ms"
    );
  }
}