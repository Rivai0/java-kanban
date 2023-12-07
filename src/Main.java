public class Main {


    public static void main(String[] args) {
        Manager manager = new Manager();


        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.IN_PROGRESS);
        final int taskId1 = manager.addNewTask(task1);
        final int taskId2 = manager.addNewTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", TaskStatus.NEW);
        final int epicId1 = manager.addNewEpic(epic1);
        final int epicId2 = manager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача 1-1", "Описание подзадачи 1", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("Подзадача 2-1", "Описание подзадачи 2", TaskStatus.NEW, epicId2);
        Subtask subtask3 = new Subtask("Подзадача 2-2", "Описание подзадачи 3", TaskStatus.NEW, epicId2);
        final Integer subtaskId1 = manager.addNewSubtask(subtask1);
        final Integer subtaskId2 = manager.addNewSubtask(subtask2);
        final Integer subtaskId3 = manager.addNewSubtask(subtask3);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        System.out.println("Обновление");
        Task taskStatus1 = manager.getTask(taskId1);
        taskStatus1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(taskStatus1);
        System.out.println("Смена статуса Задачи 1: Из NEW  В IN_PROGRESS");

        Subtask subtaskStatus1 = manager.getSubtask(subtaskId1);
        Subtask subtaskStatus2 = manager.getSubtask(subtaskId2);
        Subtask subtaskStatus3 = manager.getSubtask(subtaskId3);

        manager.updateSubtask(subtaskStatus1);
        subtaskStatus1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateEpic(epic1);
        System.out.println("Смена статуса Подзадачи 1-1: Из NEW  в IN_PROGRESS");
        subtaskStatus2.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtaskStatus2);
        manager.updateEpic(epic2);
        System.out.println("Смена статуса Подзадачи 2-1: из NEW  в DONE");
        subtaskStatus3.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtaskStatus3);
        manager.updateEpic(epic2);
        System.out.println("Смена статуса Подзадачи 2-2: из NEW  в IN_PROGRESS");


        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpicSubtasks(epicId1));
        System.out.println(manager.getEpicSubtasks(epicId2));
        manager.getTask(taskId2);
        manager.getTask(taskId1);
        manager.getEpic(epicId2);
        manager.getSubtask(subtaskId3);


        System.out.println("Удаление");
        manager.deleteSubtask(subtaskId2);
        System.out.println("Удалена Подзадача 2-1");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        manager.deleteAllEpics();
        System.out.println("Удалены все эпики");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());


    }
}