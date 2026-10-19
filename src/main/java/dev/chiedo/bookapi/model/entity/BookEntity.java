package dev.chiedo.bookapi.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Book entity")
public class BookEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "book_id")
    private Long bookId;

    @NotBlank @Size(max = 50) @Column(nullable = false, length = 50)
    private String title;

    @NotBlank @Size(max = 50) @Column(nullable = false, length = 50)
    private String author;

    @NotBlank @Column(nullable = false, length = 1000)
    private String description;

    @NotBlank @Column(nullable = false, length = 50)
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    public Long getBookId() { return bookId; }

    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getIsbn() { return isbn; }

    public void setIsbn(String isbn) { this.isbn = isbn; }

    public UserEntity getOwner() { return owner; }

    public void setOwner(UserEntity owner) { this.owner = owner; }
}
