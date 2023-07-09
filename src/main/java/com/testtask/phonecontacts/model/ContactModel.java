package com.testtask.phonecontacts.model;


import com.testtask.phonecontacts.model.validation.PhoneNumber;
import com.testtask.phonecontacts.persistance.entity.Contact;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

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
                contact.getEmails(),
                contact.getPhones());
    }
}
