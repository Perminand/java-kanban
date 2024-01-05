package kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class FreeTimeManagement {
    public Duration duration;
    public int idTask;
    public LocalDateTime startDuration;
    public LocalDateTime endDuration;

    public FreeTimeManagement(int idTask, LocalDateTime startDuration, LocalDateTime endDuration) {
        this.idTask = idTask;
        this.startDuration = startDuration;
        this.endDuration = endDuration;
    }
}
