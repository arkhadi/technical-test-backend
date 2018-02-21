package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.service.PaymentServiceException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class ThirdPartyPaymentServiceTest {

    ThirdPartyPaymentService classToTest;

    @Before
    public void setUp() {
        classToTest = new ThirdPartyPaymentService();
    }

    @Test(expected = PaymentServiceException.class)
    public void test_exception() throws PaymentServiceException {
        //Given
        //When
        classToTest.charge(new BigDecimal(5));

        //Then PaymentServiceException should haev been thrown
    }

    @Test
    public void test_ok() throws PaymentServiceException {
        //Given
        //When
        //Then
        classToTest.charge(new BigDecimal(15));
    }
}
