package todo

import org.junit.jupiter.api.Test

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.containsString
import static org.junit.jupiter.api.Assertions.*

class TodoTest {

    @Test
    void addTask() {
        Todo todo = new Todo()
        int id0 = todo.addTask("First test task", "2023-01-25T14:56:18", 1, "HOURS")
        assertNotNull(id0)
        assertEquals(0, id0)


        Task task= todo.getTask(id0)

        assertNotNull(task)
        assertEquals("First test task", task.taskName)
        assertEquals("2023-01-25T14:56:18", task.beginTime.format(DateTimeFormatter.ISO_DATE_TIME))
        assertEquals(1, task.duration)
        assertEquals(TimeUnit.HOURS, task.durationUnit)

        int id1 = todo.addTask("Second test task", "2023-01-26T14:56:18", 1, "HOURS")
        assertNotNull(id1)
        assertEquals(1, id1)

        // Name
        try {
            todo.addTask("Second test task", "2023-01-26T14:56:18", 1, "HOURS")
            throw new IllegalStateException("it will throw exception")
        }
        catch (TodoException ex) {
            assertThat(ex.getMessage(), containsString("exists"))
        }

        // Intersects
        try {
            todo.addTask("Third test task", "2023-01-26T14:56:18", 1, "HOURS")
            throw new IllegalStateException("it will throw exception")
        }
        catch (TodoException ex) {
            assertThat(ex.getMessage(), containsString("Second test task"))
        }

    }

    @Test
    void addAction() {
        Todo todo = new Todo()
        int id = todo.addTask("The Test task", "2023-01-25T14:00:00", 1, "HOURS")

        todo.addAction(id, "First action",  "2023-01-25T14:00:00", 10, "MINUTES")
        todo.addAction(id, "Second action", "2023-01-25T14:10:00", 10, "MINUTES")

        assertEquals(2, todo.getTask(0).actions.size())


        TodoException exception = assertThrows(TodoException.class, () ->{
                    todo.addAction(id, "Second action", "2023-01-25T14:10:00", 10, "MINUTES")
                },
                "it will throw exception"
        )
        assertThat(exception.message, containsString("already exists"))

        exception = assertThrows(TodoException.class, () ->{
            todo.addAction(id, "Before start action", "2023-01-25T13:10:00", 10, "MINUTES")
        },
                "it will throw exception"
        )
        assertThat(exception.message, containsString("Action cannot start before task"))

        exception = assertThrows(TodoException.class, () ->{
            todo.addAction(id, "After end action", "2023-01-25T15:00:00", 10, "MINUTES")
        },
                "it will throw exception"
        )
        assertThat(exception.message, containsString("Action cannot end after the end of the task"))

        exception = assertThrows(TodoException.class, () ->{
            todo.addAction(id, "Long action", "2023-01-25T14:20:00", 1, "HOURS")
        },
                "it will throw exception"
        )
        assertThat(exception.message, containsString("Action cannot end after the end of the task"))
    }

    @Test
    void getCurrent() {
        Todo todo = new Todo()
        int id = todo.addTask("Task 01", "2023-01-25T14:00:00", 1, "HOURS")
        todo.addAction(id, "Action 01.01", "2023-01-25T14:00:00", 20, "MINUTES")
        todo.addAction(id, "Action 01.02", "2023-01-25T14:20:00", 20, "MINUTES")
        todo.addAction(id, "Action 01.03", "2023-01-25T14:40:00", 20, "MINUTES")

        id = todo.addTask("Task 02", "2023-01-25T15:00:00", 1, "HOURS")
        todo.addAction(id, "Action 02.01", "2023-01-25T15:00:00", 20, "MINUTES")
        todo.addAction(id, "Action 02.02", "2023-01-25T15:20:00", 20, "MINUTES")
        todo.addAction(id, "Action 02.03", "2023-01-25T15:40:00", 20, "MINUTES")

        id = todo.addTask("Task 03", "2023-01-25T16:00:00", 1, "HOURS")
        todo.addAction(id, "Action 03.01", "2023-01-25T16:00:00", 20, "MINUTES")
        todo.addAction(id, "Action 03.02", "2023-01-25T16:20:00", 20, "MINUTES")
        todo.addAction(id, "Action 03.03", "2023-01-25T16:40:00", 20, "MINUTES")

        Event event = todo.getCurrent(LocalDateTime.of(2023, 01, 25, 13, 0, 0))
        assertNull(event)

        event = todo.getCurrent(LocalDateTime.of(2023, 01, 25, 14, 0, 0))
        assertNotNull(event)
        assertEquals("Task 01",      event.taskName)
        assertEquals("Action 01.01", event.actionName)

        event = todo.getCurrent(LocalDateTime.of(2023, 01, 25, 16, 41, 0))
        assertNotNull(event)
        assertEquals("Task 03",      event.taskName)
        assertEquals("Action 03.03", event.actionName)


    }

}
