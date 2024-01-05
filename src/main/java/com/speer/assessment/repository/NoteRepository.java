package com.speer.assessment.repository;

import com.speer.assessment.entity.Note;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface NoteRepository extends MongoRepository<Note,String> {
    List<Note> findByUsername(String username);

    Note findByIdAndUsername(String id, String username);

    @Query("{ $text: {$search: ?0} }")
    List<Note> findByTextSearch(String keyword);
}
