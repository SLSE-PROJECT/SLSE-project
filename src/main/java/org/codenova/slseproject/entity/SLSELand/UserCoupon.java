package org.codenova.slseproject.entity.SLSELand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codenova.slseproject.entity.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon {
    private int id;
    private int userId;
    private boolean isUsed;
    private LocalDateTime createdAt;
    private LocalDateTime usedAt;
}
