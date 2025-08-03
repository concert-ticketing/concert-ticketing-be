package ticketing.ticketing.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.ticketing.domain.entity.Report;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Review;
import ticketing.ticketing.DTO.ReportRequestDto;
import ticketing.ticketing.DTO.ReportResponseDto;
import ticketing.ticketing.Repository.ReportRepository;
import ticketing.ticketing.Repository.AdminRepository;
import ticketing.ticketing.infrastructure.repository.review.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final AdminRepository adminRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReportResponseDto createReport(ReportRequestDto dto) {
        Admin admin = adminRepository.findById(dto.getAdminId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid admin ID"));

        Review review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        Report report = Report.create(admin, review, dto.getReason());

        Report saved = reportRepository.save(report);
        return ReportResponseDto.from(saved);
    }
}
