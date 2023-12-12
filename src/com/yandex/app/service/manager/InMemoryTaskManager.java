package com.yandex.app.service.manager;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.history.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int generatorId = 0;

    private int getNewId() {
        return ++generatorId;
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        if (!epics.containsKey(epicId)) {
            return new ArrayList<>();
        }
        Epic epic = epics.get(epicId);
        List<Subtask> subtasksList = epic.getSubtasks();
        for (Subtask subtask : subtasksList) {
            if (subtask.getEpicId() == epicId) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }

    @Override
    public int addNewTask(Task task) {
        final int id = getNewId();
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        final int id = getNewId();
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Такой важной задачи нет");
            return -1;
        }
        subtask.setId(id);
        epic.addSubtask(subtask);
        subtasks.put(id, subtask);
        updateEpicStatus(subtask.getEpicId());
        return id;

    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id = getNewId();
        epic.setId(id);
        updateEpicStatus(epic.getId());
        epics.put(id, epic);
        return id;
    }

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            return;
        }
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null) {
            return;
        }
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());
        }

    }

    @Override
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

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic removedEpic = epics.remove(id);
        for (Subtask deleteSubtask : removedEpic.getSubtasks()) {
            subtasks.remove(deleteSubtask.getId());
        }
    }

    @Override
    public void deleteSubtask(Subtask subtask) {
        if (subtasks.containsValue(subtask)) {
            int epicId = subtask.getEpicId();
            subtasks.remove(subtask.getId());
            if (epics.containsKey(epicId)) {
                epics.get(epicId).removeSubtask(subtask);
                updateEpicStatus(epicId);
            }
        }
    }


    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        if (!epics.isEmpty()) {
            for (Epic epic : epics.values()) {
                epic.cleanSubtaskId();
                updateEpicStatus(epic.getId());
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void updateEpicStatus(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            List<Subtask> subtasksList = epic.getSubtasks();
            if (subtasksList.isEmpty()) {
                epic.setStatus(Status.NEW);
                return;
            }
            int newTask = 0;
            int inProgress = 0;
            int done = 0;
            for (Subtask subtask : subtasksList) {
                if (subtask != null && subtasks.containsKey(subtask.getId())) {
                    if (subtasks.get(subtask.getId()).getStatus().equals(Status.DONE)) {
                        done++;
                    } else if (subtasks.get(subtask.getId()).getStatus().equals(Status.IN_PROGRESS)) {
                        inProgress++;
                    } else if (subtasks.get(subtask.getId()).getStatus().equals(Status.NEW)) {
                        newTask++;
                    }
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