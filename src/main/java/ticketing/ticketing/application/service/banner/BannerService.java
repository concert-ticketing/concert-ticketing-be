package ticketing.ticketing.application.service.banner;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Banner;
import ticketing.ticketing.domain.enums.BannerStatus;
import ticketing.ticketing.infrastructure.repository.banner.BannerRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerService {

    private final BannerRepository bannerRepository;

    @Value("${upload.path.banner}")
    private String bannerUploadPath; // 예: C:/upload/banner/

    @Transactional
    public Banner createBanner(String title, String description, MultipartFile image, BannerStatus status, Admin admin) throws IOException {
        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            imageUrl = storeImageFile(image);
        }

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
    public Optional<Banner> updateBanner(Long id, String title, String description, MultipartFile image, BannerStatus status) throws IOException {
        return bannerRepository.findById(id).map(banner -> {
            String imageUrl = banner.getImageUrl();

            if (image != null && !image.isEmpty()) {
                try {
                    imageUrl = storeImageFile(image);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 저장 실패", e);
                }
            }

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

    /**
     * 이미지 파일 저장 + 디렉토리 및 권한 체크
     */
    private String storeImageFile(MultipartFile file) throws IOException {
        File directory = new File(bannerUploadPath);

        // 1. 저장 경로 생성
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("업로드 디렉토리를 생성할 수 없습니다: " + bannerUploadPath);
            }
        }

        // 2. 권한 확인
        if (!directory.canWrite()) {
            throw new IOException("업로드 디렉토리에 쓰기 권한이 없습니다: " + bannerUploadPath);
        }

        // 3. 파일명 생성
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IOException("유효하지 않은 파일명입니다.");
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + extension;

        // 4. 실제 파일 객체 생성
        File savedFile = new File(directory, newFileName);

        // 안전장치: 상위 디렉토리 생성
        if (!savedFile.getParentFile().exists()) {
            savedFile.getParentFile().mkdirs();
        }

        // 5. 파일 저장
        file.transferTo(savedFile);

        // 6. 접근 가능한 URL 경로 반환
        return "/upload/banner/" + newFileName;
    }
}
