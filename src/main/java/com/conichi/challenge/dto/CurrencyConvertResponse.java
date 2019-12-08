package com.conichi.challenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Author Fazel Farnia
 */
public class CurrencyConvertResponse {

    private boolean success;
    private String terms;
    private String privacy;
    private Date timestamp;
    private String source;
    private Map<String, BigDecimal> quotes;
    private BigDecimal targetCurrencyAmount;
    private String targetCurrency;
    private MessageException error;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public MessageException getError() {
        return error;
    }

    public void setError(MessageException error) {
        this.error = error;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public BigDecimal getTargetCurrencyAmount() {
        return targetCurrencyAmount;
    }

    public void setTargetCurrencyAmount(BigDecimal targetCurrencyAmount) {
        this.targetCurrencyAmount = targetCurrencyAmount;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Map<String, BigDecimal> getQuotes() {
        return quotes;
    }

    public void setQuotes(Map<String, BigDecimal> quotes) {
        this.quotes = quotes;
    }


}
