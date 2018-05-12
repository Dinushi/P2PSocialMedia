package sample.Controller;

import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.*;

public class ValidatorTest {
    Validator val;
    @Before
    public void init(){
        val=new Validator();

    }

    @Test
    public void validateUser() {
        String username_enterd="Boo";
        String password_enterd="123";
        int i=val.validateUser(username_enterd,password_enterd);
        assertEquals(0,i);
    }
    @Test
    public void validateUserWithIncorrectPassword() {
        String username_enterd="Boo";
        String password_enterd="1234";
        int i=val.validateUser(username_enterd,password_enterd);
        assertEquals(1,i);
    }
    @Test
    public void validateUserWithIncorrectUsername() {
        String username_enterd="Boo00";
        String password_enterd="123";
        int i=val.validateUser(username_enterd,password_enterd);
        assertEquals(2,i);
    }

    @Test
    public void getMyPort() {
        int port=val.getMyPort();

    }

    @Test
    public void getMyIP() {
        InetAddress ip=val.getMyIp();

    }
}