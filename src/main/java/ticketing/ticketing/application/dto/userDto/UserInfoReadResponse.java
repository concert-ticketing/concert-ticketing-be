package ticketing.ticketing.application.dto.userDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ticketing.ticketing.domain.enums.Gender;
import ticketing.ticketing.domain.enums.UserState;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoReadResponse {

    private String userId;
    private String email;
    private String name;
    private String phone;
    private String nickName;
    private Gender gender;
    private UserState state;
    private LocalDate birthday;
}
