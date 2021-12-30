package org.but.feec.csfd.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.but.feec.csfd.api.user.UserAuthView;
import org.but.feec.csfd.data.UserRepository;
import org.but.feec.csfd.exception.ResourceNotFoundException;

public class AuthService {

    private UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserAuthView findUserByNick(String nickname) {
        return userRepository.findUserByNick(nickname);
    }

    public boolean authenticate(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        UserAuthView userAuthView = findUserByNick(username);
        if (userAuthView == null) {
            throw new ResourceNotFoundException("Provided username is not found.");
        }

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), userAuthView.getPassword());
        return result.verified;
    }


}
