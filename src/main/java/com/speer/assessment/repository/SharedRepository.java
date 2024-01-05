package com.speer.assessment.repository;

import com.speer.assessment.entity.SharedNote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharedRepository extends MongoRepository<SharedNote,String> {
    Optional<SharedNote> findByNoteId(String noteId);

    List<SharedNote> findAllBySharedWithUsernamesContaining(String username);
}
