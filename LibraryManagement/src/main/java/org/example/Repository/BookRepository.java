package org.example.Repository;

import org.example.Model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    @Query("SELECT b FROM Book b WHERE b.isDeleted = false")
    Page<Book> findAllActive(Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE b.isDeleted = false AND b.id = :id")
    Optional<Book> findActiveById(@Param("id") Long id);
    
    @Query("SELECT b FROM Book b WHERE b.isDeleted = false AND " +
           "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
           "(:publishedYear IS NULL OR b.publishedYear = :publishedYear)")
    Page<Book> findActiveWithFilters(@Param("author") String author, 
                                   @Param("publishedYear") Integer publishedYear, 
                                   Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE b.isDeleted = false AND " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Book> searchActiveBooks(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Book b WHERE b.isbn = :isbn AND b.isDeleted = false")
    boolean existsByIsbnAndNotDeleted(@Param("isbn") String isbn);
    
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Book b WHERE b.isbn = :isbn AND b.id != :id AND b.isDeleted = false")
    boolean existsByIsbnAndNotDeletedAndIdNot(@Param("isbn") String isbn, @Param("id") Long id);
}