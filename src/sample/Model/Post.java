package sample.Model;

import sample.Controller.HomeController;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Post implements Serializable {

    private  String username;
    private String content;
    private LocalDateTime date_created;
    private ArrayList<Reply> replies;
    private static final long serialVersionUID = 1L;
    //images are not yet included

    public Post(String username,String content ){
        this.replies=new ArrayList<Reply>();//a post can have multiple replies
        this.content=content;
        this.username=username;//the post created by this user also marked under his username
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        date_created=now;

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

    public void addReply(Reply reply){
        this.replies.add(reply);
    }
    public boolean removeReply(Reply reply){
        this.replies.remove(reply);
        return true;
    }
    public void notifyController(){
        HomeController hc=new HomeController();
        hc.showNewPost(this);
    }
}
