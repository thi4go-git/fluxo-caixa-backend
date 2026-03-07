package com.dynss.cloudtecnologia.model.enums;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum TipoEventoWebhook {

    ALL("all"),

    ITEM_CREATED("item/created"),
    ITEM_UPDATED("item/updated"),
    ITEM_ERROR("item/error"),
    ITEM_DELETED("item/deleted"),
    ITEM_WAITING_USER_INPUT("item/waiting_user_input"),
    ITEM_WAITING_USER_ACTION("item/waiting_user_action"),
    ITEM_LOGIN_SUCCEEDED("item/login_succeeded"),

    CONNECTOR_STATUS_UPDATED("connector/status_updated"),

    TRANSACTIONS_CREATED("transactions/created"),
    TRANSACTIONS_UPDATED("transactions/updated"),
    TRANSACTIONS_DELETED("transactions/deleted"),

    PAYMENT_INTENT_CREATED("payment_intent/created"),
    PAYMENT_INTENT_COMPLETED("payment_intent/completed"),
    PAYMENT_INTENT_WAITING_PAYER_AUTHORIZATION("payment_intent/waiting_payer_authorization"),
    PAYMENT_INTENT_ERROR("payment_intent/error"),

    PAYMENT_REQUEST_UPDATED("payment_request/updated"),

    SCHEDULED_PAYMENT_CREATED("scheduled_payment/created"),
    SCHEDULED_PAYMENT_COMPLETED("scheduled_payment/completed"),
    SCHEDULED_PAYMENT_ERROR("scheduled_payment/error"),
    SCHEDULED_PAYMENT_CANCELED("scheduled_payment/canceled"),
    SCHEDULED_PAYMENT_ALL_COMPLETED("scheduled_payment/all_completed"),
    SCHEDULED_PAYMENT_ALL_CREATED("scheduled_payment/all_created"),

    BOLETO_UPDATED("boleto/updated"),

    AUTOMATIC_PIX_PAYMENT_CREATED("automatic_pix_payment/created"),
    AUTOMATIC_PIX_PAYMENT_COMPLETED("automatic_pix_payment/completed"),
    AUTOMATIC_PIX_PAYMENT_ERROR("automatic_pix_payment/error"),
    AUTOMATIC_PIX_PAYMENT_CANCELED("automatic_pix_payment/canceled"),

    SMART_TRANSFER_PREAUTHORIZATION_COMPLETED("smart_transfer_preauthorization/completed"),
    SMART_TRANSFER_PREAUTHORIZATION_ERROR("smart_transfer_preauthorization/error"),
    SMART_TRANSFER_PAYMENT_COMPLETED("smart_transfer_payment/completed"),
    SMART_TRANSFER_PAYMENT_ERROR("smart_transfer_payment/error");

    private final String descricao;

    TipoEventoWebhook(String descricao) {
        this.descricao = descricao;
    }

    public static TipoEventoWebhook fromDescricao(String descricao) {
        return Arrays.stream(values())
                .filter(e -> e.descricao.equalsIgnoreCase(descricao))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Evento webhook inválido: " + descricao));
    }
}
