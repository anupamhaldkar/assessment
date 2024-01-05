package com.speer.assessment.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document( collection = "shared_notes")
public class SharedNote {
    @Id
    private String id;
    private String noteId;
    private List<String> sharedWithUsernames;

    public SharedNote(String noteId) {
        this.noteId = noteId;
    }
    public void addSharedUser(String username) {
        if (sharedWithUsernames == null) {
            sharedWithUsernames = new ArrayList<>();
        }
        sharedWithUsernames.add(username);
    }

}
