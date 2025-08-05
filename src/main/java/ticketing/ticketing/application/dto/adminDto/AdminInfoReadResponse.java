package ticketing.ticketing.application.dto.adminDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ticketing.ticketing.domain.enums.AdminRole;
import ticketing.ticketing.domain.enums.AdminState;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminInfoReadResponse {

    private String adminId;
    private String phone;
    private AdminRole role;
    private String email;
    private String company;
    private String companyNumber;
    private String companyLocation;
    private AdminState state;

}
