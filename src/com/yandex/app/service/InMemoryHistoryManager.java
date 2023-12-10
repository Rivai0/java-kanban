package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> viewedTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (!viewedTasks.contains(task)) {
            if (viewedTasks.size() >= 10) {
                viewedTasks.remove(0);
            }
            viewedTasks.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(viewedTasks);
    }
}