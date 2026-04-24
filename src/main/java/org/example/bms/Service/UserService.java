package org.example.bms.Service;

import org.example.bms.dto.UserDto;
import org.example.bms.reposatory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public UserDto createUser(UserDto userDto){

    }

}
