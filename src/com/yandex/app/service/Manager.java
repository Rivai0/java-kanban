package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.model.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatorId = 0;

    private int getNewId() {
        return ++generatorId;
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public void getEpic(int id) {
        epics.get(id);
    }


    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        if (!epics.containsKey(epicId)) {
            return new ArrayList<>();
        }
        Epic epic = epics.get(epicId);
        ArrayList<Integer> sub = epic.getSubtaskId();
        for (int id : sub) {
            if (subtasks.get(id).getEpicId() == epicId) {
                epicSubtasks.add(subtasks.get(id));
            }
        }
        return epicSubtasks;
    }


    public int addNewTask(Task task) {
        final int id = getNewId();
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public Integer addNewSubtask(Subtask subtask) {
        final int id = getNewId();
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Такой важной задачи нет");
            return -1;
        }
        subtask.setId(id);
        epic.addSubtaskId(subtask.getId());
        subtasks.put(id, subtask);
        updateEpicStatus(subtask.getEpicId());
        return id;

    }


    public int addNewEpic(Epic epic) {
        final int id = getNewId();
        epic.setId(id);
        updateEpicStatus(epic.getId());
        epics.put(id, epic);
        return id;
    }

    public void updateTask(Task task) {
        if (task == null) {
            return;
        }
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epic == null) {
            return;
        }
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());
        }

    }

    public void updateSubtask(Subtask subtask) {
        if (subtask == null) {
            return;
        }
        if (subtasks.containsKey(subtask.getId())) {
            int epicId = subtasks.get(subtask.getId()).getEpicId();
            if (subtask.getEpicId() == epicId) {
                subtasks.put(subtask.getId(), subtask);
                updateEpicStatus(subtask.getEpicId());
            }
        }
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        Epic removedEpic = epics.remove(id);
        for (int deleteSubtaskId : removedEpic.getSubtaskId()) {
            subtasks.remove(deleteSubtaskId);
        }
    }


    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            int epicId = subtasks.get(id).getEpicId();
            subtasks.remove(id);
            epics.get(epicId).removeSubtask(id);
            updateEpicStatus(epicId);
        }


    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        if (!epics.isEmpty()) {
            for (Epic epic : epics.values()) {
                epic.cleanSubtaskId();
                updateEpicStatus(epic.getId());
            }
        }
    }


    public void updateEpicStatus(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            ArrayList<Integer> sub = epic.getSubtaskId();
            if (sub.isEmpty()) {
                epic.setStatus(Status.NEW);
                return;
            }
            int newTask = 0;
            int inProgress = 0;
            int done = 0;
            for (int id : sub) {
                if (subtasks.get(id).getStatus().equals(Status.DONE)) {
                    done++;
                } else if (subtasks.get(id).getStatus().equals(Status.IN_PROGRESS)) {
                    inProgress++;
                } else if (subtasks.get(id).getStatus().equals(Status.NEW)) {
                    newTask++;
                }
            }
            if (newTask > 0 && inProgress == 0 && done == 0) {
                epic.setStatus(Status.NEW);
                return;
            }
            if (inProgress > 0) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
            if (done > 0 && inProgress == 0 && newTask == 0) {
                epic.setStatus(Status.DONE);
            }
        }
    }
}