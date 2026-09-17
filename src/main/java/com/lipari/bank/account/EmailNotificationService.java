package com.lipari.bank.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("emailNotificationService")
public class EmailNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);

    @Override
    public void notify(String accountId, String message) {
        log.info("[EMAIL-NOTIFY] account={} message={}", accountId, message);
    }
}
