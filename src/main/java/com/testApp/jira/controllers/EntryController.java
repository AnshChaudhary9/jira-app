package com.testApp.jira.controllers;

import com.testApp.jira.entities.Entry;
import com.testApp.jira.entities.EntryType;
import com.testApp.jira.entities.User;
import com.testApp.jira.services.EntryService;
import com.testApp.jira.services.UserService;
import org.bson.types.ObjectId;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entry")
public class EntryController {

    @Autowired
    private EntryService entryService;

    @Autowired
    private UserService userService;

    @PostMapping("/bug")
    @PreAuthorize("hasRole('TESTER')")
    public ResponseEntity<Entry> createBug(@RequestBody Entry myEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String fullName = authentication.getName();
            entryService.saveBug(myEntry,fullName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/story")
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<Entry> createStory(@RequestBody Entry myEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String fullName = authentication.getName();
            entryService.saveStory(myEntry,fullName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> getAllEntries() {
        List<Entry> entries = entryService.getAllEntries();
        if (entries == null || entries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entries,HttpStatus.OK);
    }

    @GetMapping("/bug")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> getAllBugs() {
        List<Entry> entries = entryService.findEntryByType(EntryType.BUG);
        if (entries == null || entries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entries,HttpStatus.OK);
    }

    @GetMapping("/story")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> getAllStories() {
        List<Entry> entries = entryService.findEntryByType(EntryType.STORY);
        if (entries == null || entries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entries,HttpStatus.OK);
    }

    @GetMapping("/user-entry/id/{myId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> getAllEntriesOfUser(@PathVariable ObjectId myId){
        User user = userService.findById(myId);
        List<Entry> allEntries = user.getEntryList();
        if (allEntries!=null && !allEntries.isEmpty()){
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(allEntries, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user-entry")
    public ResponseEntity<?> getEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String fullName = authentication.getName();
        User user = userService.findByFullName(fullName);
        List<Entry> allEntries = user.getEntryList();
        if (allEntries!=null && !allEntries.isEmpty()){
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(allEntries, HttpStatus.NOT_FOUND);
    }

    @GetMapping("id/{myid}")
    public  ResponseEntity<Entry> getEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String fullName = authentication.getName();
        User user = userService.findByFullName(fullName);
        List<Entry> collect = user.getEntryList().stream().filter(x -> x.getEntryId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()) {
            Optional<Entry> entry = entryService.getEntryById(myId);
            if (entry.isPresent()) {
                return new ResponseEntity<>(entry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId myId,@RequestBody Entry newEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String fullName = authentication.getName();
        User user = userService.findByFullName(fullName);
        List<Entry> collect = user.getEntryList().stream().filter(x -> x.getEntryId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<Entry> entry = entryService.getEntryById(myId);
            if (entry.isPresent()){
                Entry oldEntry = entry.get();
                oldEntry.setTitle(newEntry.getTitle());
                oldEntry.setDescription(newEntry.getDescription()!=null && !newEntry.getDescription().isEmpty() ? newEntry.getDescription() : oldEntry.getDescription());
                entryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("id/{myid}")
    public  ResponseEntity<Entry> deleteEntryById(@PathVariable ObjectId myid){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String fullName = authentication.getName();
            boolean removed = entryService.deleteEntryById(myid,fullName);
            if (removed){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    }
}
