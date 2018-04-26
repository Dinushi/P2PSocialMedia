package sample.Model;

import sample.Controller.HomeController;
import sample.DBHandler.DbHandler;
import sample.EventHandler.PostHandler;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Post implements Serializable {

    private  String username;
    private String content;
    private LocalDateTime date_created;
    private ArrayList<Reply> replies;
    private int post_id;
    private static final long serialVersionUID = 1L;
    private byte[] post_image;
    //images are not yet included

    public Post(String username,String content,int post_id ){
        this.setReplies(new ArrayList<Reply>());//a post can have multiple replies
        this.setContent(content);
        this.setUsername(username);//the post created by this user also marked under his username
        this.post_id=post_id;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        setDate_created(now);
        //setPostId();

    }
    public void setPostId(){
        DbHandler db=new DbHandler();
        int post_id=db.getMyMaxPostID()+1;
        db.closeConnection();
    }
    public int getPostID(){
        return post_id;
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
        DbHandler db=new DbHandler();
        boolean result=db.addNewReply(reply);
        if(result){
            System.out.println("Sucessfully stored the reply");
        }else{
            System.out.println("could not store the reply");
        }
        db.closeConnection();
    }

    public boolean removeReply(Reply reply){
        this.getReplies().remove(reply);
        return true;
    }
    public void notifyController(){
        System.out.println("came to notify controller");
        System.out.println("usename from post cladss"+ this.getUsername());
        System.out.println("content from post cladss"+ this.getContent());
        HomeController.homeController.showNewPost(this);
        //hc.showNewPost(this);

    }
    public void sendPost(){
        PostHandler.sentThePostToPeers(this);
        //new PeerConnection().sendViaSocket(this);
    }

    public void setDate_created(LocalDateTime date_created) {
        this.date_created = date_created;
    }

    public static ArrayList<Post> selectAllPosts(){
        DbHandler db=new DbHandler();

        ArrayList<Post> allPosts=db.getPosts(false,null);
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
        Reply last_reply=null;
        if (replies.size()>0) {
            last_reply = replies.get(replies.size() - 1);
        }else{
            DbHandler db=new DbHandler();
            replies=db.getReplies(this.getPostID());
            db.closeConnection();
            if (replies.size()>0) {
                last_reply = replies.get(replies.size() - 1);
            }
        }
        return last_reply;
    }

    public void addToDb(){
        DbHandler db=new DbHandler();
        db.addNewPost(this);
        db.closeConnection();

    }
    public void sendReply(Reply reply){
        System.out.println("At post.Ready to call post Handler to sent the reply");
        PostHandler.sendReplyToPeersIntersted(reply);
    }

    public byte[] getPost_image() {
        return post_image;
    }

    public void setPost_image(byte[] post_image) {
        this.post_image = post_image;
    }
}
