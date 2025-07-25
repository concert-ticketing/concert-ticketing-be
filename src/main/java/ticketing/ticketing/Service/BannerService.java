package ticketing.ticketing.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Banner;
import ticketing.ticketing.domain.enums.BannerStatus;
import ticketing.ticketing.Repository.BannerRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerService {

    private final BannerRepository bannerRepository;

    @Transactional
    public Banner createBanner(String title, String description, String imageUrl, BannerStatus status, Admin admin) {
        Banner banner = Banner.create(title, description, imageUrl, status, admin);
        return bannerRepository.save(banner);
    }

    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    public Optional<Banner> getBanner(Long id) {
        return bannerRepository.findById(id);
    }

    @Transactional
    public Optional<Banner> updateBanner(Long id, String title, String description, String imageUrl, BannerStatus status) {
        return bannerRepository.findById(id).map(banner -> {
            banner.update(title, description, imageUrl, status);
            return banner;
        });
    }

    @Transactional
    public boolean deleteBanner(Long id) {
        if (bannerRepository.existsById(id)) {
            bannerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

