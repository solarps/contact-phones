package com.testtask.phonecontacts.persistance;

import com.testtask.phonecontacts.persistance.entity.Contact;
import com.testtask.phonecontacts.persistance.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    void removeAllByContact(Contact contact);
}
