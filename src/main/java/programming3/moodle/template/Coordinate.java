package programming3.moodle.template;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Coordinate {
    
    private LocalDateTime time;
    private String username;
    private Double longitude;
    private Double latitude;
    private String description;

    public Coordinate (LocalDateTime timeSent, String username, Double latitude, Double longitude){

        this.time = timeSent;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;

    }


    public void setDescription(String desc){
        this.description = desc;
    }

    public String getDescription(){
        return this.description;
    }


}
