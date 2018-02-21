package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.WalletServiceException;
import com.playtomic.tests.wallet.service.factory.PaymentServiceFactory;
import com.playtomic.tests.wallet.service.impl.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class WalletController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private WalletService walletService;

    @Autowired
    private PaymentServiceFactory paymentServiceFactory;

    @RequestMapping(value = "/wallet/",
            method = RequestMethod.POST,
            consumes = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createWallet(@RequestBody Wallet wallet, UriComponentsBuilder ucBuilder) {
        LOGGER.info("Creating new wallet with name: {}", wallet.getWalletName());
        Wallet createdWallet = walletService.saveWallet(wallet);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/wallet/{walletName}").buildAndExpand(createdWallet.getWalletName()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/wallet/",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Wallet>> getAllWallets() {
        LOGGER.info("Retrieving all wallets");
        List<Wallet> wallets = walletService.findAllWallets();
        if (wallets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(wallets, HttpStatus.OK);
    }

    @RequestMapping(value = "/wallet/{walletName}",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Wallet> getWallet(@PathVariable("walletName") String walletName) {
        LOGGER.info("Fetching wallet with name: {}", walletName);
        Wallet wallet = walletService.findByWalletName(walletName);
        if (wallet == null) {
            LOGGER.error("Wallet with name {} not found.", walletName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @RequestMapping(value = "/wallet/{walletName}/withdraw/{amount}",
            method = RequestMethod.PUT,
            consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Wallet> withdrawFromWallet(@PathVariable("walletName")String walletName,
                                                     @PathVariable("amount") BigDecimal amount,
                                                     @RequestBody Wallet currentWallet){
        LOGGER.info("Withdrawing '{}' from wallet '{}'", amount, walletName);
        try {
            Wallet updatedWallet = walletService.withdraw(amount, currentWallet);
            return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
        } catch (WalletServiceException e) {
            LOGGER.error("The amount {} to withdraw from wallet: {} is bigger than the total {} in the wallet",
                    amount, walletName, currentWallet.getTotalAmount());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/wallet/{walletName}/topup/{amount}",
            method = RequestMethod.PUT,
            consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Wallet> topupWallet(@PathVariable("walletName")String walletName,
                                                     @PathVariable("amount") BigDecimal amount,
                                                     @RequestBody Wallet currentWallet){
        LOGGER.info("Topping up '{}' to wallet '{}'", amount, walletName);
        Wallet updatedWallet = walletService.topup(amount, currentWallet);
        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }

    @RequestMapping(value = "/wallet/{walletName}/topup/{amount}/{thirdPartyName}",
            method = RequestMethod.PUT,
            consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Wallet> topupWalletWithThirdParty(@PathVariable("walletName")String walletName,
                                                            @PathVariable("amount") BigDecimal amount,
                                                            @PathVariable("thirdPartyName") String thirdPartyName,
                                                            @RequestBody Wallet currentWallet) {
        LOGGER.info("Topping up '{}' to wallet '{}' from '{}'", amount, walletName, thirdPartyName);
        PaymentService paymentService = paymentServiceFactory.getPaymentService(thirdPartyName);
        try {
            paymentService.charge(amount);
            Wallet updatedWallet = walletService.topup(amount, currentWallet);
            return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
        } catch (PaymentServiceException e) {
            LOGGER.error("The thirdPartyService failed for amount '{}'", amount);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
