package com.conichi.challenge.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Author Fazel Farnia
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

    public boolean isValid() {
        return valid;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    @Override
    public String toString() {
        return "VatLookupResponse{" +
                "countryCode='" + countryCode + '\'' +
                ", vatNumber='" + vatNumber + '\'' +
                ", valid=" + valid +
                ", businessName='" + businessName + '\'' +
                ", businessAddress='" + businessAddress + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VatLookupResponse)) return false;

        VatLookupResponse that = (VatLookupResponse) o;

        if (isValid() != that.isValid()) return false;
        if (countryCode != null ? !countryCode.equals(that.countryCode) : that.countryCode != null) return false;
        if (vatNumber != null ? !vatNumber.equals(that.vatNumber) : that.vatNumber != null) return false;
        return !(businessName != null ? !businessName.equals(that.businessName) : that.businessName != null);

    }

    @Override
    public int hashCode() {
        int result = countryCode != null ? countryCode.hashCode() : 0;
        result = 31 * result + (vatNumber != null ? vatNumber.hashCode() : 0);
        result = 31 * result + (isValid() ? 1 : 0);
        result = 31 * result + (businessName != null ? businessName.hashCode() : 0);
        return result;
    }
}