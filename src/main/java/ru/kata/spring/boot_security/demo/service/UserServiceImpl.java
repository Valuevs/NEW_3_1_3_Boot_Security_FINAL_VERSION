package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;

import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private EntityManager em;


    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;

    }


    @Override
    public User findUserById(Long userId) {
        Optional<User> userFromDb = userDao.findById(userId);
        return userFromDb.orElse(new User());
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAll();
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoles(user.getRoles());
        userDao.save(user);
    }

    @Override
    @Transactional
    public boolean updateUser(User user) {
        Optional<User> userFromDb = userDao.findById(user.getId());
        if (userFromDb.isPresent()) {
            userFromDb.get().setUsername(user.getUsername());
        }
        userFromDb.get().setPassword(user.getPassword());
        userDao.save(userFromDb.get());
        return true;
    }

    @Override
    @Transactional
    public boolean deleteUser(Long userId) {
        if (userDao.findById(userId).isPresent()) {
            userDao.deleteById(userId);
            return true;
        }
        return false;
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }


}
