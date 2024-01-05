package com.speer.assessment.service;

import com.speer.assessment.dto.NoteDTO;
import com.speer.assessment.entity.Note;

import java.util.List;

public interface NoteService {
    public List<Note> getAllNotesForUser(String username);

    Note createNoteForUser(String username, NoteDTO noteDTO);

    Note getNoteByIdForUser(String id, String username);

    Note updateNote(String id, NoteDTO noteDTO, String username) throws Exception;

    boolean deleteNote(String id, String username);

    boolean shareNoteWithUser(String id, String username, String anotherUsername);

    List<Note> searchNotesByKeyword(String username, String q);
}
