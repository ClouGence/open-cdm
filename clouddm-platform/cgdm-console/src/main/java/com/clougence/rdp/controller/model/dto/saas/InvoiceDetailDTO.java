package com.clougence.rdp.controller.model.dto.saas;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceDetailDTO {

    private List<ItemDTO>          items;

    private String                 subscriptionId;

    private String                 invoiceId;

    private Long                   totalAmount;

    private Long                   periodStart;

    private Long                   periodEnd;

    private Long                   nextPaymentAttempt;

    private Long                   amountDue;

    private String                 clientSecret;

    private String                 status;

    private Long                   created;

    private PaymentIntentDetailDTO paymentIntent;

    public InvoiceDetailDTO(){
    }

    public InvoiceDetailDTO(List<ItemDTO> items, String subscriptionId, String invoiceId, Long totalAmount, Long periodStart, Long periodEnd, Long nextPaymentAttempt,
                            Long amountDue, String clientSecret, String status, Long created, PaymentIntentDetailDTO paymentIntent){
        this.items = items;
        this.subscriptionId = subscriptionId;
        this.invoiceId = invoiceId;
        this.totalAmount = totalAmount;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.nextPaymentAttempt = nextPaymentAttempt;
        this.amountDue = amountDue;
        this.clientSecret = clientSecret;
        this.status = status;
        this.created = created;
        this.paymentIntent = paymentIntent;
    }
}
