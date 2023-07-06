package com.testtask.phonecontacts.controller;

import com.testtask.phonecontacts.controller.api.ContactAPI;
import com.testtask.phonecontacts.model.ContactModel;
import com.testtask.phonecontacts.security.UserPrincipal;
import com.testtask.phonecontacts.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class ContactController implements ContactAPI {
    private final ContactService contactService;

    public Collection<ContactModel> getContacts(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return contactService.findAllContactsForUser(userPrincipal.getUsername());
    }

    public ContactModel createContact(@Valid @RequestBody ContactModel contactModel,
                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return contactService.addNewContactForUser(contactModel, userPrincipal.getUsername());
    }

    public ContactModel editContact(@Valid @RequestBody ContactModel contactModel,
                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return contactService.editContactForUser(contactModel, userPrincipal.getUsername());
    }

    public ResponseEntity<Void> deleteContact(@RequestParam String name, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        contactService.deleteContactForUser(name, userPrincipal.getUsername());
        return ResponseEntity.noContent().build();
    }
}
