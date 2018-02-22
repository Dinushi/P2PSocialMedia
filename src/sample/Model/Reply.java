package sample.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reply {

    private  String username;
    private String content;
    private LocalDateTime date_created;

    public Reply(String username,String content ){
        this.content=content;
        this.username=username;//the post created by this user also marked under his username
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.date_created=now;

    }


    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public void editContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate_created() {
        return date_created;
    }

}
