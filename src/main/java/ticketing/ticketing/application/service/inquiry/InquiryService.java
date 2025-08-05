package ticketing.ticketing.application.service.inquiry;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ticketing.ticketing.application.dto.inquiryRequestDto.InquiryRequestDto;
import ticketing.ticketing.application.dto.inquiryResponseDto.InquiryResponseDto;
import ticketing.ticketing.domain.entity.Inquiry;
import ticketing.ticketing.domain.entity.InquiryFile;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.domain.enums.InquiryStatus;
import ticketing.ticketing.domain.enums.InquiryType;
import ticketing.ticketing.exception.InquiryNotFoundException;
import ticketing.ticketing.exception.UserNotFoundException;
import ticketing.ticketing.infrastructure.repository.inquiry.InquiryRepository;
import ticketing.ticketing.infrastructure.repository.inquiryFile.InquiryFileRepository;
import ticketing.ticketing.infrastructure.repository.user.UserRepository;
import ticketing.ticketing.infrastructure.repository.userInquiry.UserInquiryRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private static final Logger log = LoggerFactory.getLogger(InquiryService.class);

    private final InquiryRepository inquiryRepository;
    private final InquiryFileRepository inquiryFileRepository;
    private final UserInquiryRepository userInquiryRepository;
    private final UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public Page<Inquiry> getAllInquiry(Pageable pageable) {
        InquiryStatus state = InquiryStatus.PENDING;
        return inquiryRepository.findAllByStatus(state, pageable);
    }

    public Page<InquiryResponseDto> getInquiriesByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        return inquiryRepository.findByUserId(user.getId(), pageable)
                .map(InquiryResponseDto::fromEntity);
    }

    public InquiryResponseDto getInquiryByIdAndUser(Long inquiryId, Long userId) {
        Inquiry inquiry = inquiryRepository.findByIdAndUserId(inquiryId, userId)
                .orElseThrow(() -> new InquiryNotFoundException("해당 ID의 문의가 존재하지 않거나 권한이 없습니다."));
        return InquiryResponseDto.fromEntity(inquiry);
    }

    public ResponseEntity<Page<InquiryResponseDto>> getAllInquiryByAdmin(int size, int page) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Inquiry> inquiries = inquiryRepository.findAll(pageable);
        Page<InquiryResponseDto> dtoPage = inquiries.map(InquiryResponseDto::fromEntity);
        return ResponseEntity.ok(dtoPage);
    }

    @Transactional(rollbackFor = Exception.class)
    public InquiryResponseDto createInquiryWithFiles(Long userId, InquiryRequestDto dto, List<MultipartFile> files) throws IOException {
        if (files != null && files.size() > 5) {
            throw new IllegalArgumentException("최대 5개의 파일만 업로드 가능합니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        Inquiry inquiry = new Inquiry(
                user,
                dto.getTitle(),
                dto.getContent(),
                InquiryType.valueOf(dto.getType()),
                InquiryStatus.PENDING,
                LocalDateTime.now()
        );
        inquiryRepository.save(inquiry);

        if (files != null) {
            for (MultipartFile file : files) {
                validateFile(file);
                String savedPath = saveFile(file);

                InquiryFile inquiryFile = new InquiryFile();
                inquiryFile.setInquiry(inquiry);
                inquiryFile.setFileName(file.getOriginalFilename());
                inquiryFile.setFilePath(savedPath);
                inquiryFileRepository.save(inquiryFile);
            }
        }

        return InquiryResponseDto.fromEntity(inquiry);
    }

    @Transactional
    public void markInquiryAsCompleted(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryNotFoundException("문의가 존재하지 않습니다."));
        inquiry.markCompleted(null, LocalDateTime.now());
    }

    @Transactional
    public void answerInquiry(Long inquiryId, String answer) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryNotFoundException("문의가 존재하지 않습니다."));
        inquiry.markCompleted(answer, LocalDateTime.now());
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("파일은 5MB를 초과할 수 없습니다.");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null
                || !(originalFileName.toLowerCase().endsWith(".jpg") || originalFileName.toLowerCase().endsWith(".png"))) {
            throw new IllegalArgumentException("jpg 또는 png 파일만 업로드 가능합니다.");
        }
    }

    private String saveFile(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String ext = "";
        String originalFileName = file.getOriginalFilename();
        if (originalFileName != null && originalFileName.contains(".")) {
            ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String fileName = uuid + ext;
        Path target = Paths.get(uploadPath).resolve(fileName);

        try {
            Files.createDirectories(target.getParent());
            file.transferTo(target.toFile());
            return target.toString();
        } catch (IOException e) {
            log.error("파일 저장 실패 : " + e.getMessage(), e);
            throw new RuntimeException("파일 저장에 실패했습니다.", e);
        }
    }
}
