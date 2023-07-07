package com.testtask.phonecontacts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testtask.phonecontacts.controller.api.ContactAPI;
import com.testtask.phonecontacts.model.ContactModel;
import com.testtask.phonecontacts.security.UserPrincipal;
import com.testtask.phonecontacts.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContactController implements ContactAPI {
    private final ContactService contactService;

    public Collection<ContactModel> getContacts(UserPrincipal userPrincipal) {
        return contactService.findAllContactsForUser(userPrincipal.getUsername());
    }

    public ContactModel createContact(ContactModel contactModel,
                                      UserPrincipal userPrincipal) {
        return contactService.addNewContactForUser(contactModel, userPrincipal.getUsername());
    }

    public ContactModel editContact(ContactModel contactModel,
                                    UserPrincipal userPrincipal) {
        return contactService.editContactForUser(contactModel, userPrincipal.getUsername());
    }

    public ResponseEntity<Void> deleteContact(String name, UserPrincipal userPrincipal) {
        contactService.deleteContactForUser(name, userPrincipal.getUsername());
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<byte[]> exportContacts(UserPrincipal userPrincipal) throws JsonProcessingException {
        Collection<ContactModel> allContactsForUser = contactService.findAllContactsForUser(userPrincipal.getUsername());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(allContactsForUser);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "contacts.json");

        return new ResponseEntity<>(json.getBytes(), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> importContacts(MultipartFile file, UserPrincipal userPrincipal) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Collection<ContactModel> contactModels;
        contactModels = mapper.readValue(file.getBytes(), new TypeReference<List<ContactModel>>() {
        });
        contactService.addAllContactsForUser(contactModels, userPrincipal.getUsername());

        return ResponseEntity.ok().build();
    }

}
