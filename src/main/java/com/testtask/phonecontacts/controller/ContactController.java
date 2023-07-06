package com.testtask.phonecontacts.controller;

import com.testtask.phonecontacts.model.ContactModel;
import com.testtask.phonecontacts.security.UserPrincipal;
import com.testtask.phonecontacts.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ContactModel> getContacts(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return contactService.findAllContactsForUser(userPrincipal.getUsername());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactModel createContact(@Valid @RequestBody ContactModel contactModel,
                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return contactService.addNewContactForUser(contactModel, userPrincipal.getUsername());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ContactModel editContact(@Valid @RequestBody ContactModel contactModel,
                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return contactService.editContactForUser(contactModel, userPrincipal.getUsername());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteContact(@RequestParam String name, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        contactService.deleteContactForUser(name, userPrincipal.getUsername());
        return ResponseEntity.noContent().build();
    }
}
