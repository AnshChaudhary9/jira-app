package com.testApp.jira.repositories;

import com.testApp.jira.entities.Entry;
import com.testApp.jira.entities.EntryType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends MongoRepository<Entry, ObjectId> {
    List<Entry> findBytype(EntryType type);
}
