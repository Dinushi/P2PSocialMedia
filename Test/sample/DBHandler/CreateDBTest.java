package sample.DBHandler;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreateDBTest {
    CreateDB db;

    @Before
    public void init(){
        db=new CreateDB();
    }

    @Test
    public void create_table_POST() {
        db.create_table_POST();
    }

    @Test
    public void build_table_REPLY() {
        db.build_table_REPLY();
    }

    @Test
    public void build_table_CONVERSATION() {
        db.build_table_CONVERSATION();
    }

    @Test
    public void build_table_CHAT() {
        db.build_table_CHAT();
    }

    @Test
    public void build_table_MESSAGE() {
        db.build_table_MESSAGE();
    }
}