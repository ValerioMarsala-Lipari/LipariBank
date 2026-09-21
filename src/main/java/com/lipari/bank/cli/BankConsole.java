package com.lipari.bank.cli;

import com.lipari.bank.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BankConsole {

  private final Scanner scanner = new Scanner(System.in);

  // ─── Entry point ───────────────────────────────────────────────────────────

  public static void main(String[] args) {
    new BankConsole().run();
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
        case 5 -> showTransactions();
        case 6 -> applyInterestToSavings();
        case 7 -> processAccounts();
        case 0 -> {
          System.out.println("\nArrivederci da LipariBank!");
          running = false;
        }
        default -> System.out.println("⚠ Input non valido!");
      }
    }
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
        ║  5. Storico transazioni              ║
        ║  6. Applica interessi (risparmio)    ║
        ║  7. Processa e classifica conti      ║
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
    System.out.println("\n─── COMING SOON ─────────────────────────────────────────");
  }

  private void showBalance() {
    System.out.println("\n─── COMING SOON ─────────────────────────────────────────");
  }

  private void makeDeposit() {
    System.out.println("\n─── COMING SOON ─────────────────────────────────────────");
  }

  private void makeWithdrawal() {
    System.out.println("\n─── COMING SOON ─────────────────────────────────────────");
  }

  private void showTransactions() {
    System.out.println("\n─── COMING SOON ─────────────────────────────────────────");
  }

  private void applyInterestToSavings() {
    System.out.println("\n─── COMING SOON ─────────────────────────────────────────");
  }

  private void processAccounts() {
    System.out.println("\n─── COMING SOON ─────────────────────────────────────────");
  }
}