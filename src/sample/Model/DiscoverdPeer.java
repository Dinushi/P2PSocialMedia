package sample.Model;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;

//this is used for the bs serverconnection.Send a serialized object and accept as well
public class DiscoverdPeer implements Serializable {
    private String msg;
    private String username;
    private InetAddress ip;
    private int port;

    public DiscoverdPeer(String msg,String username,InetAddress ip,int port){
        this.setMsg(msg);
        this.setUsername(username);
        this.setIp(ip);
        this.setPort(port);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof DiscoverdPeer)) {
            return false;
        }
        DiscoverdPeer peer = (DiscoverdPeer) o;
        return username == peer.username &&
                Objects.equals(ip, peer.getIp()) &&
                Objects.equals(port, peer.getPort());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, ip, port);
    }
}
