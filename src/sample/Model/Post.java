package sample.Model;

import sample.CommunicationHandler.PeerConnection;
import sample.Controller.HomeController;
import sample.DBHandler.DbHandler;

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
        this.setReplies(new ArrayList<Reply>());//a post can have multiple replies
        this.setContent(content);
        this.setUsername(username);//the post created by this user also marked under his username
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        setDate_created(now);

    }

    public String getContent() {
        return content;
    }

    public void editContent(String content) {
        this.setContent(content);
    }

    public LocalDateTime getDate_created() {
        return date_created;
    }

    public void addReply(Reply reply){
        this.getReplies().add(reply);
    }
    public boolean removeReply(Reply reply){
        this.getReplies().remove(reply);
        return true;
    }
    public void notifyController(){
        System.out.println("came to notify controller");
        System.out.println("usename from post cladss"+ this.getUsername());
        System.out.println("content from post cladss"+ this.getContent());
        HomeController hc=new HomeController();
        hc.showNewPost(this);

    }
    public void sendPost(){
        new PeerConnection().sendViaSocket(this);
    }

    public void setDate_created(LocalDateTime date_created) {
        this.date_created = date_created;
    }
    public static ArrayList<Post> selectAllPosts(){
        DbHandler db=new DbHandler();
        ArrayList<Post> allPosts=db.getPosts();
        db.closeConnection();
        return allPosts;


    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }
    public Reply getLastReply(){
        return new Reply("Thilini123","well done boys");
        //return replies.get(replies.size()-1);
    }
    public void addToDb(){
        DbHandler db=new DbHandler();
        db.addNewPost(this);

    }
}
