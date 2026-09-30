package com.lipari.bank.service;

import com.lipari.bank.exception.AccountNotFoundException;
import com.lipari.bank.model.Account;
import com.lipari.bank.repository.AccountRepository;

import java.math.BigDecimal;

/**
 * Service responsible for executing transfers between bank accounts.
 *
 * @author Valerio
 * @since 1.0
 */
public class TransferService {

  private final AccountRepository accountRepository;

  /**
   * Creates a transfer service using the specified account repository.
   *
   * @param accountRepository repository used to find accounts
   */
  public TransferService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  /**
   * Executes a transfer from one account to another.
   *
   * @param sourceIban IBAN of the source account
   * @param targetIban IBAN of the target account
   * @param amount     amount to transfer
   * @throws AccountNotFoundException if either account cannot be found
   */
  public void executeTransfer(String sourceIban, String targetIban, BigDecimal amount) {
    Account source = accountRepository.findByIban(sourceIban).orElseThrow(() -> new AccountNotFoundException(sourceIban));

    Account target = accountRepository.findByIban(targetIban).orElseThrow(() -> new AccountNotFoundException(targetIban));

    source.transferOut(amount, targetIban);
    target.transferIn(amount, sourceIban);
  }
}