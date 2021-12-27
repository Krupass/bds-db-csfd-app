package org.but.feec.csfd.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.but.feec.csfd.api.*;
import org.but.feec.csfd.data.UserRepository;
import org.but.feec.csfd.api.user.UserBasicView;
import org.but.feec.csfd.api.user.UserCreateView;
import org.but.feec.csfd.api.user.UserDetailView;
import org.but.feec.csfd.api.user.UserEditView;
import org.but.feec.csfd.api.user.UserDeleteView;

import java.util.List;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetailView getUserDetailView(Long id) {
        return userRepository.findUserDetailedView(id);
    }

    public List<UserBasicView> getUsersBasicView() {
        return userRepository.getUsersBasicView();
    }

    public void createUser(UserCreateView userCreateView) {
        // the following three lines can be written in one code line (only for more clear explanation it is written in three lines
        char[] originalPassword = userCreateView.getPwd();
        char[] hashedPassword = hashPassword(originalPassword);
        userCreateView.setPwd(hashedPassword);

        userRepository.createUser(userCreateView);
    }

    public void editUser(UserEditView userEditView) {
        userRepository.editUser(userEditView);
    }

    public void deleteUser(UserDeleteView userDeleteView) {
        userRepository.deleteUser(userDeleteView);
    }

    private char[] hashPassword(char[] password) {
        return BCrypt.withDefaults().hashToChar(12, password);
    }

}