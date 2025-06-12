package ticketing.ticketing.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ticketing.ticketing.application.dto.concertDto.ConcertMainPageInformationReadResponse;
import ticketing.ticketing.application.dto.imagesDto.ImagesReadResponse;
import ticketing.ticketing.domain.entity.Images;

import java.util.List;

public interface ImagesRepository extends JpaRepository<Images,Long> {

    @Query("SELECT new ticketing.ticketing.application.dto.imagesDto.ImagesReadResponse(" +
            "i.image, i.concert.id) " +
            "FROM Images i " +
            "WHERE i.concert.id IN :concertIds AND i.imagesRole = ticketing.ticketing.domain.enums.ImagesRole.THUMBNAIL")
    List<ImagesReadResponse> findByThumbailImagesBeforeList(@Param("concertIds") List<Long> concertIds);
}
