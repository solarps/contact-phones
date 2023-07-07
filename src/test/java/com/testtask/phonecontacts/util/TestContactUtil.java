package com.testtask.phonecontacts.util;

import com.testtask.phonecontacts.model.ContactModel;
import com.testtask.phonecontacts.persistance.entity.Contact;
import com.testtask.phonecontacts.persistance.entity.User;

import java.util.Collections;
import java.util.Set;

public class TestContactUtil {
    private static final Long ID = 1L;
    private static final String NAME = "Andrii";
    private static final Set<String> SET = Collections.emptySet();

    public static ContactModel createModel() {
        return ContactModel.builder().name(NAME).emails(SET).phones(SET).build();
    }

    public static Contact createContact() {
        return new Contact(NAME,new User(),Collections.emptySet(),Collections.emptySet());
    }
}
