package sparklab.tts.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class TimeTrackerDTO {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalTime totalDuration; // Assuming you want to represent duration as a string
    private Long taskId;
    private Long userId;
}
