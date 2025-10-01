package org.example.Config;

import org.example.Model.AppUser;
import org.example.Model.Book;
import org.example.Model.Wishlist;
import org.example.Repository.AppUserRepository;
import org.example.Repository.BookRepository;
import org.example.Repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private AppUserRepository userRepository;
    
    @Autowired
    private WishlistRepository wishlistRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.count() == 0) {
            initializeData();
        }
    }
    
    private void initializeData() {
        Book book1 = new Book();
        book1.setTitle("The Great Gatsby");
        book1.setAuthor("F. Scott Fitzgerald");
        book1.setIsbn("9780743273565");
        book1.setPublishedYear(1925);
        book1.setAvailabilityStatus(Book.AvailabilityStatus.AVAILABLE);
        
        Book book2 = new Book();
        book2.setTitle("To Kill a Mockingbird");
        book2.setAuthor("Harper Lee");
        book2.setIsbn("9780061120084");
        book2.setPublishedYear(1960);
        book2.setAvailabilityStatus(Book.AvailabilityStatus.BORROWED);
        
        Book book3 = new Book();
        book3.setTitle("1984");
        book3.setAuthor("George Orwell");
        book3.setIsbn("9780451524935");
        book3.setPublishedYear(1949);
        book3.setAvailabilityStatus(Book.AvailabilityStatus.AVAILABLE);
        
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        
        AppUser user1 = new AppUser();
        user1.setUsername("john_doe");
        user1.setEmail("john@example.com");
        
        AppUser user2 = new AppUser();
        user2.setUsername("jane_smith");
        user2.setEmail("jane@example.com");
        
        userRepository.save(user1);
        userRepository.save(user2);
        
        Wishlist wishlist1 = new Wishlist();
        wishlist1.setUser(user1);
        wishlist1.setBook(book2);
        
        Wishlist wishlist2 = new Wishlist();
        wishlist2.setUser(user2);
        wishlist2.setBook(book2);
        
        wishlistRepository.save(wishlist1);
        wishlistRepository.save(wishlist2);
        
        System.out.println("Sample data initialized successfully!");
    }
}