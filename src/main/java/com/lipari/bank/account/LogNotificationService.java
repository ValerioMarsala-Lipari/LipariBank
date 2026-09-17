package com.lipari.bank.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("logNotificationService")
public class LogNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(LogNotificationService.class);

    @Override
    public void notify(String accountId, String message) {
        log.info("[LOG-NOTIFY] account={} message={}", accountId, message);
    }
}
