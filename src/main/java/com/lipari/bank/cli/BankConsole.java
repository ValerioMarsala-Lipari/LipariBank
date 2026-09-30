package com.lipari.bank.cli;

import com.lipari.bank.compliance.ComplianceEngine;
import com.lipari.bank.compliance.alert.Alert;
import com.lipari.bank.compliance.rules.*;
import com.lipari.bank.exception.AccountNotFoundException;
import com.lipari.bank.exception.InsufficientFundsException;
import com.lipari.bank.model.*;
import com.lipari.bank.pattern.BankConfiguration;
import com.lipari.bank.reporting.ComplianceReport;
import com.lipari.bank.reporting.ComplianceReportService;
import com.lipari.bank.repository.AccountRepository;
import com.lipari.bank.risk.*;
import com.lipari.bank.service.TransferService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Provides the interactive console interface for LipariBank operations.
 *
 * @author Valerio
 * @since 1.0
 */
public class BankConsole {

  private final Scanner scanner = new Scanner(System.in);
  private final AccountRepository accountRepository = new AccountRepository();
  private final TransferService transferService;

  private final RiskCalculationService riskCalculationService;
  private final ParallelRiskCalculationService parallelRiskCalculationService;
  private final ComplianceEngine complianceEngine;
  private final ComplianceReportService complianceReportService;

  /**
   * Creates and configures the bank console and its application services.
   */
  public BankConsole() {
    this.transferService = new TransferService(accountRepository);
    this.riskCalculationService = new RiskCalculationService(new PrivateCustomerScoringStrategy(), new BusinessCustomerScoringStrategy());
    this.parallelRiskCalculationService = new ParallelRiskCalculationService(riskCalculationService);
    List<ComplianceRule> complianceRules = List.of(new HighFrequencyTransferRule(), new LargeTransactionRule(), new DailyVolumeRule(), new NewCustomerHighValueRule());

    this.complianceEngine = new ComplianceEngine(complianceRules);
    this.complianceReportService = new ComplianceReportService();

  }

  /**
   * Starts the LipariBank console application.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    BankConsole console = new BankConsole();
    console.initializeData();
    console.run();
  }

  private void run() {
    System.out.println("Benvenuto in LipariBank!");

    boolean running = true;

    while (running) {
      printMenu();
      int choice = readIntSafe();

      switch (choice) {
        case 1 -> listAccounts();
        case 2 -> showBalance();
        case 3 -> makeDeposit();
        case 4 -> makeWithdrawal();
        case 5 -> makeTransfer();
        case 6 -> showTransactions();
        case 7 -> showReporting();
        case 8 -> showConfiguration();
        case 9 -> applyInterest();
        case 10 -> processAndClassifyAccounts();
        case 11 -> showRiskAndCompliance();
        case 0 -> {
          System.out.println("\nArrivederci da LipariBank!");
          running = false;
        }
        default -> System.out.println("⚠ Input non valido!");
      }
    }

  }

  private void initializeData() {

    BankConfiguration configuration = BankConfiguration.getInstance();

    configuration.setDailyTransferLimit(new BigDecimal("10000"));
    configuration.setMaxOverdraftLimit(new BigDecimal("500"));
    configuration.setMinAgeForLifeInsurance(18);
    configuration.setBankName("LipariBank");

    Customer mario = new Customer("RSSMRA80A01H501X", "Mario", "Rossi", CustomerType.PRIVATE, LocalDate.now().minusYears(2));
    mario.setId(1L);

    Customer laura = new Customer("BNCLRA85B02H501Y", "Laura", "Bianchi", CustomerType.PRIVATE, LocalDate.now().minusMonths(3));
    laura.setId(2L);

    Account marioAccount = new CheckingAccount("ACC-001", LocalDate.now(), "IT60X0542811101000000123456", BigDecimal.valueOf(1000), mario, BigDecimal.valueOf(200));

    Account lauraAccount = new CheckingAccount("ACC-002", LocalDate.now(), "IT60X0542811101000000654321", BigDecimal.valueOf(500), laura, BigDecimal.valueOf(200));

    accountRepository.save(marioAccount);
    accountRepository.save(lauraAccount);

  }

  private void printMenu() {
    System.out.println("""
        
        ╔══════════════════════════════════════╗
        ║         LIPARIBANK  CONSOLE          ║
        ╠══════════════════════════════════════╣
        ║  1. Lista conti                      ║
        ║  2. Visualizza saldo                 ║
        ║  3. Deposita                         ║
        ║  4. Preleva                          ║
        ║  5. Bonifico                         ║
        ║  6. Storico transazioni              ║
        ║  7. Reportistica                     ║
        ║  8. Configurazione                   ║
        ║  9. Applica interessi (risparmio)    ║
        ║  10. Processa e classifica conti     ║
        ║  11. Risk & Compliance               ║
        ║  0. Esci                             ║
        ╚══════════════════════════════════════╝""");
    System.out.print("  Scelta: ");

  }

  /**
   * Reads an integer from the console.
   *
   * @return entered integer, or -1 when the input is invalid
   */
  private int readIntSafe() {
    String input = scanner.nextLine().trim();
    try {
      return Integer.parseInt(input);
    } catch (NumberFormatException e) {
      return -1;
    }

  }

  private BigDecimal readBigDecimalSafe() {
    String input = scanner.nextLine().trim();

    try {
      return new BigDecimal(input);
    } catch (NumberFormatException e) {
      return BigDecimal.valueOf(-1);
    }

  }

  private void listAccounts() {
    System.out.println("\n─── CONTI ───────────────────────────────────────────────");

    for (Account account : accountRepository.findAll()) {
      System.out.println("IBAN: " + account.getIban() + " | Titolare: " + account.getOwner().getFirstName() + " " + account.getOwner().getLastName() + " | Saldo: " + account.getBalance());
    }

  }

  private void showBalance() {
    System.out.print("\nInserisci IBAN: ");
    String iban = scanner.nextLine().trim();

    try {
      Account account = accountRepository.findByIban(iban).orElseThrow(() -> new AccountNotFoundException(iban));

      System.out.println("Saldo: " + account.getBalance());
    } catch (AccountNotFoundException e) {
      System.out.println("⚠ " + e.getMessage());
    }

  }

  private void makeDeposit() {
    System.out.print("\nInserisci IBAN: ");
    String iban = scanner.nextLine().trim();

    System.out.print("Inserisci importo: ");
    BigDecimal amount = readBigDecimalSafe();

    try {
      Account account = accountRepository.findByIban(iban).orElseThrow(() -> new AccountNotFoundException(iban));

      account.deposit(amount);

      System.out.println("Deposito effettuato.");
      System.out.println("Nuovo saldo: " + account.getBalance());
    } catch (AccountNotFoundException | IllegalArgumentException e) {
      System.out.println("⚠ " + e.getMessage());
    }

  }

  private void makeWithdrawal() {
    System.out.print("\nInserisci IBAN: ");
    String iban = scanner.nextLine().trim();

    System.out.print("Inserisci importo: ");
    BigDecimal amount = readBigDecimalSafe();

    try {
      Account account = accountRepository.findByIban(iban).orElseThrow(() -> new AccountNotFoundException(iban));

      account.withdraw(amount);

      System.out.println("Prelievo effettuato.");
      System.out.println("Nuovo saldo: " + account.getBalance());
    } catch (AccountNotFoundException | InsufficientFundsException | IllegalArgumentException e) {
      System.out.println("⚠ " + e.getMessage());
    }

  }

  private void makeTransfer() {
    System.out.print("\nIBAN del conto sorgente: ");
    String sourceIban = scanner.nextLine().trim();

    System.out.print("IBAN del conto destinatario: ");
    String targetIban = scanner.nextLine().trim();

    System.out.print("Importo: ");
    BigDecimal amount = readBigDecimalSafe();

    try {
      transferService.executeTransfer(sourceIban, targetIban, amount);

      System.out.println("Bonifico effettuato.");

      Account source = accountRepository.findByIban(sourceIban).orElseThrow(() -> new AccountNotFoundException(sourceIban));

      Account target = accountRepository.findByIban(targetIban).orElseThrow(() -> new AccountNotFoundException(targetIban));

      System.out.println("Saldo sorgente: " + source.getBalance());
      System.out.println("Saldo destinatario: " + target.getBalance());
    } catch (AccountNotFoundException | InsufficientFundsException | IllegalArgumentException e) {
      System.out.println("⚠ " + e.getMessage());
    }

  }

  private void showTransactions() {
    System.out.print("\nInserisci IBAN: ");
    String iban = scanner.nextLine().trim();

    try {
      Account account = accountRepository.findByIban(iban).orElseThrow(() -> new AccountNotFoundException(iban));

      System.out.println("\n─── STORICO TRANSAZIONI ───────────────────────────────");

      if (account.getTransactions().isEmpty()) {
        System.out.println("Nessuna transazione.");
        return;
      }

      for (Transaction transaction : account.getTransactions()) {
        System.out.println(transaction.timestamp() + " | " + transaction.type() + " | " + transaction.amount() + " | " + transaction.description());
      }
    } catch (AccountNotFoundException e) {
      System.out.println("⚠ " + e.getMessage());
    }

  }

  private void showReporting() {
    System.out.println("\n─── REPORTISTICA ───────────────────────────────────────");

    List<Account> accounts = accountRepository.findAll();

    if (accounts.isEmpty()) {
      System.out.println("Nessun dato disponibile.");
      return;
    }

    BigDecimal totalBalance = accounts.stream().map(Account::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal averageBalance = totalBalance.divide(BigDecimal.valueOf(accounts.size()), 2, RoundingMode.HALF_UP);

    Map<CustomerType, Long> customersByType = accounts.stream().map(Account::getOwner).distinct().collect(Collectors.groupingBy(Customer::getCustomerType, Collectors.counting()));

    Map<String, Long> accountsByType = accounts.stream().collect(Collectors.groupingBy(account -> switch (account) {
      case CheckingAccount checkingAccount -> "CHECKING";
      case SavingsAccount savingsAccount -> "SAVINGS";
      default -> "UNKNOWN";
    }, Collectors.counting()));

    long totalTransactions = accounts.stream().mapToLong(account -> account.getTransactions().size()).sum();

    System.out.println("\nNumero conti: " + accounts.size());
    System.out.println("Saldo complessivo: " + totalBalance);
    System.out.println("Saldo medio: " + averageBalance);
    System.out.println("Numero transazioni: " + totalTransactions);

    System.out.println("\nClienti per tipologia:");

    customersByType.forEach((type, count) -> System.out.println(type + " -> " + count));

    System.out.println("\nConti per tipologia:");

    accountsByType.forEach((type, count) -> System.out.println(type + " -> " + count));

  }

  private void showConfiguration() {
    BankConfiguration configuration = BankConfiguration.getInstance();

    System.out.println("\n─── CONFIGURAZIONE ─────────────────────────────────────");
    System.out.println("Limite trasferimenti giornaliero: " + configuration.getDailyTransferLimit());
    System.out.println("Massimo scoperto: " + configuration.getMaxOverdraftLimit());
    System.out.println("Età minima assicurazione vita: " + configuration.getMinAgeForLifeInsurance());
    System.out.println("Nome banca: " + configuration.getBankName());

  }

  private void applyInterest() {
    System.out.println("\n─── APPLICA INTERESSI ──────────────────────────────────");

    List<SavingsAccount> savingsAccounts = accountRepository.findAll().stream().filter(SavingsAccount.class::isInstance).map(SavingsAccount.class::cast).toList();

    if (savingsAccounts.isEmpty()) {
      System.out.println("Nessun conto di risparmio disponibile.");
      return;
    }

    for (SavingsAccount account : savingsAccounts) {
      BigDecimal previousBalance = account.getBalance();
      account.applyInterest();

      System.out.println("IBAN: " + account.getIban() + " | Saldo precedente: " + previousBalance + " | Nuovo saldo: " + account.getBalance());
    }

  }

  private void processAndClassifyAccounts() {
    System.out.println("\n─── PROCESSA E CLASSIFICA CONTI ────────────────────────");

    List<Account> accounts = accountRepository.findAll();

    if (accounts.isEmpty()) {
      System.out.println("Nessun conto disponibile.");
      return;
    }

    Map<String, List<Account>> accountsByClassification = accounts.stream().collect(Collectors.groupingBy(this::classifyAccount));

    accountsByClassification.forEach((classification, classifiedAccounts) -> {
      System.out.println("\n" + classification + ":");

      classifiedAccounts.forEach(account -> System.out.println("IBAN: " + account.getIban() + " | Titolare: " + account.getOwner().getFirstName() + " " + account.getOwner().getLastName() + " | Saldo: " + account.getBalance()));
    });

  }

  private String classifyAccount(Account account) {
    if (account instanceof SavingsAccount) {
      return "CONTO RISPARMIO";
    }

    if (account instanceof CheckingAccount checkingAccount) {
      if (checkingAccount.getBalance().compareTo(BigDecimal.ZERO) < 0) {
        return "CONTO CORRENTE IN SCOPERTO";
      }

      return "CONTO CORRENTE";
    }

    return "ALTRO";

  }

  private void showRiskAndCompliance() {
    System.out.println("\n─── RISK & COMPLIANCE ─────────────────────────────────");

    List<Account> accounts = accountRepository.findAll();

    List<Customer> customers = accounts.stream().map(Account::getOwner).distinct().toList();

    Map<Long, List<Account>> accountsByCustomer = accounts.stream().collect(Collectors.groupingBy(account -> account.getOwner().getId()));

    Map<Long, List<Transaction>> transactionsByCustomer = accounts.stream().collect(Collectors.groupingBy(account -> account.getOwner().getId(), Collectors.flatMapping(account -> account.getTransactions().stream(), Collectors.toList())));

    List<RiskScore> riskScores = parallelRiskCalculationService.calculateRisks(customers, accountsByCustomer, transactionsByCustomer);

    List<Alert> alerts = customers.stream().flatMap(customer -> complianceEngine.evaluate(customer, transactionsByCustomer.getOrDefault(customer.getId(), List.of())).stream()).toList();

    Map<Long, CustomerType> customerTypes = customers.stream().collect(Collectors.toMap(Customer::getId, Customer::getCustomerType));

    ComplianceReport report = complianceReportService.generateReport(riskScores, alerts, customerTypes);

    System.out.println("\nRisk scores:");

    for (RiskScore riskScore : riskScores) {
      System.out.println("Customer " + riskScore.customerId() + " -> " + riskScore.score() + " -> " + riskScore.level());
    }

    System.out.println("\nRisk distribution:");

    report.riskDistribution().forEach((level, count) -> System.out.println(level + " -> " + count));

    System.out.println("\nOpen alerts:");

    if (report.openAlerts().isEmpty()) {
      System.out.println("Nessun alert.");
    } else {
      report.openAlerts().forEach(alert -> System.out.println(alert.level() + " - " + alert.ruleName() + " - " + alert.message()));
    }

    System.out.println("\nAverage risk by customer type:");

    report.averageRiskByCustomerType().forEach((type, average) -> System.out.println(type + " -> " + average));

    System.out.println("\nCustomers to flag:");

    if (report.customersToFlag().isEmpty()) {
      System.out.println("Nessun cliente da segnalare.");
    } else {
      report.customersToFlag().forEach(riskScore -> System.out.println("Customer " + riskScore.customerId() + " -> " + riskScore.score()));
    }

  }
}