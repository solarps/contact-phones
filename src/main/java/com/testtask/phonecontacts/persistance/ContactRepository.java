package com.testtask.phonecontacts.persistance;

import com.testtask.phonecontacts.persistance.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("""
            select distinct c from Contact c
             left join fetch c.emails e
             left join fetch c.phones p
             where c.user.username = :username""")
    Collection<Contact> findAllByUsername(String username);

    boolean existsByName(String name);

    @Query("""
            select distinct c from Contact c
             left join fetch c.emails e
             left join fetch c.phones p
             where c.name = :name and c.user.username = :username""")
    Optional<Contact> findByUsernameAndName(String username, String name);
}
