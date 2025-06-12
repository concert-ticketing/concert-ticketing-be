package ticketing.ticketing.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ticketing.ticketing.DTO.InquiryResponseDto;
import ticketing.ticketing.Repository.InquiryRepository;
import ticketing.ticketing.domain.entity.Inquiry;
import ticketing.ticketing.exception.InquiryNotFoundException; // ✅ 사용자 정의 예외 import

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    // 전체 문의 목록 조회 (페이지네이션)
    public Page<InquiryResponseDto> getInquiries(Pageable pageable) {
        return inquiryRepository.findAll(pageable)
                .map(InquiryResponseDto::fromEntity);
    }

    // ✅ 단건 상세 조회
    public InquiryResponseDto getInquiryById(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new InquiryNotFoundException("해당 ID의 문의가 존재하지 않습니다."));
        return InquiryResponseDto.fromEntity(inquiry);
    }
}
