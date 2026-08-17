package com.thittaiAmman.thittai_backend.service;

import com.thittaiAmman.thittai_backend.model.Gallery;
import com.thittaiAmman.thittai_backend.model.UserRoles;
import com.thittaiAmman.thittai_backend.model.Users;
import com.thittaiAmman.thittai_backend.repo.GalleryRepository;
import com.thittaiAmman.thittai_backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private UserRepository repo;
    @Autowired
    private GalleryRepository galleryRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public List<Users> getUsers() {
        return repo.findAll();
    }

    public boolean updateUserRole(Users user) {

        Users existingUser = repo.findByUsername(user.getUsername());

        if (existingUser==null){
            return false;
        }
        existingUser.setRoles(user.getRoles());
        repo.save(existingUser);
        return true;
    }

    public String verify(Users user) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        if (authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());
        }
        return null;
    }

    public Users returnUser(String username) {
        return repo.findByUsername(username);
    }

    public Users signUpUser(Users user) {
        Users existing = repo.findByUsername(user.getUsername());

        if (existing!=null){
            return null;
        }

        user.setRoles(UserRoles.USER);
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

}
