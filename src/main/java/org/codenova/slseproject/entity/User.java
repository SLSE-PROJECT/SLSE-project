package org.codenova.slseproject.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class User {
    private int id;
    private String email;
    private String password;
    private String nickname;
    private String provider;
    private String providerId;
    private LocalDateTime createAt;
    private int SLSE;

    private List<Champion> champions;
}
