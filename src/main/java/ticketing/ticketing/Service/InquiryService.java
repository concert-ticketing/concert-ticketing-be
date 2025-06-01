package ticketing.ticketing.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ticketing.ticketing.DTO.InquiryResponseDto;
import ticketing.ticketing.Repository.InquiryRepository;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public Page<InquiryResponseDto> getInquiries(Pageable pageable) {
        return inquiryRepository.findAll(pageable)
                .map(InquiryResponseDto::fromEntity);
    }
}
