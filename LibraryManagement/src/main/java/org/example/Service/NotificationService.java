package org.example.Service;

import org.example.Model.Book;
import org.example.Model.Wishlist;
import org.example.Repository.WishlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    @Autowired
    private WishlistRepository wishlistRepository;
    
    @Async
    public void notifyUsersBookAvailable(Book book) {
        try {
            List<Wishlist> wishlists = wishlistRepository.findByBookIdWithUser(book.getId());
            
            for (Wishlist wishlist : wishlists) {
                String message = String.format(
                    "Notification prepared for user_id: %d: Book [%s] is now available.",
                    wishlist.getUser().getId(),
                    book.getTitle()
                );
                logger.info(message);
            }
            
            logger.info("Processed {} notifications for book: {}", wishlists.size(), book.getTitle());
            
        } catch (Exception e) {
            logger.error("Error processing notifications for book {}: {}", book.getId(), e.getMessage(), e);
        }
    }
}