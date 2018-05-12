package sample.Controller;

import javafx.event.ActionEvent;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.*;
/*
@RunWith(Parameterized.class)
*/
public class RegisterControllerTest {
    RegisterController rc;
    /*
        @Parameterized.Parameters
        public static Object[] validUsernameProvider() {
           /*create and return a Collection
             of Objects arrays here.
             Each element in each array is
             a parameter to your constructor.
            */
    /*
            return new Object[][]{
                    {new String[] {
                            "mkyong34", "mkyong_2002","mkyong-2002" ,"mk3-4_yong"
                    }}
            };

        }
        */
/*
        private RegisterController rc;
        private String username;
        private String password;



        public RegisterControllerTest (String a,String b, String c) {
            username=a;
            password=b;

        }


        @Test
        public void test() {
            //do your test with a,b
        }

        @Test
        public void testC(){
            //you can have multiple tests
            //which all will run

            //...test c
        }
        */

/*
    @DataProvider
    public Object[][] ValidUsernameProvider() {
        return new Object[][]{
                {new String[] {
                        "mkyong34", "mkyong_2002","mkyong-2002" ,"mk3-4_yong"
                }}
        };
    }

    @DataProvider
    public Object[][] InvalidUsernameProvider() {
        return new Object[][]{
                {new String[] {
                        "mk","mk@yong","mkyong123456789_-"
                }}
        };
    }
*/
/*
    @Test(dataProvider = "ValidUsernameProvider")
    public void ValidUsernameTest(String[] Username) {

        for(String temp : Username){
            boolean valid = usernameValidator.validate(temp);
            System.out.println("Username is valid : " + temp + " , " + valid);
            Assert.assertEquals(true, valid);
        }

    }

    @Test(dataProvider = "InvalidUsernameProvider",
            dependsOnMethods="ValidUsernameTest")
    public void InValidUsernameTest(String[] Username) {

        for(String temp : Username){
            boolean valid = usernameValidator.validate(temp);
            System.out.println("username is valid : " + temp + " , " + valid);
            Assert.assertEquals(false, valid);
        }

    }*/

    @Before
    public void setRequiredParameters(){
        rc=new RegisterController();
    }


    @Test
    public void validateTheUserData() {
        boolean result=rc.validateTheUserData("Thilini Dinushika","Thilini123","Akuressa","Dinu_123","Dinu_123");
        assertEquals(true,result);

    }

    @Test
    public void validatePassword() {
        boolean result3=rc.validatePassword("Dinu_123");
        assertEquals(true,result3);

        //boolean result2=rc.validatePasswordEntered("Dinu_123","Dinu_123");
        //assertEquals(true,result2);
    }

    @Test
    public void validateUsername() {
        boolean result=rc.validateUsername("Thilini_123");
        assertEquals(true,result);

    }

    @Test
    public void validateFullName() {
        boolean result3=rc.validateFullName_HomeTown("Thilini Dinushika",true);
        assertEquals(true,result3);

    }
    @Test
    public void validateHomeTown() {
        boolean result3=rc.validateFullName_HomeTown("Matara",false);
        assertEquals(true,result3);

    }
}