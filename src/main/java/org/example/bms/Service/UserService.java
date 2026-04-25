package org.example.bms.Service;

import org.example.bms.dto.UserDto;
import org.example.bms.model.User;
import org.example.bms.reposatory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public UserDto createUser(UserDto userDto){
User useer=mapToEntity(userDto);
    }

    public User mapToEntity(UserDto userDto){
        User user=new User();
        user.setName(userDto.getName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return  user;
    }
}
