package sample.Model;

import sample.DBHandler.DbHandler;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reply implements Serializable {
    private String reply_creator;

    private  String username;//this username means the actual owener of the post
    private String content;
    private LocalDateTime date_created;
    private int post_id;
    private int reply_id;


    public Reply(String username,String content,int post_id,int reply_id ){
        this.content=content;
        this.username=username;//the post created by this user also marked under his username
        this.post_id=post_id;
        this.reply_id=reply_id;
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
    public void setDate_created(LocalDateTime date_created) {
        this.date_created = date_created;
    }

    public int getPost_id() {
        return post_id;
    }


    public int getReply_id() {
        return reply_id;
    }
    public static int getNextReplyId(int post_id){
        DbHandler db=new DbHandler();
        int nxt_reply_id=db.getMyMaxReplyID(post_id)+1;
        db.closeConnection();
        return nxt_reply_id;
    }


    public String getReply_creator() {
        return reply_creator;
    }

    public void setReply_creator(String reply_creator) {
        this.reply_creator = reply_creator;
    }
}
