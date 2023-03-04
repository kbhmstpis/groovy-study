package todo


import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class Task {

    /**
     * Время начала выполнения задачи
     */
    LocalDateTime beginTime

    /**
     * Время окончания выполнения задания
     */
    LocalDateTime endTime

    /**
     * Длительность выполнения задачи
     */
    int duration

    /**
     * Единицы измерения длительности выполнения задачи
     */
    TimeUnit durationUnit

    /**
     * Имя задачи
     */
    String taskName

    /**
     * Список действий, которые необходимо выполнить в задаче
     */
    List<Action> actions = []


    /**
     * Нормальный конструктор
     *
     * @param taskName Имя задачи
     * @param beginTime Время начала выполнения
     * @param duration Длительность выполнения
     * @param durationUnit Единицы измерения длительности выполнения
     */
    Task(String taskName, LocalDateTime beginTime, int duration, TimeUnit durationUnit) {
        this.taskName = taskName
        this.beginTime = beginTime
        this.duration = duration
        this.durationUnit = durationUnit
        this.endTime = beginTime.plusSeconds(durationUnit.toSeconds(duration))
    }

    void addAction(String actionName, LocalDateTime beginTime, int duration, TimeUnit durationUnit) {
        Action action = new Action(actionName, beginTime, duration, durationUnit)
        checkAction(action)
        actions << action
    }

    private void checkAction(Action action) {
        if (action.beginTime.isBefore(beginTime)) {
            throw new TodoException("Action cannot start before task")
        }
        if (action.beginTime.isAfter(endTime)) {
            throw new TodoException("Action cannot start after the end of the task")
        }
        if (action.endTime.isAfter(endTime)) {
            throw new TodoException("Action cannot end after the end of the task")
        }

        actions.each {
            if (it.actionName == action.actionName) {
                throw new TodoException("Action name already exists")
            }
            if (!it.beginTime.isBefore(action.beginTime) && !it.endTime.isAfter(action.endTime)) {
                throw new TodoException("Action intersects with task $it.actionName")
            }
        }
    }

}
