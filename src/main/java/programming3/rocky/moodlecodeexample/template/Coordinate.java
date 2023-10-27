package programming3.rocky.moodlecodeexample.template;

import java.time.LocalDateTime;

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
