package com.example.todo.service;


import com.example.todo.controller.CustomUserDetails;
import com.example.todo.exception.EmailAlreadyExistsException;
import com.example.todo.exception.UserNotFoundException;
import com.example.todo.model.dto.user.CreateUserRequest;
import com.example.todo.model.dto.user.UpdateUserRequest;
import com.example.todo.model.dto.user.UserResponse;
import com.example.todo.model.entity.Task;
import com.example.todo.model.entity.User;
import com.example.todo.model.enums.Role;
import com.example.todo.model.enums.StatusTask;
import com.example.todo.model.mapper.UserMapper;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserService implements UserDetailsService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskRepository taskRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder, TaskRepository taskRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.taskRepository = taskRepository;
    }


    public List<UserResponse> getAll() {
         List<User> listOfAll = userRepository.findAll();
         return userMapper.toResponseList(listOfAll);
    }

    public UserResponse getUser(Long id) {
        User user =  userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
return userMapper.toResponse(user);
    }

    public User createUser(CreateUserRequest request) {
        if(userRepository.existsByEmail(request.email())){
            throw new EmailAlreadyExistsException(request.email());
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        return userRepository.save(user);
    }

    public UserResponse updateUser(String email,UpdateUserRequest request) {
        User findUserByEmail = userRepository.findUserByEmail(email).orElseThrow(()->new UserNotFoundException(email));
        if(request.password() != null ){
            findUserByEmail.setPassword(passwordEncoder.encode(request.password()));
        }if(request.name()!=null){
            findUserByEmail.setName(request.name());
        }else{
            return userMapper.toResponse(findUserByEmail);
        }
        userRepository.flush();
        return userMapper.toResponse(findUserByEmail);
    }

    public boolean deleteUser(String email) {
        return userRepository.findUserByEmail(email).map(user->{
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }

    public void save(@Valid String name, @Valid String password, @Valid String email) {
        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException(email);
        }
        CreateUserRequest user = new CreateUserRequest(name,email,password);
User entity = userMapper.toEntity(user);
entity.setRole(Role.USER);
entity.setPassword(passwordEncoder.encode(password));
userRepository.save(entity);
}


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email).orElseThrow(()-> new UsernameNotFoundException(email));
        CustomUserDetails details = new CustomUserDetails();
        details.setId(user.getId());
        details.setEmail(user.getEmail());
        details.setName(user.getName());
        details.setPassword(user.getPassword());
        details.setRole(user.getRole());
        return details;
    }

    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(()->new UserNotFoundException(email));

    }
    public Map<User, Long> getUsersWithCompletedTasksCountLast24Hours() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twentyFourHoursAgo = now.minusHours(24);

        List<User> users = userRepository.findUsersWithTasksCompletedInPeriod(
                twentyFourHoursAgo, now
        );

        // Группируем пользователей и считаем количество повторений
        return users.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),  // Группируем по самому пользователю
                        Collectors.counting() // Считаем количество вхождений
                ));
    }
    public UserResponse getProfile(String email){
        User user = userRepository.findUserByEmail(email).orElseThrow(()->new UserNotFoundException(email));
        return userMapper.toResponse(user);
}


    @PostConstruct
    public void initAdmin(){
        if(!userRepository.existsByEmail("admin228@mail.ru")){
            User admin = new User();
            admin.setName("Администратор");
            admin.setEmail("admin228@mail.ru");
            admin.setPassword(passwordEncoder.encode("A12345678"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Создан админ!");

}
}


    public boolean existsById(long userId) {
        return userRepository.existsById(userId);
    }
}




