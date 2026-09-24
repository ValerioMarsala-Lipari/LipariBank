package com.lipari.bank.service;

import com.lipari.bank.exception.AccountNotFoundException;
import com.lipari.bank.model.Account;
import com.lipari.bank.repository.AccountRepository;

import java.math.BigDecimal;

public class TransferService {

  private final AccountRepository accountRepository;

  public TransferService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public void executeTransfer(String sourceIban, String targetIban, BigDecimal amount) {
    Account source = accountRepository.findByIban(sourceIban).orElseThrow(() -> new AccountNotFoundException(sourceIban));

    Account target = accountRepository.findByIban(targetIban).orElseThrow(() -> new AccountNotFoundException(targetIban));

    source.transferOut(amount, targetIban);
    target.transferIn(amount, sourceIban);
  }
}