package ticketing.ticketing.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByUserId(Long userId, Pageable pageable);
    Page<Review> findByConcert_TitleContaining(String concertTitle, Pageable pageable);
    List<Review> findByUserId(Long userId);
}
