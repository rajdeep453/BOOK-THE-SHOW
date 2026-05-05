package org.example.bms.Service;

import org.example.bms.dto.UserDto;
import org.example.bms.exception.ResourceNotFoundException;
import org.example.bms.model.User;
import org.example.bms.reposatory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public UserDto createUser(UserDto userDto){
User useer=mapToEntity(userDto);
User savedUser=userRepository.save(useer);
        return mapToDto(savedUser);
    }

 public  UserDto getUserById(Long id){
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User Not Found"));
        return mapToDto(user);
 }
    public List<UserDto> getAllUsers()
    {
        List<User> users=userRepository.findAll();
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    public UserDto updateUser(Long id, UserDto userDto) {
       
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found with id: " + id));


        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());

        User updatedUser = userRepository.save(existingUser);

      
        return mapToDto(updatedUser);
    }

    private User mapToEntity(UserDto userDto) {
        User user=new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return  user;
    }

    private UserDto mapToDto(User user)
    {
        UserDto userDto=new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;

    }
}
