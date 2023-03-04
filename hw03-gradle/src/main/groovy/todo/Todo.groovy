package todo

import groovy.transform.PackageScope

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


class Todo {

    /**
     * Список дел
     */
    List<Task> todo = []

    /**
     * Добавить задачу
     *
     * @param name Имя задачи
     * @param time Время начала
     * @param duration Длительность
     * @param unit Единицы измерения длительности
     * @return Порядковый номер задачи в списке дел
     */
    int addTask(String name, String time, int duration, String unit) {
        TimeUnit durationUnit = checkDurationUnit(unit)
        LocalDateTime beginTime = checkTime(time)

        Task task = new Task(name, beginTime, duration, durationUnit)
        checkTask(task)

        todo << task
        return todo.size()-1
    }

    /**
     * Возвращает задачу по порядковому номеру
     *
     * @param tempId Порядковый номер задачи
     * @return Задача
     */
    Task getTask(int tempId) {
        return todo[tempId]
    }


    /**
     * Добавляет действие к задаче
     *
     * @param taskId Порядковый номер задачи в списке дел
     * @param name Имя действия
     * @param time Время начала действия
     * @param duration Длительность выполнения действия
     * @param unit Единицы измерения длительности действия
     */
    void addAction(int taskId, String name, String time, int duration, String unit) {
        TimeUnit durationUnit = checkDurationUnit(unit)
        LocalDateTime startTime = checkTime(time)
        def task = getTask(taskId)
        task.addAction(name, startTime, duration, durationUnit)
    }

    /**
     * Возвращает сведения о текущем задании и текущее действие
     * @param currentTime Текущее время
     * @return Событие со сведениями о текущем задании и текущее действие
     */
    Event getCurrent(LocalDateTime currentTime) {
        /*
        Жаль, что нельзя сделать так:
        def f = todo.map {task->
            if ((task.beginTime.isBefore(currentTime) || task.beginTime == currentTime) && task.endTime.isAfter(currentTime)) {
                return task.actions.map {action->
                    if ((action.beginTime.isBefore(currentTime) || action.beginTime == currentTime) && action.endTime.isAfter(currentTime)) {
                        return new Event().with {
                            it.beginTime = action.beginTime
                            it.taskName = task.taskName
                            it.actionName = action.actionName
                        }
                    }

                }
            }
        }
        */

        // Но с другой стороны, теперь и просмотре дифа всё понятно

        Task task = todo.find { task ->
            (task.beginTime.isBefore(currentTime) || task.beginTime == currentTime) && task.endTime.isAfter(currentTime)
        }
        if (task == null) {
            return null
        }

        Action action = task.actions.find {action ->
            (action.beginTime.isBefore(currentTime) || action.beginTime == currentTime) && action.endTime.isAfter(currentTime)
        }
        if (action == null) {
            return null
        }

        Event event = new Event()
        event.with {
            it.beginTime  = action.beginTime
            it.taskName   = task.taskName
            it.actionName = action.actionName
        }
        return event
    }


    int getTaskCountByDay(String time) {
        LocalDateTime beginTime = checkTime(time)
        def cnt = todo.groupBy {it.beginTime.toLocalDate()}
        return cnt[beginTime.toLocalDate()].size()
    }

    int genBusyTimeByDay(String time) {
        LocalDateTime beginTime = checkTime(time)
        def cnt = todo.groupBy {it.beginTime.toLocalDate()}[beginTime.toLocalDate()]
                .sum { Duration.between(it.beginTime, it.endTime).toSeconds()}
    }



    /**
     * Поддерживаемые единицы измерения длительности
     */
    private Set<TimeUnit> compatibleTimeUnit = new LinkedHashSet<TimeUnit>(){{
        add TimeUnit.HOURS
        add TimeUnit.MINUTES
        add TimeUnit.SECONDS
    }}

    /**
     * Проверяет что единицы измерения длительности поддерживаются списком дел
     *
     * @param unit Единица измерения
     * @return Длительность приведённая к TimeUnit
     */
    @PackageScope
    TimeUnit checkDurationUnit(String unit) {
        TimeUnit timeUnit = TimeUnit.valueOf(TimeUnit.class, unit)
        if (!compatibleTimeUnit.contains(timeUnit)) {
            throw new TodoException("Incompatible time unit $unit")
        }
        return timeUnit
    }


    /**
     * Проверяет что строка означающая время задана в правильном формате
     *
     * @param time Время заданное строкой
     * @return Время в формате LocalDateTime
     */
    @PackageScope
    LocalDateTime checkTime(String time) {
        LocalDateTime localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
    }

    /**
     * Проверяет что название задачи не повторяется и что нет пересечения по времени выполнения
     *
     * @param task Новая задача
     */
    private void checkTask(Task task) {
        todo.each {
            if (it.taskName == task.taskName) {
                throw new TodoException("Task name already exists")
            }
            if (!it.beginTime.isBefore(task.beginTime) && !it.endTime.isAfter(task.endTime)) {
                throw new TodoException("Task intersects with task $it.taskName")
            }
        }
    }

}
