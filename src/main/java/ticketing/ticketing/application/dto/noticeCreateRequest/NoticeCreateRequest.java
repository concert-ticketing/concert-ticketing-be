package ticketing.ticketing.application.dto.noticeCreateRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ticketing.ticketing.domain.enums.NoticeVisibility;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeCreateRequest {
    private String title;
    private String content;
    @Schema(description = "노출 상태", allowableValues = {"VISIBLE", "HIDDEN"})
    private NoticeVisibility visibility;
    private List<String> imagePaths;
}
