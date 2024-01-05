package com.speer.assessment.controller;

import com.speer.assessment.dto.NoteDTO;
import com.speer.assessment.entity.Note;
import com.speer.assessment.service.NoteService;
import com.speer.assessment.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {


    @Autowired
    private NoteService noteService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<Object> getAllNotes(HttpServletRequest httpServletRequest) {
        String username = jwtUtil.extractUsername(httpServletRequest.getHeader("Authorization").substring(7));
        List<Note> userNotes = noteService.getAllNotesForUser(username);
        return ResponseEntity.ok(userNotes);
    }

    @PostMapping
    public ResponseEntity<Note> createNote(HttpServletRequest httpServletRequest, @RequestBody NoteDTO noteDTO) {
        String username = jwtUtil.extractUsername(httpServletRequest.getHeader("Authorization").substring(7));
        Note createdNote = noteService.createNoteForUser(username, noteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable String id, HttpServletRequest httpServletRequest) {
        String username = jwtUtil.extractUsername(httpServletRequest.getHeader("Authorization").substring(7));
        Note note = noteService.getNoteByIdForUser(id, username);
        if (note != null) {
            NoteDTO noteDTO = new NoteDTO();
            noteDTO.setTitle(note.getTitle());
            noteDTO.setContent(note.getContent());
            return ResponseEntity.status(HttpStatus.OK).body(noteDTO);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new NoteDTO("No title", "No notes exist"));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable String id, HttpServletRequest httpServletRequest, @RequestBody NoteDTO noteDTO) throws Exception {
        String username = jwtUtil.extractUsername(httpServletRequest.getHeader("Authorization").substring(7));
        Note note = noteService.updateNote(id, noteDTO, username);
        if (note != null) {
            NoteDTO updatedNoteDTO = new NoteDTO();
            noteDTO.setTitle(note.getTitle());
            noteDTO.setContent(note.getContent());
            return ResponseEntity.status(HttpStatus.OK).body(noteDTO);
        }
        throw new Exception("Not found");
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable String id, HttpServletRequest httpServletRequest) throws Exception {
        String username = jwtUtil.extractUsername(httpServletRequest.getHeader("Authorization").substring(7));
        boolean deleteNote = noteService.deleteNote(id, username);
        if (deleteNote) {
            return ResponseEntity.status(HttpStatus.OK).body("deleted the note");
        }
        throw new Exception("Note not found or error occurred");
    }

    @PostMapping(value = "/{id}/share")
    public ResponseEntity<String> shareNote(@PathVariable String id, @RequestParam String anotherUsername, HttpServletRequest httpServletRequest){
        String username = jwtUtil.extractUsername(httpServletRequest.getHeader("Authorization").substring(7));
        boolean shared= noteService.shareNoteWithUser(id, username, anotherUsername);
        if(shared){
            return ResponseEntity.status(HttpStatus.OK).body("Shared to "+ anotherUsername);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not Shared! - user/note not exist");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Note>> searchNotesByKeyword(@RequestParam String q, HttpServletRequest httpServletRequest){
        String username = jwtUtil.extractUsername(httpServletRequest.getHeader("Authorization").substring(7));
        List<Note> searchedNotes = noteService.searchNotesByKeyword(username, q);
        return ResponseEntity.status(HttpStatus.OK).body(searchedNotes);
    }
    
}
