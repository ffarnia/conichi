package com.conichi.challenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Fazel on 11/3/2019.
 */
public class VatLookupResponse {

    @JsonProperty("CountryCode")
    private String countryCode;

    @JsonProperty("VatNumber")
    private String vatNumber;

    @JsonProperty("IsValid")
    private boolean valid;

    @JsonProperty("BusinessName")
    private String businessName;

    @JsonProperty("BusinessAddress")
    private String businessAddress;



}