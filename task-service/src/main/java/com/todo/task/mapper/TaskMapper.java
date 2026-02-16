package com.todo.task.mapper;

import com.todo.common.dto.CreateTaskRequest;
import com.todo.common.dto.TaskDto;
import com.todo.common.dto.UpdateTaskRequest;
import com.todo.common.enums.StatusTask;
import com.todo.task.entity.Task;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = {StatusTask.class, LocalDateTime.class})
public interface TaskMapper {

    /**
     * Преобразование Entity → DTO
     */
    @Mapping(target = "status", expression = "java(task.getStatus().name())")
    TaskDto toDto(Task task);

    /**
     * Преобразование списка Entity → список DTO
     */
    List<TaskDto> toDtoList(List<Task> tasks);

    /**
     * Преобразование запроса на создание → Entity
     * userId передается отдельно, так как его нет в запросе
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "dateTimeOfCompletion", ignore = true)
    Task toEntity(CreateTaskRequest request, @Context Long userId);

    /**
     * Обновление существующей задачи из запроса
     * Обновляются только не-null поля
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "dateTimeOfCompletion", ignore = true)
    void updateTaskFromRequest(UpdateTaskRequest request, @MappingTarget Task task);

    /**
     * Кастомный метод для обновления статуса с бизнес-логикой
     */
    default void updateTaskStatus(Task task, String newStatus) {
        if (newStatus == null) return;

        StatusTask status = StatusTask.valueOf(newStatus);

        if (status == StatusTask.COMPLETED) {
            task.setDateTimeOfCompletion(java.time.LocalDateTime.now());
        } else if (task.getStatus() == StatusTask.COMPLETED && status != StatusTask.COMPLETED) {
            task.setDateTimeOfCompletion(null);
        }

        task.setStatus(status);
    }
}