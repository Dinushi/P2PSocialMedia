package sample.Model;

import java.net.InetAddress;
import java.util.Date;

public class AbstractPeer {

        protected static String username;
        protected static InetAddress ip;
        protected static int port;
        private String fullname;
        protected   String status;
        protected   String gender;
        private Date bday;
        //private String prof_pic;
        private String hometown;


        public AbstractPeer(String username,InetAddress ip,int port){
            this.username=username;
            this.ip=ip;
            this.port=port;
        }


        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public InetAddress getIp() {
            return ip;
        }

        public int getPort() {
            return port;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Date getBday() {
            return bday;
        }

        public void setBday(Date bday) { this.bday = bday;
        }

        public String getHometown() {
            return hometown;
        }

        public void setHometown(String hometown) {
            this.hometown = hometown;
        }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}

