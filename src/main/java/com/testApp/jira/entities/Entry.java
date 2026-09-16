package com.testApp.jira.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@Data
@Document(collection= "entry")
@NoArgsConstructor
public class Entry {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId entryId;

    @NonNull
    private String title;

    private String description;

    private String assignedTo;

    private EntryType type;

    private LocalDateTime date;

}
