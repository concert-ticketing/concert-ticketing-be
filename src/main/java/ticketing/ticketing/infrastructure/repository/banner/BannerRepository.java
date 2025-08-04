package ticketing.ticketing.infrastructure.repository.banner;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketing.ticketing.domain.entity.Banner;

public interface BannerRepository extends JpaRepository<Banner, Long> {
}

