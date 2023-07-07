package com.testtask.phonecontacts.service;

import com.testtask.phonecontacts.model.ContactModel;
import com.testtask.phonecontacts.persistance.ContactRepository;
import com.testtask.phonecontacts.persistance.EmailRepository;
import com.testtask.phonecontacts.persistance.PhoneRepository;
import com.testtask.phonecontacts.persistance.UserRepository;
import com.testtask.phonecontacts.persistance.entity.Contact;
import com.testtask.phonecontacts.persistance.entity.Email;
import com.testtask.phonecontacts.persistance.entity.Phone;
import com.testtask.phonecontacts.persistance.entity.User;
import com.testtask.phonecontacts.service.exceptions.EntityExistsException;
import com.testtask.phonecontacts.service.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final EmailRepository emailRepository;

    @Transactional(readOnly = true)
    public Collection<ContactModel> findAllContactsForUser(String username) {
        Collection<Contact> contacts = contactRepository.findAllByUsername(username);
        return contacts.stream()
                .map(ContactModel::fromContact)
                .toList();
    }

    @Transactional
    public ContactModel addNewContactForUser(ContactModel contactModel, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        if (contactRepository.existsByName(contactModel.getName())) {
            throw new EntityExistsException("Contact already exists");
        }
        Set<Email> emails = contactModel.getEmails();
        Set<Phone> phones = contactModel.getPhones();

        Contact contact = new Contact(contactModel.getName(), user, emails, phones);
        emails.forEach(email -> email.setContact(contact));
        phones.forEach(phone -> phone.setContact(contact));
        return ContactModel.fromContact(contactRepository.save(contact));
    }

    @Transactional
    public ContactModel editContactForUser(ContactModel contactModel, String username) {
        Contact contact = contactRepository.findByUsernameAndName(username, contactModel.getName())
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));

        phoneRepository.removeAllByContact(contact);
        emailRepository.removeAllByContact(contact);

        contact.setPhones(contactModel.getPhones());
        contact.setEmails(contactModel.getEmails());

        contact.getPhones().forEach(phone -> phone.setContact(contact));
        contact.getEmails().forEach(email -> email.setContact(contact));
        return ContactModel.fromContact(contact);
    }

    @Transactional
    public void deleteContactForUser(String name, String username) {
        Contact contact = contactRepository.findByUsernameAndName(username, name)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));
        contactRepository.delete(contact);
    }
}
