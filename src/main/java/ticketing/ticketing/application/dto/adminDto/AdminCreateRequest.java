package ticketing.ticketing.application.dto.adminDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ticketing.ticketing.domain.enums.AdminRole;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminCreateRequest {

    private String adminId;
    private String password;
    private String company;
    private String companyNumber;
    private String companyLocation;
    private String phone;
    private String email;
}
