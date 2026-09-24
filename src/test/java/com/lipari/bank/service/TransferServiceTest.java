package com.lipari.bank.service;

import com.lipari.bank.exception.AccountNotFoundException;
import com.lipari.bank.exception.InsufficientFundsException;
import com.lipari.bank.model.Account;
import com.lipari.bank.model.CheckingAccount;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.CustomerType;
import com.lipari.bank.repository.AccountRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransferServiceTest {

  public static void main(String[] args) {

    Customer customer1 = new Customer("RSSMRA80A01H501X", "Mario", "Rossi", CustomerType.PRIVATE);

    Customer customer2 = new Customer("BNCLRA85B02H501Y", "Laura", "Bianchi", CustomerType.PRIVATE);

    Account source = new CheckingAccount("ACC-001", LocalDate.now(), "IT60X0542811101000000123456", BigDecimal.valueOf(1000), customer1, BigDecimal.valueOf(500));

    Account target = new CheckingAccount("ACC-002", LocalDate.now(), "IT60X0542811101000000654321", BigDecimal.valueOf(500), customer2, BigDecimal.valueOf(500));

    AccountRepository repository = new AccountRepository();

    repository.save(source);
    repository.save(target);

    TransferService transferService = new TransferService(repository);

    transferService.executeTransfer(source.getIban(), target.getIban(), BigDecimal.valueOf(200));

    System.out.println("Source balance: " + source.getBalance());
    System.out.println("Target balance: " + target.getBalance());

    System.out.println("Source transactions: " + source.getTransactions().size());

    System.out.println("Target transactions: " + target.getTransactions().size());

    try {
      transferService.executeTransfer(
          source.getIban(),
          "IT60X0542811101000000999999",
          BigDecimal.valueOf(100)
      );
    } catch (AccountNotFoundException e) {
      System.out.println("Error: " + e.getMessage());
      System.out.println("Code: " + e.getErrorCode());
      System.out.println("Source balance: " + source.getBalance());
      System.out.println("Target balance: " + target.getBalance());
    }

    try {
      transferService.executeTransfer(
          source.getIban(),
          target.getIban(),
          BigDecimal.valueOf(2000)
      );
    } catch (InsufficientFundsException e) {
      System.out.println("Error: " + e.getMessage());
      System.out.println("Code: " + e.getErrorCode());
      System.out.println("Source balance: " + source.getBalance());
      System.out.println("Target balance: " + target.getBalance());
    }
  }
}