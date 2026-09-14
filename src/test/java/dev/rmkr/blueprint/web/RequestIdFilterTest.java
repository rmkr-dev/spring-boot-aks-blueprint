package dev.rmkr.blueprint.web;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RequestIdFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void generatesRequestIdWhenAbsent() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/hello"))
                .andExpect(status().isOk())
                .andExpect(header().string(RequestIdFilter.HEADER, Matchers.not(Matchers.blankOrNullString())))
                .andReturn();
        String id = result.getResponse().getHeader(RequestIdFilter.HEADER);
        assertFalse(id.isBlank());
    }

    @Test
    void echoesIncomingRequestId() throws Exception {
        mockMvc.perform(get("/api/v1/hello").header(RequestIdFilter.HEADER, "client-corr-1"))
                .andExpect(status().isOk())
                .andExpect(header().string(RequestIdFilter.HEADER, "client-corr-1"));
    }

    @Test
    void trimsIncomingRequestId() throws Exception {
        mockMvc.perform(get("/api/v1/hello").header(RequestIdFilter.HEADER, "  trim-me  "))
                .andExpect(status().isOk())
                .andExpect(header().string(RequestIdFilter.HEADER, "trim-me"));
    }
}
