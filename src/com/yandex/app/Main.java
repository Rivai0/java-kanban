package com.yandex.app;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.model.Status;
import com.yandex.app.service.Manager;

public class Main {


    public static void main(String[] args) {
        Manager manager = new Manager();


        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.IN_PROGRESS);
        final int taskId1 = manager.addNewTask(task1);
        final int taskId2 = manager.addNewTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        final int epicId1 = manager.addNewEpic(epic1);
        final int epicId2 = manager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача 1-1", "Описание подзадачи 1", Status.NEW, epicId1);
        Subtask subtask2 = new Subtask("Подзадача 2-1", "Описание подзадачи 2", Status.NEW, epicId2);
        Subtask subtask3 = new Subtask("Подзадача 2-2", "Описание подзадачи 3", Status.NEW, epicId2);
        final Integer subtaskId1 = manager.addNewSubtask(subtask1);
        final Integer subtaskId2 = manager.addNewSubtask(subtask2);
        final Integer subtaskId3 = manager.addNewSubtask(subtask3);

        System.out.println("Список задач:");
        System.out.println(manager.getTasks());
        System.out.println("Список эпиков:");
        System.out.println(manager.getEpics());
        System.out.println("Список подзадач:");
        System.out.println(manager.getSubtasks());

        System.out.println("Изменение статусов:");
        Task taskStatus1 = manager.getTask(taskId1);
        taskStatus1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(taskStatus1);
        System.out.println("Статус Задачи 1: " + manager.getTask(taskId1).getStatus());

        Subtask subtaskStatus1 = manager.getSubtask(subtaskId1);
        subtaskStatus1.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtaskStatus1);
        System.out.println("Статус Подзадачи 1-1: " + manager.getSubtask(subtaskId1).getStatus());


        System.out.println("Удаление:");
        manager.deleteTask(taskId1);
        System.out.println("Удалена Задача 1");
        manager.deleteEpic(epicId1);
        System.out.println("Удален Эпик 1");

        System.out.println("Задачи после удаления:");
        System.out.println(manager.getTasks());
        System.out.println("Эпики после удаления:");
        System.out.println(manager.getEpics());
        System.out.println("Подзадачи после удаления:");
        System.out.println(manager.getSubtasks());


    }
}