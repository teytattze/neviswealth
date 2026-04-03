package com.neviswealth.app.adapter.common.outbound;

import com.neviswealth.app.core.port.outbound.UuidGeneratorPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RandomUuidGenerator implements UuidGeneratorPort {

    @Override
    public UUID generate() {
        return UUID.randomUUID(); // TODO: Use v7
    }
}
