package sample.CommunicationHandler;

import java.net.InetAddress;

public class ReceivingPeer {
    private InetAddress IP;
    private int port;

    public ReceivingPeer(InetAddress address,int port){
        this.setIP(address);
        this.setPort(port);
    }

    public InetAddress getIP() {
        return IP;
    }

    public void setIP(InetAddress IP) {
        this.IP = IP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
