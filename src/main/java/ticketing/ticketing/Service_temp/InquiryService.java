package ticketing.ticketing.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ticketing.ticketing.DTO.InquiryRequestDto;
import ticketing.ticketing.DTO.InquiryResponseDto;
import ticketing.ticketing.Repository.InquiryFileRepository;
import ticketing.ticketing.Repository.InquiryRepository;
import ticketing.ticketing.Repository.UserInquiryRepository;
import ticketing.ticketing.domain.entity.Inquiry;
import ticketing.ticketing.domain.entity.InquiryFile;
import ticketing.ticketing.domain.entity.User;
import ticketing.ticketing.domain.enums.InquiryStatus;
import ticketing.ticketing.domain.enums.InquiryType;
import ticketing.ticketing.exception.InquiryNotFoundException;
import ticketing.ticketing.exception.UserNotFoundException;

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

    private final InquiryRepository inquiryRepository;
    private final InquiryFileRepository inquiryFileRepository;
    private final UserInquiryRepository userInquiryRepository;

    private final String uploadPath = "uploads";

    public Page<InquiryResponseDto> getInquiriesByUser(Long userId, Pageable pageable) {
        if (userId != null) {
            return inquiryRepository.findByUserId(userId, pageable)
                    .map(InquiryResponseDto::fromEntity);
        } else {
            return inquiryRepository.findAll(pageable)
                    .map(InquiryResponseDto::fromEntity);
        }
    }

    public InquiryResponseDto getInquiryByIdAndUser(Long inquiryId, Long userId) {
        Inquiry inquiry = inquiryRepository.findByIdAndUserId(inquiryId, userId)
                .orElseThrow(() -> new InquiryNotFoundException("해당 ID의 문의가 존재하지 않거나 권한이 없습니다."));
        return InquiryResponseDto.fromEntity(inquiry);
    }

    @Transactional(rollbackFor = Exception.class)
    public InquiryResponseDto createInquiryWithFiles(Long userId, InquiryRequestDto dto, List<MultipartFile> files) {
        if (files != null && files.size() > 5) {
            throw new IllegalArgumentException("최대 5개의 파일만 업로드 가능합니다.");
        }

        User user = userInquiryRepository.findById(userId)
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

        inquiry.markCompleted(null, LocalDateTime.now());  // 답변 없이 완료 처리
    }

    @Transactional
    public void answerInquiry(Long inquiryId, String answer) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryNotFoundException("문의가 존재하지 않습니다."));

        inquiry.markCompleted(answer, LocalDateTime.now());  // 답변과 함께 완료 처리
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("파일은 5MB를 초과할 수 없습니다.");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || (!originalFileName.endsWith(".jpg") && !originalFileName.endsWith(".png"))) {
            throw new IllegalArgumentException("jpg 또는 png 파일만 업로드 가능합니다.");
        }
    }

    private String saveFile(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "_" + file.getOriginalFilename();
        Path target = Paths.get(uploadPath).resolve(fileName);

        try {
            Files.createDirectories(target.getParent());
            file.transferTo(target.toFile());
            return target.toString();
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다.", e);
        }
    }
}
