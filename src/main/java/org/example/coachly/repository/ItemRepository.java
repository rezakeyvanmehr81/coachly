package org.example.coachly.repository;

import org.example.coachly.model.Item;
import org.example.coachly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner(User owner);

    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategoryContainingIgnoreCase(
            String name, String description, String category);

    List<Item> findAllByOrderByCreatedAtDesc();

    @Query("SELECT i FROM Item i WHERE " +
           "(:query IS NULL OR :query = '' OR LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "   OR LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "   OR LOWER(i.category) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND (:category IS NULL OR :category = '' OR LOWER(i.category) = LOWER(:category)) " +
           "AND (:maxPrice IS NULL OR i.pricePerDay <= :maxPrice) " +
           "ORDER BY i.createdAt DESC")
    List<Item> findWithFilters(@Param("query") String query,
                               @Param("category") String category,
                               @Param("maxPrice") Double maxPrice);
}

