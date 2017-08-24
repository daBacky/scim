package ch.fhnw.scim.common.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ch.fhnw.scim.common.model.System;

public interface SystemRepo extends CrudRepository<System, Long> {

    List<System> findAll();

    System findById(int id);

    System findByName(String name);

    System findByDescription(String description);

    void delete(System entity);

    System save(System entity);
}
