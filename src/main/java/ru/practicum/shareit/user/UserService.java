package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User creatUser (User user){
        return userRepository.creatUser(user);
    }

    public User updateUser(User user, int id){
        return userRepository.updateUser(user,id);
    }

    public User getUser (int id){
        return userRepository.getUser(id);
    }

    public void deleteUser(int id){
       userRepository.deleteUser(id);
    }

    public List<User> getAllUser (){
        return userRepository.getAllUser();
    }

}
