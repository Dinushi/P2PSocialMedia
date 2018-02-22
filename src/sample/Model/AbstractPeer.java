package sample.Model;

import java.net.InetAddress;

public class AbstractPeer {

        protected String username;
        protected InetAddress ip;
        protected int port;
        protected   String fullname;
        protected   String status;
        protected   String gender;
        private  String bday;
        //private String prof_pic;
        protected   String hometown;


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

        public String getBday() {
            return bday;
        }

        public void setBday(String bday) {
            this.bday = bday;
        }

        public String getHometown() {
            return hometown;
        }

        public void setHometown(String hometown) {
            this.hometown = hometown;
        }
    }

