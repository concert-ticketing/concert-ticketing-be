package ticketing.ticketing.application.dto.imagesDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ImagesThumbNailReadResponse {

    private String ThumbNailImageUrl;
    private Long concertId;

}
