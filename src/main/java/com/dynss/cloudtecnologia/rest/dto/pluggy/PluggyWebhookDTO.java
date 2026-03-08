package com.dynss.cloudtecnologia.rest.dto.pluggy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
