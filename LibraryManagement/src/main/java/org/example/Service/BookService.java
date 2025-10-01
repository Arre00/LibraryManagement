package org.example.Service;

import org.example.Model.Book;
import org.example.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Transactional
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    public Book createBook(Book book) {
        if (bookRepository.existsByIsbnAndNotDeleted(book.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        return bookRepository.save(book);
    }
    
    @Transactional(readOnly = true)
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAllActive(pageable);
    }
    
    @Transactional(readOnly = true)
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findActiveById(id);
    }
    
    @Transactional(readOnly = true)
    public Page<Book> getBooksWithFilters(String author, Integer publishedYear, Pageable pageable) {
        return bookRepository.findActiveWithFilters(author, publishedYear, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Book> searchBooks(String searchTerm, Pageable pageable) {
        return bookRepository.searchActiveBooks(searchTerm, pageable);
    }
    
    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = bookRepository.findActiveById(id)
            .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        
        if (!existingBook.getIsbn().equals(updatedBook.getIsbn()) && 
            bookRepository.existsByIsbnAndNotDeletedAndIdNot(updatedBook.getIsbn(), id)) {
            throw new IllegalArgumentException("Book with ISBN " + updatedBook.getIsbn() + " already exists");
        }
        
        Book.AvailabilityStatus previousStatus = existingBook.getAvailabilityStatus();
        
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setPublishedYear(updatedBook.getPublishedYear());
        existingBook.setAvailabilityStatus(updatedBook.getAvailabilityStatus());
        
        Book savedBook = bookRepository.save(existingBook);
        
        if (previousStatus == Book.AvailabilityStatus.BORROWED && 
            updatedBook.getAvailabilityStatus() == Book.AvailabilityStatus.AVAILABLE) {
            notificationService.notifyUsersBookAvailable(savedBook);
        }
        
        return savedBook;
    }
    
    public void deleteBook(Long id) {
        Book book = bookRepository.findActiveById(id)
            .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        
        book.softDelete();
        bookRepository.save(book);
    }
}