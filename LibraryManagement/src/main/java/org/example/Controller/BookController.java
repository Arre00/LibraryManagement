package org.example.Controller;

import org.example.Model.Book;
import org.example.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        try {
            Book createdBook = bookService.createBook(book);
            return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer publishedYear) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Book> books;
        if (author != null || publishedYear != null) {
            books = bookService.getBooksWithFilters(author, publishedYear, pageable);
        } else {
            books = bookService.getAllBooks(pageable);
        }
        
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                  .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Book>> searchBooks(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Book> books = bookService.searchBooks(q, pageable);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        try {
            Book updatedBook = bookService.updateBook(id, book);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}