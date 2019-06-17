import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

public class MyGlobals {
    private int page;
    private static MyGlobals instance = null;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    private Socket socket;

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    private String nick_name;

    public BufferedReader getNetworkReader() {
        return networkReader;
    }

    public void setNetworkReader(BufferedReader networkReader) {
        this.networkReader = networkReader;
    }

    public BufferedWriter getNetworkWriter() {
        return networkWriter;
    }

    public void setNetworkWriter(BufferedWriter networkWriter) {
        this.networkWriter = networkWriter;
    }

    private BufferedReader networkReader;
    private BufferedWriter networkWriter;

    public static synchronized MyGlobals getInstance(){
        if(null == instance){
            instance = new MyGlobals();
        }

        return instance;
    }
}
