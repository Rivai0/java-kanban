package com.yandex.app.service.history;

import com.yandex.app.model.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void add(Task task);
}