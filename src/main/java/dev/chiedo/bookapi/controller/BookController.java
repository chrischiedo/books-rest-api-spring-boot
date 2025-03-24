package dev.chiedo.bookapi.controller;

import dev.chiedo.bookapi.model.dto.BookDto;
import dev.chiedo.bookapi.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Optional;
import java.net.URI;


@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    // constructor injection
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Returns all existing books")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get all books",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No books found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{bookId}")
    @Operation(summary = "Returns a book given the book Id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get Book by bookId",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No Book found for the bookId provided",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<BookDto> getBookById(@PathVariable("bookId") long bookId) {
        Optional<BookDto> optionalBook = bookService.findById(bookId);

        if (optionalBook.isPresent()) {
            LOGGER.info("Found book {}", optionalBook);
            return ResponseEntity.ok(optionalBook.get());
        } else {
            LOGGER.debug("No book found with id {}", bookId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search/{title}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Returns a book given its title")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found the book with the given title",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Book with the particular title is not found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<BookDto> getBookByTitle(@Parameter(description = "title of book to be searched", required = true) @PathVariable String title) {
        BookDto book = bookService.findByTitle(title);

        if (book != null) {
            LOGGER.info("Book with title {} found", title);
            return ResponseEntity.ok(book);
        } else {
            LOGGER.debug("No book found with title {}", title);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Add a new book")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Book created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Book already exists with bookId",
                            content = @Content(mediaType = "application/json")),
            }
    )
    public ResponseEntity<Void> createBook(@RequestBody @Valid BookDto bookDto) {
        bookService.save(bookDto);

        URI bookUrl = URI.create("/api/v1/books/" + bookDto.getBookId());

        return ResponseEntity.created(bookUrl).build();
    }

    @PutMapping("/{bookId}")
    @Operation(summary = "Updates an existing book")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Book successfully updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No Book found for bookId provided",
                            content = @Content(mediaType = "application/json")),
            }
    )
    public ResponseEntity<Void> updateBook(
            @PathVariable("bookId") long bookId,
            @RequestBody @Valid BookDto bookDto) {

        Optional<BookDto> optionalBook = bookService.findById(bookId);

        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        bookService.update(bookId, bookDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bookId}")
    @Operation(summary = "Deletes an existing book by Id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Book successfully deleted",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No Book found for bookId provided",
                            content = @Content(mediaType = "application/json")),
            }
    )
    public ResponseEntity<Void> deleteBook(@PathVariable("bookId") long bookId) {
        Optional<BookDto> optionalBook = bookService.findById(bookId);

        if (optionalBook.isEmpty()) {
            LOGGER.debug("Book with id {} does not exist", bookId);
            return ResponseEntity.notFound().build();
        }

        bookService.delete(optionalBook.get());

        LOGGER.info("Book deleted with id {}", bookId);

        return ResponseEntity.noContent().build();
    }
}
