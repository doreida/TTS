package sparklab.tts.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sparklab.tts.dto.TaskStatusDTO;
import sparklab.tts.dto.TimeTrackerDTO;
import sparklab.tts.model.TimeTracker;

import java.time.Duration;

@Component
public class TimeTrackerToTimeTrackerDTO implements Converter<TimeTracker, TimeTrackerDTO> {

    @Override
    public TimeTrackerDTO convert(TimeTracker source) {
        if(source !=null){
            TimeTrackerDTO timeTrackerDTO = new TimeTrackerDTO();
            timeTrackerDTO.setId(source.getId());
            timeTrackerDTO.setStartDate(source.getStartDate());
            timeTrackerDTO.setEndDate(source.getEndDate());
            timeTrackerDTO.setTotalDuration(source.getTotalTimePerTask());
            timeTrackerDTO.setTaskId(source.getTask().getId());
            timeTrackerDTO.setUserId(source.getUser().getId());
            return  timeTrackerDTO;

        }
        return null;
    }

    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
