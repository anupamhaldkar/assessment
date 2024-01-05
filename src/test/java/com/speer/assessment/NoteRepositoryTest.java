package com.speer.assessment;

import com.speer.assessment.entity.Note;
import com.speer.assessment.repository.NoteRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Test
    public void testFindByTextSearch() {
        // Create and save test notes
        Note note1 = new Note("1234", "Title 1", "Content 1", "user1");
        Note note2 = new Note("4567", "Title 2", "Content 2", "user2");
        noteRepository.saveAll(Arrays.asList(note1, note2));

        //this is actual call not the mock one.
        List<Note> searchResults = noteRepository.findByTextSearch("content");

        Assertions.assertFalse(searchResults.isEmpty());

    }
}
