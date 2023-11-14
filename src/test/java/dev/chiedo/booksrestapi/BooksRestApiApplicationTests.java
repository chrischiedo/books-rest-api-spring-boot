package dev.chiedo.booksrestapi;

import dev.chiedo.booksrestapi.model.Book;
import dev.chiedo.booksrestapi.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import net.minidev.json.JSONArray;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BooksRestApiApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

//	@Autowired
//	private MockMvc mockMvc;

//	@MockBean
//	private BookService bookService;

	@Test
	void contextLoads() {
	}


	@Test
	@DisplayName("GET /api/v1/books/id returns 200 OK and a Book JSON")
	void shouldReturn200OkAndABookWhenValidIdIsGiven() {
		String url = "http://localhost:" + port + "/api/v1/books/1";
		ResponseEntity<Book> response = restTemplate.getForEntity(url, Book.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); // Check for a 200 OK response status

		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getAuthor()).isEqualTo("Chinua Achebe");
	}

	@Test
	@DisplayName("GET /api/v1/books/2 returns 200 OK and a valid JSON")
	void shouldReturnA200OkAWhenABookIsPresent() {
		String url = "http://localhost:" + port + "/api/v1/books/2";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); // Check for a 200 OK response status

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");

		assertThat(id).isNotNull();
		assertThat(id).isEqualTo(2);

		String title = documentContext.read("$.title");
		assertThat(title).isEqualTo("The River And The Source");
	}

	@Test
	@DisplayName("GET /api/v1/books/{id} returns 404 Not Found when given a wrong id")
	void shouldReturnA404NotFoundWhenAnUnknownIdIsGiven() {
		String url = "http://localhost:" + port + "/api/v1/books/20";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
	}

	@Test
	@DisplayName("GET /api/v1/books returns 200 OK and a list of all books")
	void shouldReturnA200OkAndAllBooksWhenListIsRequested() {
		String url = "http://localhost:" + port + "/api/v1/books";

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		int bookCount = documentContext.read("$.length()");
		assertThat(bookCount).isEqualTo(4);

		JSONArray titles = documentContext.read("$..title");
		assertThat(titles).containsExactlyInAnyOrder(
				"Things Fall Apart",
				"The River And The Source",
				"Dreams From My Father",
				"Animal Farm"
		);
	}

	@Test
	@DisplayName("POST /api/v1/books returns 401 Unauthorized when the user has a username of 'user', a password of 'pass' and a role of 'ADMIN'")
	@WithMockUser(username = "user4", password = "pass4", authorities = "ROLE_ADMIN")
	void shouldReturn401UnauthorizedWhenUserHasInvalidCredentials() {
		String url = "http://localhost:" + port + "/api/v1/books";

		Book book = new Book(null, "Test Book", "Test Author", "Test Description", "123-45");
		ResponseEntity<Book> response = restTemplate.postForEntity(url, book, Book.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

//	@Test
//	@DisplayName("GET /api/v1/books/3 returns 200 OK and a Book JSON")
//	void shouldReturnABookGivenAValidId() throws Exception {
//		var mockBook = new Book(3L, "Test Book", "Test Author", "Test Description", "123-45");
//		when(bookService.findBookById(3L)).thenReturn(Optional.of(mockBook));
//		mockMvc.perform(get("/api/v1/books/3"))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.id").value(3))
//				.andExpect(jsonPath("$.title").value("Test Book"))
//				.andExpect(jsonPath("$.author").value("Test Author"))
//				.andExpect(jsonPath("$.description").value("Test Description"))
//				.andExpect(jsonPath("$.isbn").value("123-45"));
//	}
}
