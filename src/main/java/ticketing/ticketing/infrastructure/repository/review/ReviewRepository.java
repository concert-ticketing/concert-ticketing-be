package ticketing.ticketing.infrastructure.repository.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ticketing.ticketing.application.dto.reviewDto.ReviewByConcertIdReadResponse;
import ticketing.ticketing.domain.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT new ticketing.ticketing.application.dto.reviewDto.ReviewByConcertIdReadResponse(r.id,r.user.id, r.user.email,r.rating,r.content,r.createdAt,r.updatedAt) " +
            " FROM Review r" +
            " WHERE r.concert.id  = :concertId " +
            "ORDER BY r.id DESC")
    List<ReviewByConcertIdReadResponse> getReviewByConcertId(Long concertId);


    Page<Review> findByUserId(Long user_id, Pageable pageable);
    Page<Review> findByConcert_TitleContaining(String concertTitle, Pageable pageable);
}
