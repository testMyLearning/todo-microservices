package com.example.todo.model.enums;

public enum StatusTask {
    ACTIVE("Активна")
    ,INACTIVE("Неактивна"),
    COMPLETED ("Завершена");

    private final String displayName;

    StatusTask(String displayName) {
        this.displayName = displayName;
    }
public String getDisplayName() {
return displayName;
}
}
