package com.example.todo.controller;

import com.example.todo.model.entity.Task;
import com.example.todo.model.entity.User;
import com.example.todo.model.enums.StatusTask;
import com.example.todo.service.TaskService;

import com.example.todo.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/v1/task")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/main")
    public String getTasks(@RequestParam(required = false)String status,
                           Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login"; // Если не аутентифицирован - на логин
        }
        List<Task> tasks= taskService.getTasksByUserEmail(userDetails.getUsername(),status);
        User user = userService.findByEmail(userDetails.getUsername());

        model.addAttribute("tasks",tasks);
        model.addAttribute("task",new Task());
        model.addAttribute("user",user);
        String completedCount = taskService.getCompletedCount(userDetails.getUsername());
        model.addAttribute("completedCount", completedCount);
        return "test";  // Будет искать templates/taskMain.html
    }
@PostMapping
    public String createTask(@ModelAttribute Task task,
                             @AuthenticationPrincipal UserDetails userDetails){
    if (userDetails == null) {
        return "redirect:/login";
    }
    User user = userService.findByEmail(userDetails.getUsername());
    task.setUser(user); // Связываем задачу с пользователем
    taskService.saveTask(task);
        return "redirect:/v1/task/main";
}
@PostMapping("/delete/{id}")
    public String delete (@PathVariable UUID id){
taskService.delete(id);
    return "redirect:/v1/task/main";
}
    @PostMapping("/changeStatus/{id}/{status}")
    public String changeStatus (@PathVariable UUID id,
                                @PathVariable StatusTask status){
        taskService.changeStatus(id,status);
        return "redirect:/v1/task/main";
    }
   /* public ResponseEntity<Void>deleteById(
            HttpServletRequest request,
           */// @PathVariable Long id){


}

//@GetMapping("/info")
//public String getInfo(@RequestHeader("Authorization") String token) {
//    // Чтение заголовка Authorization
//}
//@PostMapping("/cart")
//public void addToCart(@SessionAttribute("cartId") String cartId) {
//    // Работа с сессией
//}
//@GetMapping("/profile")
//public Profile getProfile(@CookieValue("sessionId") String sessionId) {
//    // Чтение cookie
//}