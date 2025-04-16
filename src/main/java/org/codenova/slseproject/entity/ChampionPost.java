package org.codenova.slseproject.entity;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChampionPost {

    private int id;
    private int userId; // user 테이블 외래키
    private String championId; // champion 테이블 외래키
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private String nickname;
}
