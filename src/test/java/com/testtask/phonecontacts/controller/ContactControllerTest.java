package com.testtask.phonecontacts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testtask.phonecontacts.service.ContactService;
import com.testtask.phonecontacts.util.TestContactUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContactControllerTest {

    @Autowired
    private MockMvc api;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ContactService contactService;

    @Test
    void notLoggedIn_shouldNotSeeSecuredEndpoints() throws Exception {
        api.perform(get("/contacts"))
                .andExpect(status().isUnauthorized());
        api.perform(post("/contacts"))
                .andExpect(status().isUnauthorized());
        api.perform(put("/contacts"))
                .andExpect(status().isUnauthorized());
        api.perform(delete("/contacts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void LoggedIn_shouldSeeSecuredEndpoints() throws Exception {
        when(contactService.addNewContactForUser(any(), any())).thenReturn(TestContactUtil.createModel());
        when(contactService.editContactForUser(any(), any())).thenReturn(TestContactUtil.createModel());
        when(contactService.findAllContactsForUser(any())).thenReturn(Collections.emptyList());
        doNothing().when(contactService).deleteContactForUser(any(), any());

        api.perform(get("/contacts"))
                .andExpect(status().isOk());
        api.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestContactUtil.createModel())))
                .andExpect(status().isCreated());
        api.perform(put("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestContactUtil.createModel())))
                .andExpect(status().isOk());
        api.perform(delete("/contacts")
                        .param("name", "Test"))
                .andExpect(status().isNoContent());
    }
}