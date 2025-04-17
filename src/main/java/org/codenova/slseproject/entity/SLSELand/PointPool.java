package org.codenova.slseproject.entity.SLSELand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codenova.slseproject.entity.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointPool {
    private int id;
    private int totalAmount;
    private LocalDateTime updatedAt;
}