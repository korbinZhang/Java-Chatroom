import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client2 extends Socket{

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 2018;
    
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    
    /**
     * ����������ӣ������뷢����Ϣ
     */
    public Client2() throws Exception{
        super(SERVER_IP, SERVER_PORT);
        client = this;
        out = new PrintWriter(this.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(this.getInputStream()));
        new readLineThread();
        
        while(true){
            in = new BufferedReader(new InputStreamReader(System.in));
            String input = in.readLine();
            out.println(input);
        }
    }
    
    /**
     * ���ڼ�������������ͻ��˷�����Ϣ�߳���
     */
    class readLineThread extends Thread{
        
        private BufferedReader buff;
        public readLineThread(){
            try {
                buff = new BufferedReader(new InputStreamReader(client.getInputStream()));
                start();
            } catch (Exception e) {
            }
        }
        
        @Override
        public void run() {
            try {
                while(true){
                    String result = buff.readLine();
                    if("byeClient".equals(result)){//�ͻ��������˳�������˷���ȷ���˳�
                        break;
                    }else{//�������˷�����Ϣ
                        System.out.println(result);
                    }
                }
                in.close();
                out.close();
                client.close();
            } catch (Exception e) {
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            new Client2();//�����ͻ���
        } catch (Exception e) {
        }
    }
}