package org.example.Repository;

import org.example.Model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    
    @Query("SELECT w FROM Wishlist w JOIN FETCH w.user WHERE w.book.id = :bookId")
    List<Wishlist> findByBookIdWithUser(@Param("bookId") Long bookId);
}