package com.speer.assessment;

import com.speer.assessment.controller.NoteController;
import com.speer.assessment.dto.NoteDTO;
import com.speer.assessment.entity.Note;
import com.speer.assessment.repository.NoteRepository;
import com.speer.assessment.service.NoteService;
import com.speer.assessment.service.UserService;
import com.speer.assessment.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AssessmentApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;

	@TestConfiguration
	public static class TestConfig {
		@Bean
		@Primary
		public String tokenMock() {
			JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);

			when(jwtUtil.extractUsername(Mockito.contains(TOKEN))).thenReturn("test");
			return "test";
		}
	}

	public static String TOKEN = "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzA0NDgzMDQ4LCJleHAiOjE3MDQ0ODY2NDh9.fJdniMCK-xYkiTXTpkPEv94oX4cYPhkMaR9IzZS4gE4";
	@Test
	public void testGetAllNotesAuthFail() throws Exception {


		mockMvc.perform(get("/api/notes"))
				.andExpect(status().is5xxServerError())
				.andDo(print())
				.andReturn();


	}

	@InjectMocks
	private NoteController notesController;

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private NoteService noteService;

	@Mock
	private HttpServletRequest httpServletRequest;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAllNotes() {

		when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");


		when(jwtUtil.extractUsername("token")).thenReturn("testUser");


		List<Note> mockedNotes = new ArrayList<>();
		when(noteService.getAllNotesForUser("testUser")).thenReturn(mockedNotes);

		ResponseEntity<Object> response = notesController.getAllNotes(httpServletRequest);


		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockedNotes, response.getBody());

		verify(jwtUtil, times(1)).extractUsername("token");
		verify(noteService, times(1)).getAllNotesForUser("testUser");
	}

	@Test
	public void testCreateNote() {
		when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");

		when(jwtUtil.extractUsername("token")).thenReturn("testUser");

		NoteDTO noteDTO = new NoteDTO();
		Note mockedCreatedNote = new Note();
		when(noteService.createNoteForUser("testUser", noteDTO)).thenReturn(mockedCreatedNote);

		ResponseEntity<Note> response = notesController.createNote(httpServletRequest, noteDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(mockedCreatedNote, response.getBody());

		verify(jwtUtil, times(1)).extractUsername("token");
		verify(noteService, times(1)).createNoteForUser("testUser", noteDTO);
	}

	@Test
	public void testGetNoteById() {
		when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");

		when(jwtUtil.extractUsername("token")).thenReturn("testUser");

		String noteId = "2345";
		Note mockedNote = new Note();
		when(noteService.getNoteByIdForUser(noteId, "testUser")).thenReturn(mockedNote);

		ResponseEntity<NoteDTO> response = notesController.getNoteById(noteId, httpServletRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(jwtUtil, times(1)).extractUsername("token");
		verify(noteService, times(1)).getNoteByIdForUser(noteId, "testUser");
	}

	@Test
	public void testUpdateNote() throws Exception {
		when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");

		when(jwtUtil.extractUsername("token")).thenReturn("testUser");

		String noteId = "1234";
		NoteDTO updatedNoteDTO = new NoteDTO();
		Note mockedUpdatedNote = new Note();
		when(noteService.updateNote(noteId, updatedNoteDTO, "testUser")).thenReturn(mockedUpdatedNote);

		ResponseEntity<NoteDTO> response = notesController.updateNote(noteId, httpServletRequest, updatedNoteDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(jwtUtil, times(1)).extractUsername("token");
		verify(noteService, times(1)).updateNote(noteId, updatedNoteDTO, "testUser");
	}

	@Test
	public void testDeleteNote() throws Exception {
		when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");

		when(jwtUtil.extractUsername("token")).thenReturn("testUser");

		String noteId = "12345";
		when(noteService.deleteNote(noteId, "testUser")).thenReturn(true);

		ResponseEntity<String> response = notesController.deleteNote(noteId, httpServletRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("deleted the note", response.getBody());

		verify(jwtUtil, times(1)).extractUsername("token");
		verify(noteService, times(1)).deleteNote(noteId, "testUser");
	}

	@Test
	public void testShareNote() {
		when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");

		when(jwtUtil.extractUsername("token")).thenReturn("testUser");

		String noteId = "12345";
		String anotherUsername = "another_test_user";
		when(noteService.shareNoteWithUser(noteId, "testUser", anotherUsername)).thenReturn(true);

		ResponseEntity<String> response = notesController.shareNote(noteId, anotherUsername, httpServletRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Shared to " + anotherUsername, response.getBody());

		verify(jwtUtil, times(1)).extractUsername("token");
		verify(noteService, times(1)).shareNoteWithUser(noteId, "testUser", anotherUsername);
	}

	@Test
	public void testSearchNotesByKeyword() {
		when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");

		when(jwtUtil.extractUsername("token")).thenReturn("testUser");

		String keyword = "content";
		List<Note> searchedNotes = new ArrayList<>();
		when(noteService.searchNotesByKeyword("testUser", keyword)).thenReturn(searchedNotes);

		ResponseEntity<List<Note>> response = notesController.searchNotesByKeyword(keyword, httpServletRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(searchedNotes, response.getBody());

		verify(jwtUtil, times(1)).extractUsername("token");
		verify(noteService, times(1)).searchNotesByKeyword("testUser", keyword);
	}

}
