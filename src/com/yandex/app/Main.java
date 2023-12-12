package com.yandex.app;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.manager.Managers;
import com.yandex.app.service.manager.TaskManager;

public class Main {


    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();


        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.IN_PROGRESS);
        final int taskId1 = taskManager.addNewTask(task1);
        final int taskId2 = taskManager.addNewTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        final int epicId1 = taskManager.addNewEpic(epic1);
        final int epicId2 = taskManager.addNewEpic(epic2);
        int epicId = taskManager.addNewEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача 1-1", "Описание подзадачи 1", Status.NEW, epicId1);
        Subtask subtask2 = new Subtask("Подзадача 2-1", "Описание подзадачи 2", Status.NEW, epicId2);
        Subtask subtask3 = new Subtask("Подзадача 2-2", "Описание подзадачи 3", Status.NEW, epicId2);
        final Integer subtaskId1 = taskManager.addNewSubtask(subtask1);
        final Integer subtaskId2 = taskManager.addNewSubtask(subtask2);
        final Integer subtaskId3 = taskManager.addNewSubtask(subtask3);

        System.out.println("Список задач:");
        System.out.println(taskManager.getTasks());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getEpics());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getSubtasks());

        System.out.println("Изменение статусов:");
        Task taskStatus1 = taskManager.getTask(taskId1);
        taskStatus1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(taskStatus1);
        System.out.println("Статус Задачи 1: " + taskManager.getTask(taskId1).getStatus());
        printHistory(taskManager);

        Subtask subtaskStatus1 = taskManager.getSubtask(subtaskId1);
        subtaskStatus1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtaskStatus1);
        System.out.println("Статус Подзадачи 1-1: " + taskManager.getSubtask(subtaskId1).getStatus());
        printHistory(taskManager);

        System.out.println("Удаление:");
        taskManager.deleteTask(taskId1);
        System.out.println("Удалена Задача 1");
        printHistory(taskManager);

        taskManager.deleteEpic(epicId1);
        System.out.println("Удален Эпик 1");

        System.out.println("Задачи после удаления:");
        System.out.println(taskManager.getTasks());
        System.out.println("Эпики после удаления:");
        System.out.println(taskManager.getEpics());
        System.out.println("Подзадачи после удаления:");
        System.out.println(taskManager.getSubtasks());

    }

    public static void printHistory(TaskManager taskManager) {
        System.out.println("История просмотров:");
        System.out.println(taskManager.getHistory());
    }
}