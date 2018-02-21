package com.playtomic.tests.wallet.service.factory;

import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.impl.ThirdPartyPaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceFactoryTest {

    @InjectMocks
    private PaymentServiceFactory classToTest;

    @Mock
    private ThirdPartyPaymentService thirdPartyPaymentService;

    @Test
    public void shouldGetPaymentService() {
        //Given
        //When
        PaymentService result = classToTest.getPaymentService("thirdPartyPaymentService");

        //Then
        assertThat(result, is(thirdPartyPaymentService));
    }


}