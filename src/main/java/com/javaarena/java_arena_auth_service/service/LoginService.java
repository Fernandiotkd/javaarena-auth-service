package com.javaarena.java_arena_auth_service.service;

import com.javaarena.java_arena_auth_service.controller.LoginController;
import com.javaarena.java_arena_auth_service.model.User;
import com.javaarena.java_arena_auth_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserRepository userRepository;

    public String getUserName(Long id){
        log.info("Getting user name in service");

        try{
            Optional<User> byId = userRepository.findById(id);
            if(byId.isPresent()){
                return byId.get().getUsername();
            }else{
                return "User id not found";
            }
        }catch (Exception e){
            try {
                throw new Exception(e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

    }
}
