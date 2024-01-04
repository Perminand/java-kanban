package kanban.comparator;

import kanban.model.Task;

import java.util.Comparator;

public class DateTimeComparator implements Comparator<Task> {

    @Override
    public int compare(Task dt1, Task dt2) {
        if (dt1.getStartTime() == null && dt2.getStartTime() == null)
            return 0;
        else {
            if (dt1.getStartTime() == null && dt2.getStartTime() != null)
                return +1;
            else {
                if (dt1.getStartTime() != null && dt2.getStartTime() == null)
                    return -1;
                if (dt1.getStartTime().getDayOfMonth() != dt2.getStartTime().getDayOfMonth()) {
                    return Integer.compare(dt1.getStartTime().getDayOfYear(), dt2.getStartTime().getDayOfYear());
                } else {
                    if (dt1.getStartTime().getHour() != dt2.getStartTime().getHour()) {
                        return Integer.compare(dt1.getStartTime().getHour(), dt2.getStartTime().getHour());
                    } else {
                        if (dt1.getStartTime().getMinute() != dt2.getStartTime().getMinute()) {
                            return Integer.compare(dt1.getStartTime().getMinute(), dt2.getStartTime().getMinute());
                        } else {
                            if (dt1.getStartTime().getSecond() != dt2.getStartTime().getSecond()) {
                                return Integer.compare(dt1.getStartTime().getSecond(), dt2.getStartTime().getSecond());
                            } else {
                                return Integer.compare(dt1.getStartTime().getNano(), dt2.getStartTime().getNano());
                            }
                        }
                    }
                }
            }

        }
    }
}