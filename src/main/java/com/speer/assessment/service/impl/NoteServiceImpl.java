package com.speer.assessment.service.impl;

import com.speer.assessment.dto.NoteDTO;
import com.speer.assessment.entity.Note;
import com.speer.assessment.entity.SharedNote;
import com.speer.assessment.entity.User;
import com.speer.assessment.repository.NoteRepository;
import com.speer.assessment.repository.SharedRepository;
import com.speer.assessment.repository.UserRepository;
import com.speer.assessment.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private SharedRepository sharedRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public List<Note> getAllNotesForUser(String username) {

       List<Note> userOwnedNotes = noteRepository.findByUsername(username);

       List<SharedNote> sharedNotes = sharedRepository.findAllBySharedWithUsernamesContaining(username);

       List<String> sharedNoteIds = sharedNotes.stream().map(SharedNote::getNoteId).collect(Collectors.toList());

       List<Note> userSharedNotes = noteRepository.findAllById(sharedNoteIds);

       List<Note> allUserNotes = new ArrayList<>(userOwnedNotes);
       allUserNotes.addAll(userSharedNotes);
        return allUserNotes;
    }

    @Override
    public Note createNoteForUser(String username, NoteDTO noteDTO) {
        Note note = new Note();
        note.setTitle(noteDTO.getTitle());
        note.setContent(noteDTO.getContent());
        note.setUsername(username);
        return noteRepository.save(note);
    }

    @Override
    public Note getNoteByIdForUser(String id, String username) {
        return noteRepository.findByIdAndUsername(id, username);
    }

    @Override
    public Note updateNote(String id, NoteDTO noteDTO, String username) throws Exception {
        Note existingNote = noteRepository.findByIdAndUsername(id, username);

        if (existingNote != null) {
            existingNote.setTitle(noteDTO.getTitle());
            existingNote.setContent(noteDTO.getContent());

            return noteRepository.save(existingNote);
        }
            throw new Exception("Note not exist");

    }

    @Override
    public boolean deleteNote(String id, String username) {
       Note note = noteRepository.findByIdAndUsername(id, username);
       if(note != null) {
           noteRepository.deleteById(id);
           return true;
       }
        return false;
    }

    @Override
    public boolean shareNoteWithUser(String noteId, String username, String anotherUsername) {
        Note noteToShare = noteRepository.findByIdAndUsername(noteId, username);
        Optional<User> user = userRepository.findByUsername(anotherUsername);
        if(noteToShare != null && user.isPresent()){
            SharedNote sharedNote = sharedRepository.findByNoteId(noteId)
                    .orElseGet(()-> new SharedNote(noteId));
            sharedNote.addSharedUser(anotherUsername);
            sharedRepository.save(sharedNote);
            return true;
        }
        return false;
    }

    @Override
    public List<Note> searchNotesByKeyword(String username, String keyword) {

//        List<SharedNote> sharedNotes = sharedRepository.findAllBySharedWithUsernamesContaining(username);
//        List<String> sharedNoteIds = sharedNotes.stream()
//                .map(SharedNote::getNoteId)
//                .collect(Collectors.toList());
        //TO DO: we can enhance search by adding seraching in shared notes as well


        return noteRepository.findByTextSearch(keyword);

    }


}
