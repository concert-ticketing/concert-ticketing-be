package ticketing.ticketing.application.dto.concertDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConcertMapReadResponse {
    private BigDecimal locationX;
    private BigDecimal locationY;

    private String location;
    //Entity -> ConcertHall Area
    private String areaName;
    //Entity -> admin
    private String phone;


}
