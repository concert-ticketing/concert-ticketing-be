package ticketing.ticketing.application.dto.userDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserOAuthUpdateRequest {

    private String name;
    private String gender;
    private String email;
    private String phone;
    private LocalDate birthday;
}
