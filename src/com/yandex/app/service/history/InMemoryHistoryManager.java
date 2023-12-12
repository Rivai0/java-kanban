package com.yandex.app.service.history;

import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_HISTORY_SIZE = 10;
    private final LinkedList<Task> viewedTasks = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task != null && !viewedTasks.contains(task)) {
            if (viewedTasks.size() >= MAX_HISTORY_SIZE) {
                viewedTasks.removeFirst();
            }
            viewedTasks.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(viewedTasks);
    }
}