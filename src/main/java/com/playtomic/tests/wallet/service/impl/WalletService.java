package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.WalletServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public Wallet saveWallet(Wallet wallet) {
        return walletRepository.saveAndFlush(wallet);
    }

    public List<Wallet> findAllWallets() {
        return walletRepository.findAll();
    }

    public Wallet findByWalletName(String walletName) {
        return walletRepository.findByWalletName(walletName);
    }

    public Wallet withdraw(BigDecimal amount, Wallet currentWallet) throws WalletServiceException {
        Wallet wallet = findByWalletName(currentWallet.getWalletName());
        BigDecimal totalAmount = wallet.getTotalAmount();
        if (totalAmount.compareTo(amount) < 0) {
            throw new WalletServiceException("Amount to withdraw is bigger than the total amount in the wallet: " + wallet.getWalletName());
        }
        wallet.setTotalAmount(totalAmount.subtract(amount));
        return walletRepository.saveAndFlush(wallet);
    }

    public Wallet topup(BigDecimal amount, Wallet currentWallet) {
        Wallet wallet = findByWalletName(currentWallet.getWalletName());
        wallet.setTotalAmount(wallet.getTotalAmount().add(amount));
        return walletRepository.saveAndFlush(wallet);
    }
}
