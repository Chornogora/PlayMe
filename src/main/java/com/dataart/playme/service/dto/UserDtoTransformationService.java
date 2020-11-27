package com.dataart.playme.service.dto;

import com.dataart.playme.dto.UserDto;
import com.dataart.playme.model.User;

public interface UserDtoTransformationService {

    User transform(UserDto userDto);
}
