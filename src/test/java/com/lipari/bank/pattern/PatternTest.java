package com.lipari.bank.pattern;

import com.lipari.bank.model.Account;
import com.lipari.bank.model.CheckingAccount;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.CustomerType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PatternTest {

  public static void main(String[] args) {

    Customer mario = new Customer("RSSMRA80A01H501X", "Mario", "Rossi", CustomerType.PRIVATE);

    Customer laura = new Customer("BNCLRA85B02H501Y", "Laura", "Bianchi", CustomerType.PRIVATE);

    Account source = new CheckingAccount("ACC-001", LocalDate.now(), "IT60X0000000000000000000001", BigDecimal.valueOf(1000), mario, BigDecimal.ZERO);

    Account target = new CheckingAccount("ACC-002", LocalDate.now(), "IT60X0000000000000000000002", BigDecimal.valueOf(500), laura, BigDecimal.ZERO);

    TransferCommand command = new TransferCommand(source, target, BigDecimal.valueOf(300));

    System.out.println("Saldo iniziale sorgente: " + source.getBalance());
    System.out.println("Saldo iniziale destinatario: " + target.getBalance());

    command.execute();

    System.out.println("Dopo execute:");
    System.out.println("Saldo sorgente: " + source.getBalance());
    System.out.println("Saldo destinatario: " + target.getBalance());

    command.undo();

    System.out.println("Dopo undo:");
    System.out.println("Saldo sorgente: " + source.getBalance());
    System.out.println("Saldo destinatario: " + target.getBalance());
  }
}