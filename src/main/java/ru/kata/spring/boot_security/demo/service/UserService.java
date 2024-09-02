package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    public List<User> findAllUsers();

    public User findUserById(Long userId);


    public void saveUser(User user);


    public boolean updateUser(User user);

    public boolean deleteUser(Long userId);

    User findByEmail(String email);

}
