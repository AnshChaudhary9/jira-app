package com.testApp.jira.services;

import com.testApp.jira.entities.Entry;
import com.testApp.jira.entities.EntryType;
import com.testApp.jira.entities.User;
import com.testApp.jira.repositories.EntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class EntryService {

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveBug(Entry myEntry, String fullName){
        try {
            User user = userService.findByFullName(fullName);
            myEntry.setDate(LocalDateTime.now());
            myEntry.setType(EntryType.BUG);
            myEntry.setAssignedTo(fullName);
            Entry saveEntry = entryRepository.save(myEntry);
            user.getEntryList().add(saveEntry);
            userService.saveUser(user);
        }catch (Exception e){
            throw new RuntimeException("An error occurred while saving the Bug",e);
        }
    }

    @Transactional
    public void saveStory(Entry myEntry, String fullName){
        try {
            User user = userService.findByFullName(fullName);
            myEntry.setDate(LocalDateTime.now());
            myEntry.setType(EntryType.STORY);
            myEntry.setAssignedTo(fullName);
            Entry saveEntry = entryRepository.save(myEntry);
            user.getEntryList().add(saveEntry);
            userService.saveUser(user);
        }catch (Exception e){
            throw new RuntimeException("An error occurred while saving the Story",e);
        }
    }

    public void saveEntry(Entry myEntry){
        Entry saveEntry = entryRepository.save(myEntry);
    }

    public List<Entry> getAllEntries(){
        return entryRepository.findAll();
    }

    public Optional<Entry> getEntryById(ObjectId id){
        return entryRepository.findById(id);
    }

    @Transactional
    public boolean deleteEntryById(ObjectId id,String fullName){
        boolean removed = false;
        try {
            User user = userService.findByFullName(fullName);
            removed = user.getEntryList().removeIf(x -> x.getEntryId().equals(id));
            if (removed){
                userService.saveUser(user);
                entryRepository.deleteById(id);
            }
        }catch (Exception e){
            throw new RuntimeException("An error occurred while deleting the entry",e);
        }
        return removed;
    }

    public List<Entry> findEntryByType(EntryType type){
        return entryRepository.findBytype(type);
    }
}

