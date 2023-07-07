package com.testtask.phonecontacts.model;


import com.testtask.phonecontacts.model.validation.PhoneNumber;
import com.testtask.phonecontacts.persistance.entity.Contact;
import com.testtask.phonecontacts.persistance.entity.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactModel {
    @NotEmpty
    @NotNull
    private String name;

    private Set<@Email String> emails;

    private Set<@PhoneNumber String> phones;

    public static ContactModel fromContact(Contact contact) {
        return new ContactModel(contact.getName(),
                contact.getEmails().stream()
                        .map(com.testtask.phonecontacts.persistance.entity.Email::getValue)
                        .collect(Collectors.toSet()),
                contact.getPhones().stream()
                        .map(Phone::getPhoneNumber)
                        .collect(Collectors.toSet()));
    }

    public Set<com.testtask.phonecontacts.persistance.entity.Email> getEmails() {
        return emails.stream()
                .map(com.testtask.phonecontacts.persistance.entity.Email::new)
                .collect(Collectors.toSet());
    }

    public Set<Phone> getPhones() {
        return phones.stream()
                .map(Phone::new)
                .collect(Collectors.toSet());
    }
}
