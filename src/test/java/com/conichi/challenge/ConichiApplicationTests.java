package com.conichi.challenge;

import com.conichi.challenge.dto.CurrencyConvertDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ConichiApplication.class)
@WebAppConfiguration
class ConichiApplicationTests {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    private MvcResult setUp(String uri) {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = null;
        try {
            mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        } catch (Exception e) {
            LOGGER.error("Error in creating mvc request");
        }
        return mvcResult;
    }

    @Test
    public void getCurrestTime() {
        MvcResult mvcResult = setUp("/api/currentTime");
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

//    @Test
    public void convertCurrency() throws UnsupportedEncodingException, JsonProcessingException {
        MvcResult mvcResult = setUp("/api/currency/convert/source-currency/USD/target-currency/EUR/amount/100");
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        CurrencyConvertDto convertDto = mapper.readValue(content, CurrencyConvertDto.class);
        assertEquals(new BigDecimal("89.345500"), convertDto.getTargetCurrencyAmount());
    }

//    @Test
    public void lookupVatNumber() throws UnsupportedEncodingException, JsonProcessingException {
        MvcResult mvcResult = setUp("/api/lookupVat/LU26375245");
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        VatLookupResponse lookupResponse = mapper.readValue(content, VatLookupResponse.class);
//        assertTrue(lookupResponse.isValid());
    }

}
