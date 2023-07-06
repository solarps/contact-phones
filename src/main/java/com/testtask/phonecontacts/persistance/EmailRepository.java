package com.testtask.phonecontacts.persistance;

import com.testtask.phonecontacts.persistance.entity.Contact;
import com.testtask.phonecontacts.persistance.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {

    void removeAllByContact(Contact contact);
}
