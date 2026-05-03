package com.clougence.rdp.controller.model.dto.saas;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubscriptionDetailDTO {

    private String                 subscriptionId;

    private String                 accountIdentifier;

    private String                 customerId;

    private String                 status;

    private Long                   cancelAt;

    private Long                   canceledAt;

    private String                 clientSecret;

    private PendingUpdateDetailDTO pendingUpdate;

    private String                 latestInvoice;

    private InvoiceDetailDTO       latestInvoiceDetail;

    private String                 defaultPaymentMethodId;

    public SubscriptionDetailDTO(){
    }

    public SubscriptionDetailDTO(String subscriptionId, String accountIdentifier, String customerId, String status, Long cancelAt, Long canceledAt, String clientSecret,
                                 PendingUpdateDetailDTO pendingUpdate, String latestInvoice, InvoiceDetailDTO latestInvoiceDetail, String defaultPaymentMethodId){
        this.subscriptionId = subscriptionId;
        this.accountIdentifier = accountIdentifier;
        this.customerId = customerId;
        this.status = status;
        this.cancelAt = cancelAt;
        this.canceledAt = canceledAt;
        this.clientSecret = clientSecret;
        this.pendingUpdate = pendingUpdate;
        this.latestInvoice = latestInvoice;
        this.latestInvoiceDetail = latestInvoiceDetail;
        this.defaultPaymentMethodId = defaultPaymentMethodId;
    }
}
