package com.lipari.bank.service;

import java.time.LocalDate;

public record PolicySummary(String policyId, String policyType, String holder, LocalDate expirationDate) {
}