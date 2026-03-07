package com.dynss.cloudtecnologia.rest.dto.pluggy;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;


@Data
@Builder
public class PluggyWebhookDTO {
    private String clientUserId;
    private UUID itemId;
    private UUID eventId;
    private UUID clientId;
    private String id;
    private String event;
    private String triggeredBy;
    private Long connectorId;
    private PluggyWebhookErrorDTO error;
    private DataPayload data;

    @Data
    public static class DataPayload {
        private String status;
    }
}
