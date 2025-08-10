package ticketing.ticketing.application.service.faq;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.ticketing.application.dto.faqCreateRequestDto.FaqCreateRequestDto;
import ticketing.ticketing.application.dto.faqUpdateRequestDto.FaqUpdateRequestDto;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Faq;
import ticketing.ticketing.infrastructure.repository.faqRepository.FaqRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqService {

    private final FaqRepository faqRepository;

    // FAQ 등록
    @Transactional
    public Faq createFaq(FaqCreateRequestDto request, Admin admin) {
        Faq faq = Faq.create(
                request.getCategory(),
                request.getVisibility(),
                request.getQuestion(),
                request.getAnswer(),
                admin,
                LocalDateTime.now()
        );
        return faqRepository.save(faq);
    }

    // FAQ 수정
    @Transactional
    public Faq updateFaq(Long id, FaqUpdateRequestDto request) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FAQ not found"));

        faq.update(
                request.getCategory(),
                request.getVisibility(),
                request.getQuestion(),
                request.getAnswer()
        );
        return faq;
    }

    // FAQ 삭제
    @Transactional
    public void deleteFaq(Long id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FAQ not found"));
        faqRepository.delete(faq);
    }

    // 전체 FAQ 조회
    public List<Faq> getAllFaqs() {
        return faqRepository.findAll();
    }

    // 단건 조회
    public Faq getFaqById(Long id) {
        return faqRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FAQ not found"));
    }
}
