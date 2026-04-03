package com.neviswealth.app.core.port.outbound;

import java.time.Instant;

public interface ClockPort {
    Instant now();
}
