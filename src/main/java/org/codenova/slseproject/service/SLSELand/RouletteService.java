package org.codenova.slseproject.service.SLSELand;

import org.codenova.slseproject.entity.User;
import org.codenova.slseproject.entity.vo.RouletteResult;

public interface RouletteService {
    RouletteResult spin(User user);
}
