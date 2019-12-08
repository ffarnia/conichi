package com.conichi.challenge.rest;

import com.conichi.challenge.dto.CurrencyConvertDto;
import com.conichi.challenge.dto.VatLookupRequest;
import com.conichi.challenge.dto.VatLookupResponse;
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
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Created by Fazel on 11/3/2019.
 */
@CacheConfig(cacheNames = {"conichiServices"})
@RestController
@RequestMapping("/api")
public class ConichiController {

    private static final String CONVERTER_ACCESS_KEY = "52d9bcf431d0af0ee45df8718cb67362";
    private static final String CONVERTER_END_POINT = "http://apilayer.net/api/live";
    private static final String VAT_END_POINT = "https://api.cloudmersive.com/validate/vat/lookup";
    private static final String VAT_API_KEY = "28e63794-ef8a-4616-80bb-26fdd3709a19";

    public ConichiController() {
    }

    @RequestMapping(method = RequestMethod.GET, path = "/currentTime")
    public LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }

    @Cacheable
    @RequestMapping(method = RequestMethod.GET, path = "/currency/convert/amount/{amount}/source-currency/{sourceCurrency}/target-currency/{targetCurrency}")
    public CurrencyConvertDto convertCurrency(@PathVariable("amount") Integer amount,@PathVariable("sourceCurrency") String sourceCurrency, @PathVariable("targetCurrency") String targetCurrency) throws RemoteException {
        ResponseEntity<CurrencyConvertDto> response = null;
        try {
            response = new RestTemplate().getForEntity(CONVERTER_END_POINT + "?access_key=" + CONVERTER_ACCESS_KEY + "&source={sourceCurrency}&currencies={targetCurrency}", CurrencyConvertDto.class, sourceCurrency, targetCurrency);
        } catch (RestClientException e) {
            throw new RemoteException("Error in currency conversion public api");
        }
        CurrencyConvertDto convertDto = response.getBody();
        CurrencyConvertDto currencyConvertDto = new CurrencyConvertDto();
        if (!convertDto.isSuccess()) {
            return convertDto;
        }
        Map<String, BigDecimal> quotes = convertDto.getQuotes();
        currencyConvertDto.setTargetCurrency(targetCurrency);
        currencyConvertDto.setSource(sourceCurrency);
        currencyConvertDto.setTargetCurrencyAmount(quotes.get(sourceCurrency.concat(targetCurrency)).multiply(BigDecimal.valueOf(amount)));
        currencyConvertDto.setQuotes(quotes);
        currencyConvertDto.setSuccess(convertDto.isSuccess());
        return currencyConvertDto;

    }

    @Cacheable
    @RequestMapping(method = RequestMethod.POST, path = "/lookupVat")
    public ResponseEntity<VatLookupResponse> lookupVAT(@RequestBody VatLookupRequest vatLookupRequest) throws RemoteException {
        ResponseEntity<VatLookupResponse> response = null;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Apikey", VAT_API_KEY);
        HttpEntity<VatLookupRequest> vatLookupRequestHttpEntity = new HttpEntity<>(vatLookupRequest, headers);
        try {
            response = new RestTemplate().postForEntity(VAT_END_POINT, vatLookupRequestHttpEntity, VatLookupResponse.class);
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
