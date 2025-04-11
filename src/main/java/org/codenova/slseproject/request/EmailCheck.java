package org.codenova.slseproject.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailCheck {
    @Email
    private String email;
}
