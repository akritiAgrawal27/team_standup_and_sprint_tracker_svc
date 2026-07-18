package com.sprinttracker.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockerDetectionServiceTest {

    private final BlockerDetectionService service = new BlockerDetectionService();

    @Test
    void flagsWhenBlockersTextPresent() {
        assertTrue(service.isBlocked("finished login page", "starting dashboard", "waiting on design review"));
    }

    @Test
    void flagsWhenTodayContainsBlockerPhrase() {
        assertTrue(service.isBlocked("finished login page", "I'm stuck on the payment integration", null));
    }

    @Test
    void flagsWhenYesterdayContainsBlockerPhrase() {
        assertTrue(service.isBlocked("was blocked on API access", "working on tests now", ""));
    }

    @Test
    void doesNotFlagNormalUpdate() {
        assertFalse(service.isBlocked("finished login page", "starting dashboard work", ""));
    }

    @Test
    void doesNotFlagEmptyUpdate() {
        assertFalse(service.isBlocked(null, null, null));
    }

    @Test
    void matchIsCaseInsensitive() {
        assertTrue(service.isBlocked("all good", "BLOCKED on the release", null));
    }
}
