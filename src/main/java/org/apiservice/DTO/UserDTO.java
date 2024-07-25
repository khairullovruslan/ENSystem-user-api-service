package org.apiservice.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {
    String fullName;
    String email;

}
