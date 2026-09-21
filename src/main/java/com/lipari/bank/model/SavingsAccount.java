package com.lipari.bank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class SavingsAccount extends Account {

    private final BigDecimal interestRate;

    public SavingsAccount(
            String id,
            LocalDate creationDate,
            String iban,
            BigDecimal balance,
            Customer owner,
            BigDecimal interestRate
    ) {
        super(id, creationDate, iban, balance, owner);

        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Interest rate cannot be null or negative"
            );
        }

        this.interestRate = interestRate;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void applyInterest() {
        BigDecimal interest = getBalance()
                .multiply(interestRate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        if (interest.compareTo(BigDecimal.ZERO) > 0) {
            setBalance(getBalance().add(interest));

            addTransaction(
                    new Transaction(
                            TransactionType.DEPOSIT,
                            interest,
                            "Interest applied",
                            LocalDateTime.now()
                    )
            );
        }
    }

    @Override
    public BigDecimal calculateTaxAmount() {
        return BigDecimal.ZERO;
    }
}