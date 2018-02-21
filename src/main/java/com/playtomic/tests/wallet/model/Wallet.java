package com.playtomic.tests.wallet.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "WALLET")
public class Wallet {

    @Id
    @Column(name = "WALLET_NAME", unique = true)
    private String walletName;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    public Wallet(){}

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
