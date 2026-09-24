package com.lipari.bank.cli;

import com.lipari.bank.exception.AccountNotFoundException;
import com.lipari.bank.exception.InsufficientFundsException;
import com.lipari.bank.model.*;
import com.lipari.bank.repository.AccountRepository;
import com.lipari.bank.service.TransferService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class BankConsole {

  private final Scanner scanner = new Scanner(System.in);
  private final AccountRepository accountRepository = new AccountRepository();
  private final TransferService transferService;

  public BankConsole() {
    this.transferService = new TransferService(accountRepository);
  }

  // ─── Entry point ───────────────────────────────────────────────────────────

  public static void main(String[] args) {
    BankConsole console = new BankConsole();
    console.initializeData();
    console.run();
  }

  // ─── Main loop ─────────────────────────────────────────────────────────────

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
        case 7 -> configuration();
        case 8 -> applyInterestToSavings();
        case 9 -> processAccounts();
        case 0 -> {
          System.out.println("\nArrivederci da LipariBank!");
          running = false;
        }
        default -> System.out.println("⚠ Input non valido!");
      }
    }
  }

  // ─── Inizializzazione dati ──────────────────────────────────────────────────────────────────
  private void initializeData() {
    Customer mario = new Customer("RSSMRA80A01H501X", "Mario", "Rossi", CustomerType.PRIVATE);

    Customer laura = new Customer("BNCLRA85B02H501Y", "Laura", "Bianchi", CustomerType.PRIVATE);

    Account marioAccount = new CheckingAccount("ACC-001", LocalDate.now(), "IT60X0542811101000000123456", BigDecimal.valueOf(1000), mario, BigDecimal.valueOf(200));

    Account lauraAccount = new CheckingAccount("ACC-002", LocalDate.now(), "IT60X0542811101000000654321", BigDecimal.valueOf(500), laura, BigDecimal.valueOf(200));

    accountRepository.save(marioAccount);
    accountRepository.save(lauraAccount);
  }

  // ─── Menu ──────────────────────────────────────────────────────────────────

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
        ║  7. Configurazione                   ║
        ║  8. Applica interessi (risparmio)    ║
        ║  9. Processa e classifica conti      ║
        ║  0. Esci                             ║
        ╚══════════════════════════════════════╝""");
    System.out.print("  Scelta: ");
  }

  // ─── Input helper ──────────────────────────────────────────────────────────

  /**
   * Legge un intero da console.
   */
  private int readIntSafe() {
    String input = scanner.nextLine().trim();

    try {
      return Integer.parseInt(input);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  // ─── Operazioni sui conti ──────────────────────────────────────────────────

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

  private BigDecimal readBigDecimalSafe() {
    String input = scanner.nextLine().trim();

    try {
      return new BigDecimal(input);
    } catch (NumberFormatException e) {
      return BigDecimal.valueOf(-1);
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

  private void configuration() {
    System.out.println("\n─── COMING SOON ─────────────────────────────────────────");
  }

  private void applyInterestToSavings() {
    System.out.println("\n─── COMING SOON ─────────────────────────────────────────");
  }

  private void processAccounts() {
    System.out.println("\n─── COMING SOON ─────────────────────────────────────────");
  }
}