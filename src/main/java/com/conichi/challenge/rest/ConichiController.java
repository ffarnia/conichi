package com.conichi.challenge.rest;

import com.conichi.challenge.dto.CurrencyConvertResponse;
import com.conichi.challenge.dto.VatLookupRequest;
import com.conichi.challenge.dto.VatLookupResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by Fazel on 11/3/2019.
 */
@CacheConfig(cacheNames = {"conichiServices"})
@RestController
@RequestMapping("/api")
public class ConichiController {

    @Value("${currency.converter.access.key}")
    private String converterAccessKey;

    @Value("${currency.converter.endpoint}")
    private String converterEndpoint;

    @Value("${vat.validation.endpoint}")
    private String vatEndpoint;

    @Value("${vat.api.key}")
    private String vatApiKey;

    public ConichiController() {
    }

    @RequestMapping(method = RequestMethod.GET, path = "/currentTime")
    public LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }

    @Cacheable
    @RequestMapping(method = RequestMethod.GET, path = "/currency/convert/amount/{amount}/source-currency/{sourceCurrency}/target-currency/{targetCurrency}")
    public CurrencyConvertResponse convertCurrency(@PathVariable(name = "amount", required = true) Integer amount, @PathVariable(name = "sourceCurrency", required = true) String sourceCurrency, @PathVariable(name = "targetCurrency", required = true) String targetCurrency) throws RemoteException {
        ResponseEntity<CurrencyConvertResponse> response = null;
        try {
            response = new RestTemplate().getForEntity(converterEndpoint + "?access_key=" + converterAccessKey + "&source={sourceCurrency}&currencies={targetCurrency}", CurrencyConvertResponse.class, sourceCurrency, targetCurrency);
        } catch (RestClientException e) {
            throw new RemoteException("Error in currency conversion public api");
        }
        CurrencyConvertResponse convertDto = response.getBody();
        CurrencyConvertResponse currencyConvertResponse = new CurrencyConvertResponse();
        if (!convertDto.isSuccess()) {
            return convertDto;
        }
        Map<String, BigDecimal> quotes = convertDto.getQuotes();
        currencyConvertResponse.setTargetCurrency(targetCurrency);
        currencyConvertResponse.setSource(sourceCurrency);
        currencyConvertResponse.setTargetCurrencyAmount(quotes.get(sourceCurrency.concat(targetCurrency)).multiply(BigDecimal.valueOf(amount)));
        currencyConvertResponse.setQuotes(quotes);
        currencyConvertResponse.setSuccess(convertDto.isSuccess());
        return currencyConvertResponse;

    }

    @Cacheable
    @RequestMapping(method = RequestMethod.POST, path = "/lookupVat")
    public ResponseEntity<VatLookupResponse> lookupVAT(@RequestBody VatLookupRequest vatLookupRequest) throws RemoteException {
        ResponseEntity<VatLookupResponse> response = null;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Apikey", vatApiKey);
        HttpEntity<VatLookupRequest> vatLookupRequestHttpEntity = new HttpEntity<>(vatLookupRequest, headers);
        try {
            response = new RestTemplate().postForEntity(vatEndpoint, vatLookupRequestHttpEntity, VatLookupResponse.class);
        } catch (RestClientException e) {
            throw new RemoteException("Error in VAT number validation public api");
        }
        return response;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RemoteException.class)
    public String getRemoteException(RemoteException e) {
        return e.getMessage();
    }
}
