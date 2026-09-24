package com.lipari.bank.service;

import com.lipari.bank.model.Account;
import com.lipari.bank.model.CheckingAccount;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.CustomerType;
import com.lipari.bank.pattern.TransferCommand;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConcurrentBatchTest {

  public static void main(String[] args) {

    Customer customer = new Customer(
        "RSSMRA80A01H501Z",
        "Mario",
        "Rossi",
        CustomerType.PRIVATE
    );

    Account source = new CheckingAccount(
        "1",
        LocalDate.now(),
        "IT01",
        new BigDecimal("10000"),
        customer,
        BigDecimal.ZERO
    );

    Account target = new CheckingAccount(
        "2",
        LocalDate.now(),
        "IT02",
        BigDecimal.ZERO,
        customer,
        BigDecimal.ZERO
    );

    List<TransferCommand> commands = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      commands.add(
          new TransferCommand(
              source,
              target,
              new BigDecimal("100")
          )
      );
    }

    ConcurrentBatchService service = new ConcurrentBatchService();

    service.executeBatchTransfers(commands);

    System.out.println("Source: " + source.getBalance());
    System.out.println("Target: " + target.getBalance());
  }
}