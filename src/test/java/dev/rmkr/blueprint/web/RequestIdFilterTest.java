package dev.rmkr.blueprint.web;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        UUID.fromString(id);
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

    @Test
    void emptyIncomingHeaderGeneratesUuid() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/hello").header(RequestIdFilter.HEADER, ""))
                .andExpect(status().isOk())
                .andExpect(header().string(RequestIdFilter.HEADER, Matchers.not(Matchers.blankOrNullString())))
                .andReturn();
        UUID.fromString(result.getResponse().getHeader(RequestIdFilter.HEADER));
    }

    @Test
    void whitespaceOnlyIncomingHeaderGeneratesUuid() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/hello").header(RequestIdFilter.HEADER, "   "))
                .andExpect(status().isOk())
                .andExpect(header().string(RequestIdFilter.HEADER, Matchers.not(Matchers.blankOrNullString())))
                .andReturn();
        String id = result.getResponse().getHeader(RequestIdFilter.HEADER);
        assertNotEquals("   ", id);
        UUID.fromString(id);
    }

    @Test
    void generatedIdsDifferAcrossRequests() throws Exception {
        String first = mockMvc.perform(get("/api/v1/hello"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(RequestIdFilter.HEADER);
        String second = mockMvc.perform(get("/api/v1/hello"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(RequestIdFilter.HEADER);
        assertNotNull(first);
        assertNotNull(second);
        assertNotEquals(first, second);
    }

    @Test
    void validationErrorIncludesRequestId() throws Exception {
        mockMvc.perform(get("/api/v1/hello")
                        .param("name", "x".repeat(65))
                        .header(RequestIdFilter.HEADER, "err-400"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(RequestIdFilter.HEADER, "err-400"));
    }

    @Test
    void notFoundIncludesGeneratedRequestId() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/missing"))
                .andExpect(status().isNotFound())
                .andExpect(header().string(RequestIdFilter.HEADER, Matchers.not(Matchers.blankOrNullString())))
                .andReturn();
        UUID.fromString(result.getResponse().getHeader(RequestIdFilter.HEADER));
    }

    @Test
    void prometheusIncludesEchoedRequestId() throws Exception {
        mockMvc.perform(get("/actuator/prometheus").header(RequestIdFilter.HEADER, "prom-1"))
                .andExpect(status().isOk())
                .andExpect(header().string(RequestIdFilter.HEADER, "prom-1"));
    }
}
