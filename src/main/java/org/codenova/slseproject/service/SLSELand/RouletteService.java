package org.codenova.slseproject.service.SLSELand;

import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.entity.vo.RouletteResult;

public interface RouletteService {


    int getTotalPoint();

    int applyForPoint(int userId);

    RouletteResult spin(User user);
}
