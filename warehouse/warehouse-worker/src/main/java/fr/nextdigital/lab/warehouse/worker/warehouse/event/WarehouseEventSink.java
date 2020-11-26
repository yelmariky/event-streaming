package fr.nextdigital.lab.warehouse.worker.warehouse.event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface WarehouseEventSink {
    String INPUT = "warehouse";

    @Input(WarehouseEventSink.INPUT)
    SubscribableChannel input();
}
