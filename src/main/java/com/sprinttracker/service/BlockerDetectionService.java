package com.sprinttracker.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;

@Service
public class BlockerDetectionService {

    private static final List<String> BLOCKER_PHRASES = List.of(
            "blocked", "block", "stuck", "waiting on", "waiting for", "can't proceed",
            "cannot proceed", "need help", "dependency", "dependent on", "pending approval",
            "not able to", "unable to", "no access", "still waiting", "held up"
    );

    /**
     * A non-empty blockers field is always a blocker; otherwise scan the free-text
     * yesterday/today updates for common blocker phrasing.
     */
    public boolean isBlocked(String yesterday, String today, String blockersText) {
        if (StringUtils.hasText(blockersText)) {
            return true;
        }
        return containsBlockerPhrase(today) || containsBlockerPhrase(yesterday);
    }

    private boolean containsBlockerPhrase(String text) {
        if (!StringUtils.hasText(text)) {
            return false;
        }
        String lower = text.toLowerCase(Locale.ROOT);
        return BLOCKER_PHRASES.stream().anyMatch(lower::contains);
    }
}
