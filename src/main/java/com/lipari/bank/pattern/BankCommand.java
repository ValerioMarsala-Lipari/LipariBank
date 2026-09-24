package com.lipari.bank.pattern;

public sealed interface BankCommand permits DepositCommand, WithdrawCommand, TransferCommand {

  void execute();

  void undo();
}