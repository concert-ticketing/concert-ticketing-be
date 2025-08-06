package ticketing.ticketing.presentation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.ticketing.application.dto.faqCreateRequestDto.FaqCreateRequestDto;
import ticketing.ticketing.application.dto.faqResponseDto.FaqResponseDto;
import ticketing.ticketing.application.dto.faqUpdateRequestDto.FaqUpdateRequestDto;
import ticketing.ticketing.application.service.faq.FaqService;
import ticketing.ticketing.domain.entity.Admin;
import ticketing.ticketing.domain.entity.Faq;
import ticketing.ticketing.infrastructure.security.UserContext;
import ticketing.ticketing.infrastructure.repository.admin.AdminRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faqs")
public class FaqController {

    private final FaqService faqService;
    private final AdminRepository adminRepository;
    private final UserContext userContext;

    @GetMapping
    public ResponseEntity<List<FaqResponseDto>> getAllFaqs() {
        List<Faq> faqs = faqService.getAllFaqs();
        List<FaqResponseDto> response = faqs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FaqResponseDto> getFaq(@PathVariable Long id) {
        try {
            Faq faq = faqService.getFaqById(id);
            return ResponseEntity.ok(toDto(faq));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<FaqResponseDto> createFaq(@RequestBody FaqCreateRequestDto request) {
        Long adminId = userContext.getCurrentUserId();
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("인증된 관리자를 찾을 수 없습니다."));

        Faq faq = faqService.createFaq(request, admin);
        return ResponseEntity.ok(toDto(faq));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FaqResponseDto> updateFaq(@PathVariable Long id, @RequestBody FaqUpdateRequestDto request) {
        try {
            Faq faq = faqService.updateFaq(id, request);
            return ResponseEntity.ok(toDto(faq));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaq(@PathVariable Long id) {
        try {
            faqService.deleteFaq(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private FaqResponseDto toDto(Faq faq) {
        return FaqResponseDto.builder()
                .id(faq.getId())
                .category(faq.getCategory())
                .visibility(faq.getVisibility())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .build();
    }
}
