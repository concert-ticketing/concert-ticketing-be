package ticketing.ticketing.infrastructure.repository.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketing.ticketing.domain.entity.Notice;
import ticketing.ticketing.domain.entity.NoticeImage;

import java.util.List;

@Repository
public interface NoticeImageRepository extends JpaRepository<NoticeImage, Long> {

    List<NoticeImage> findAllByNotice(Notice notice);

    void deleteAllByNotice(Notice notice);
}
