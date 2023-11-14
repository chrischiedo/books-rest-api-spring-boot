package dev.chiedo.booksrestapi.controller;

import java.net.URI;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import dev.chiedo.booksrestapi.model.Book;
import dev.chiedo.booksrestapi.service.BookService;

@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Book Controller", description = "Controller for managing books")
public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

//    @Autowired
//    private BookService bookService;

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

//    @Operation(summary = "Returns a welcome message")
//    @GetMapping("/welcome")
//    public String greeting() {
//        return "Welcome to our Books REST API project!";
//    }

    @Operation(summary = "Returns a book given its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book with the given id",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "404", description = "Book with the particular id is not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<Book> findBookById(@Parameter(description = "id of book to be returned", required = true) @PathVariable("id") Long id) {
        Optional<Book> bookOptional = bookService.findBookById(id);

        if (bookOptional.isPresent()) {
            LOGGER.debug("Found book " + bookOptional);
            return ResponseEntity.ok(bookOptional.get());
        } else {
            LOGGER.debug("No book found with id " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Returns a book given its title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book with the given title",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "404", description = "Book with the particular title is not found",
                    content = @Content)})
    @GetMapping("/search/{title}")
    public ResponseEntity<Book> findBookByTitle(@Parameter(description = "title of book to be searched", required = true) @PathVariable String title) {
        Book book = bookService.findBookByTitle(title);

        if (book != null) {
            LOGGER.debug("Found book " + book);
            return ResponseEntity.ok(book);
        } else {
            LOGGER.debug("No book found with title " + title);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Returns all the existing books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "204", description = "No content")})
    @GetMapping
    public ResponseEntity<Iterable<Book>> getAllBooks() {
        // Iterable<Book> books = bookService.getAllBooks();
        var books = bookService.getAllBooks();

        LOGGER.debug("Total number of books " + books);
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Adds a new book")
    @ApiResponse(responseCode = "201", description = "The URI of the added book",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = URI.class))})
    @PostMapping
    private ResponseEntity<Void> addNewBook(@RequestBody(required = true) @Valid Book newBook,
    UriComponentsBuilder ucb) {
        // Book savedBook = bookService.saveNewBook(newBook);
        Book savedBook = bookService.saveNewBook(new Book(
            newBook.getId(),
            newBook.getTitle(),
            newBook.getAuthor(),
            newBook.getDescription(),
            newBook.getIsbn()
        ));

        URI locationOfNewBook = ucb
                .path("/books/{id}")
                .buildAndExpand(savedBook.getId())
                .toUri();

        LOGGER.debug("New book created with URI " + locationOfNewBook);

        return ResponseEntity.created(locationOfNewBook).build();
    }

    @Operation(summary = "Updates an existing book")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "404", description = "Book with that id is Not Found", content = @Content)
    @PutMapping("/{id}")
    private ResponseEntity<Void> updateBook(@Parameter(description = "id of book to be updated", required = true) @PathVariable("id") Long id, @RequestBody @Valid Book bookUpdate) {
        Optional<Book> bookOptional = bookService.findBookById(id);

        if (bookOptional.isPresent()) {
            Book updatedBook = new Book(
                bookOptional.get().getId(),
                bookUpdate.getTitle(),
                bookUpdate.getAuthor(),
                bookUpdate.getDescription(),
                bookUpdate.getIsbn()
            );
            bookService.saveNewBook(updatedBook);
            LOGGER.debug("Book updated with new values " + updatedBook);
            
            return ResponseEntity.noContent().build();
        }

        LOGGER.debug("No book found with id " + id);
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Deletes an existing book")
    @ApiResponse(responseCode = "204", description = "The book has been successfully deleted")
    @ApiResponse(responseCode = "404", description = "Book with that id is Not Found", content = @Content)
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteBook(@Parameter(description = "id of book to be deleted", required = true) @PathVariable("id") Long id) {
        Optional<Book> bookOptional = bookService.findBookById(id);

        if (bookOptional.isPresent()) {
            bookService.deleteBookById(id);
            LOGGER.debug("Book deleted with " + id);
            return ResponseEntity.noContent().build();
        }

        LOGGER.debug("No book found with id " + id);
        return ResponseEntity.notFound().build();
    }
}

