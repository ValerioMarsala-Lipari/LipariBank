package com.lipari.bank.risk;

import com.lipari.bank.exception.RiskCalculationException;
import com.lipari.bank.model.Account;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.Transaction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Calculates risk scores for multiple customers in parallel.
 *
 * @author Valerio
 * @since 1.0
 */
public class ParallelRiskCalculationService {

  private final RiskCalculationService riskCalculationService;

  /**
   * Creates a parallel risk calculation service.
   *
   * @param riskCalculationService service used for individual calculations
   */
  public ParallelRiskCalculationService(RiskCalculationService riskCalculationService) {
    if (riskCalculationService == null) {
      throw new IllegalArgumentException("Risk calculation service cannot be null");
    }

    this.riskCalculationService = riskCalculationService;
  }

  /**
   * Calculates risk scores for multiple customers concurrently.
   *
   * @param customers              customers to evaluate
   * @param accountsByCustomer     accounts grouped by customer ID
   * @param transactionsByCustomer transactions grouped by customer ID
   * @return calculated risk scores
   */
  public List<RiskScore> calculateRisks(List<Customer> customers, Map<Long, List<Account>> accountsByCustomer, Map<Long, List<Transaction>> transactionsByCustomer) {
    if (customers == null) {
      throw new IllegalArgumentException("Customers cannot be null");
    }

    if (accountsByCustomer == null) {
      throw new IllegalArgumentException("Accounts by customer cannot be null");
    }

    if (transactionsByCustomer == null) {
      throw new IllegalArgumentException("Transactions by customer cannot be null");
    }

    try (ExecutorService executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {

      List<Future<RiskScore>> futures = customers.stream().map(customer -> executor.submit(() -> riskCalculationService.calculateRisk(customer, accountsByCustomer.getOrDefault(customer.getId(), List.of()), transactionsByCustomer.getOrDefault(customer.getId(), List.of())))).toList();

      return futures.stream().map(this::getResult).toList();
    }
  }

  private RiskScore getResult(Future<RiskScore> future) {
    try {
      return future.get();
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();

      throw new RiskCalculationException("Risk calculation was interrupted", exception);
    } catch (java.util.concurrent.ExecutionException exception) {
      Throwable cause = exception.getCause();

      if (cause instanceof RiskCalculationException riskException) {
        throw riskException;
      }

      throw new RiskCalculationException("Risk calculation failed", cause);
    }
  }
}