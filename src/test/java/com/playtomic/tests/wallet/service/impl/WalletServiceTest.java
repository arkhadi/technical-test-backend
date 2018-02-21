package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.WalletServiceException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class WalletServiceTest {

    @InjectMocks
    private WalletService classToTest;

    @Mock
    private WalletRepository walletRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldSaveWallet() {
        //Given
        Wallet wallet = new Wallet();
        wallet.setWalletName("testWallet");
        wallet.setTotalAmount(BigDecimal.ONE);
        given(walletRepository.saveAndFlush(wallet)).willReturn(wallet);

        //When
        Wallet result = classToTest.saveWallet(wallet);

        //Then
        assertThat(result, is(wallet));
    }

    @Test
    public void shouldFindAllWallets() {
        //Given
        List<Wallet> listOfWallets = new ArrayList<>();
        given(walletRepository.findAll()).willReturn(listOfWallets);

        //When
        List<Wallet> result = classToTest.findAllWallets();

        //Then
        assertThat(result, is(listOfWallets));

    }

    @Test
    public void shouldFindByWalletName() {
        //Given
        Wallet wallet = new Wallet();
        wallet.setWalletName("testWallet");
        wallet.setTotalAmount(BigDecimal.ONE);
        given(walletRepository.findByWalletName("testWallet")).willReturn(wallet);

        //When
        Wallet result = classToTest.findByWalletName("testWallet");

        //Then
        assertThat(result, is(wallet));
    }

    @Test
    public void shouldWithdraw() throws WalletServiceException {
        //Given
        Wallet wallet = new Wallet();
        wallet.setWalletName("testWallet");
        wallet.setTotalAmount(BigDecimal.TEN);
        BigDecimal amount = BigDecimal.ONE;
        given(walletRepository.findByWalletName("testWallet")).willReturn(wallet);
        given(walletRepository.saveAndFlush(wallet)).willReturn(wallet);

        //When
        Wallet result = classToTest.withdraw(amount, wallet);

        //Then
        assertThat(result.getWalletName(), is("testWallet"));
        assertThat(result.getTotalAmount(), is(new BigDecimal("9")));
    }

    @Test
    public void shouldThrowWalletServiceExceptionWhenAmountBiggerThanTotalAmount() throws WalletServiceException {
        //Given
        Wallet wallet = new Wallet();
        wallet.setWalletName("testWallet");
        wallet.setTotalAmount(BigDecimal.ONE);
        BigDecimal amount = BigDecimal.TEN;
        given(walletRepository.findByWalletName("testWallet")).willReturn(wallet);

        expectedException.expect(WalletServiceException.class);
        expectedException.expectMessage("Amount to withdraw is bigger than the total amount in the wallet: testWallet");

        //When
        classToTest.withdraw(amount, wallet);
    }

    @Test
    public void shouldTopup() {
        //Given
        Wallet wallet = new Wallet();
        wallet.setWalletName("testWallet");
        wallet.setTotalAmount(BigDecimal.ONE);
        BigDecimal amount = BigDecimal.TEN;
        given(walletRepository.findByWalletName("testWallet")).willReturn(wallet);
        given(walletRepository.saveAndFlush(wallet)).willReturn(wallet);

        //When
        Wallet result = classToTest.topup(amount, wallet);

        //Then
        assertThat(result.getWalletName(), is("testWallet"));
        assertThat(result.getTotalAmount(), is(new BigDecimal("11")));
    }
}