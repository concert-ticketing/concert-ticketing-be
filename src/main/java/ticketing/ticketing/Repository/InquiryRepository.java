package ticketing.ticketing.Repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticketing.ticketing.DTO.InquiryResponseDto;
import ticketing.ticketing.domain.entity.Inquiry;
import ticketing.ticketing.domain.enums.InquiryStatus;

import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    Page<Inquiry> findByUserId(Long userId, Pageable pageable);

    Optional<Inquiry> findByIdAndUserId(Long id, Long userId);

    @NotNull
    Page<Inquiry> findAll(Pageable pageable);

    @Query("""
  SELECT new ticketing.ticketing.DTO.InquiryResponseDto(
    i.id,
    i.user.id,
    i.user.name,
    i.user.email,
    i.title,
    i.content,
    i.type,
    i.status,
    i.repliedAt,
    i.createdAt,
    i.updatedAt,
    i.deletedAt
  )
  FROM Inquiry i
  WHERE i.status = :status
""")
    Page<InquiryResponseDto> findAllByStatus(@Param("status") InquiryStatus status, Pageable pageable);
}
