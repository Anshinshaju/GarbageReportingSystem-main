package service;

import model.User;
import repository.UserRepository;

public class UserService {
    private UserRepository userRepository;
    
    public UserService() {
        this.userRepository = new UserRepository();
    }
    
    public boolean registerUser(User user) {
        return userRepository.addUser(user);
    }
    
    public User loginUser(String userId, String password) {
        User user = userRepository.getUserById(userId);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    
    public boolean isEmailExists(String email) {
        return userRepository.isEmailExists(email);
    }
    
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
}