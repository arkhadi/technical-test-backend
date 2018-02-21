package com.playtomic.tests.wallet.service.factory;

import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.impl.ThirdPartyPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceFactory {

    @Autowired
    private ThirdPartyPaymentService thirdPartyPaymentService;

    public PaymentService getPaymentService(String serviceName) {
        switch (serviceName) {
            case "thirdPartyPaymentService":
            default:
                return thirdPartyPaymentService;
        }
    }
}
