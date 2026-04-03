package com.neviswealth.app.adapter.common.outbound;

import com.neviswealth.app.core.port.outbound.ClockPort;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SystemClockAdapter implements ClockPort {

    @Override
    public Instant now() {
        return Instant.now();
    }
}
