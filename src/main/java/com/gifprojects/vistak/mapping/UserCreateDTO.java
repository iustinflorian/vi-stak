package com.gifprojects.vistak.mapping;

import com.gifprojects.vistak.model.GenderType;
import lombok.Data;

@Data
public class UserCreateDTO {
    String username;
    String password;
    String email;
    GenderType genderType;
}
