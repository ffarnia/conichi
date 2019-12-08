package com.conichi.challenge;

import com.conichi.challenge.dto.CurrencyConvertResponse;
import com.conichi.challenge.dto.VatLookupRequest;
import com.conichi.challenge.dto.VatLookupResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ConichiApplication.class)
@WebAppConfiguration
class ConichiApplicationTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    private MvcResult setUpForGetCall(String uri) {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = null;
        try {
            mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        } catch (Exception e) {
            LOGGER.error("Error in creating integration test mvc request for Get calls");
        }
        return mvcResult;
    }

    private MvcResult setUpForPostCall(String uri, String jsonRequest) {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = null;
        try {
            mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(jsonRequest).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        } catch (Exception e) {
            LOGGER.error("Error in creating integration test mvc request for Post calls");
        }
        return mvcResult;
    }

    @Test
    public void checkCurrestTime() {
        MvcResult mvcResult = setUpForGetCall("/api/currentTime");
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void checkResult_whenSpecificAmountAndCurrenciesIsGiven() throws UnsupportedEncodingException, JsonProcessingException {
        MvcResult mvcResult = setUpForGetCall("/api/currency/convert/amount/100/source-currency/USD/target-currency/EUR");
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        CurrencyConvertResponse convertDto = mapper.readValue(content, CurrencyConvertResponse.class);
        assertTrue(convertDto.isSuccess());
        assertEquals(new BigDecimal("90.426100"), convertDto.getTargetCurrencyAmount());
    }


    @Test
    public void checkErrorCode_whenInvalidTargetCurrencyIsGiven() throws UnsupportedEncodingException, JsonProcessingException {
        MvcResult mvcResult = setUpForGetCall("/api/currency/convert/amount/100/source-currency/USD/target-currency/INVALIDEUR");
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        CurrencyConvertResponse convertDto = mapper.readValue(content, CurrencyConvertResponse.class);
        assertFalse(convertDto.isSuccess());
        assertEquals(202,convertDto.getError().getCode());
    }

    @Test
    public void checkValidationVatNumber_whenValidVatCodeIsGiven() throws UnsupportedEncodingException, JsonProcessingException, URISyntaxException {
        VatLookupRequest vatLookupRequest = new VatLookupRequest();
        vatLookupRequest.setVatCode("LU26375245");
        MvcResult mvcResult = setUpForPostCall("/api/lookupVat", mapper.writeValueAsString(vatLookupRequest));
        assertEquals(200, mvcResult.getResponse().getStatus());
        String responseBody = mvcResult.getResponse().getContentAsString();
        VatLookupResponse lookupResponse = mapper.readValue(responseBody, VatLookupResponse.class);
        assertTrue(lookupResponse.isValid());
    }

    @Test
    public void checkVatNumberResult_whenValidVatCodeIsGiven() throws UnsupportedEncodingException, JsonProcessingException, URISyntaxException {
        VatLookupRequest vatLookupRequest = new VatLookupRequest();
        vatLookupRequest.setVatCode("CZ25123891");
        MvcResult mvcResult = setUpForPostCall("/api/lookupVat", mapper.writeValueAsString(vatLookupRequest));
        assertEquals(200, mvcResult.getResponse().getStatus());
        String responseBody = mvcResult.getResponse().getContentAsString();
        VatLookupResponse givenLookupResponse = mapper.readValue(responseBody, VatLookupResponse.class);
        assertTrue(givenLookupResponse.isValid());
        VatLookupResponse expectedVatLookupResponse = new VatLookupResponse();
        expectedVatLookupResponse.setValid(true);
        expectedVatLookupResponse.setCountryCode("CZ");
        expectedVatLookupResponse.setVatNumber("25123891");
        expectedVatLookupResponse.setBusinessName("K+K Hotel s.r.o.");
        assertEquals(expectedVatLookupResponse, givenLookupResponse);

    }

    @Test
    public void validateVatNumber_whenInvalidVatCodeIsGiven() throws UnsupportedEncodingException, JsonProcessingException, URISyntaxException {
        VatLookupRequest vatLookupRequest = new VatLookupRequest();
        vatLookupRequest.setVatCode("invalidCode");
        MvcResult mvcResult = setUpForPostCall("/api/lookupVat", mapper.writeValueAsString(vatLookupRequest));
        assertEquals(200, mvcResult.getResponse().getStatus());
        String responseBody = mvcResult.getResponse().getContentAsString();
        VatLookupResponse lookupResponse = mapper.readValue(responseBody, VatLookupResponse.class);
        assertFalse(lookupResponse.isValid());
    }

}
