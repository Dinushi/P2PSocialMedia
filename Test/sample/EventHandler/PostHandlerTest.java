package sample.EventHandler;

import org.junit.Before;
import org.junit.Test;
import sample.CommunicationHandler.ReceivingPeer;
import sample.DBHandler.DbHandler;
import sample.Model.Post;
import sample.Model.Reply;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class PostHandlerTest {

    @Test
    public  void gotAreply() throws UnknownHostException {
        Post p=new Post("Ami","Hii akkk",2);
        DbHandler db=new DbHandler();
        db.addNewPost(p);
        Reply reply=new Reply("Ami","Oh really",2,1);
        reply.setReply_creator("Ami");
        PostHandler.gotAReply(reply, InetAddress.getByName("10.10.22.212"),13099);

        Post post=db.checkTheAvailabilityOfAPost("Ami",reply.getPost_id());
        db.closeConnection();
        assertNotNull(post);
        db.closeConnection();


    }


}