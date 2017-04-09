import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Server2 extends ServerSocket {

    private static final int SERVER_PORT = 2018;

    private static boolean isPrint = false;// �Ƿ������Ϣ��־
    private static List user_list = new ArrayList();// ��¼�û�����
    private static List<ServerThread> thread_list = new ArrayList<ServerThread>();// �������������̼߳���
    private static LinkedList message_list = new LinkedList();// �����Ϣ����

    /**
     * ���������Socket,������ͻ��˷�����Ϣ�߳�,�����ͻ������󲢴���
     */
    public Server2() throws IOException {
        super(SERVER_PORT);// ����ServerSocket
        new PrintOutThread();// ������ͻ��˷�����Ϣ�߳�

        try {
            while (true) {// �����ͻ������������̴߳���
                Socket socket = accept();
                new ServerThread(socket);
            }
        } catch (Exception e) {
        } finally {
            close();
        }
    }

    /**
     * �����Ƿ��������Ϣ�����߳���,��ͻ��˷�����Ϣ
     */
    class PrintOutThread extends Thread {

        public PrintOutThread() {
            start();
        }

        @Override
        public void run() {
            while (true) {
				//û�д�ӡ��䣬if�������䲻��ִ�У������Ƕ��̷߳���isPrint��ɵ�
				System.out.println("�����С�����"+isPrint);
                if (isPrint) {// �������ڶ����е���Ϣ��˳���͵����ͻ��ˣ����Ӷ����������
                    String message = (String) message_list.getFirst();
                    for (ServerThread thread : thread_list) {
                        thread.sendMessage(message);
                    }
                    message_list.removeFirst();
                    isPrint = message_list.size() > 0 ? true : false;
                }
            }
        }
    }

    /**
     * �������߳���
     */
	@SuppressWarnings("unchecked")
    class ServerThread extends Thread {
        private Socket client;
        private PrintWriter out;
        private BufferedReader in;
        private String name;

        public ServerThread(Socket s) throws IOException {
            client = s;
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            //in.readLine();
            out.println("�ɹ�����������,������������֣�");
            start();
        }

        @Override
        public void run() {
            try {
                int flag = 0;
                String line = in.readLine();
                while (true) {
                    // �鿴�����û��б�
                    if ("showuser".equals(line)) {
                        out.println(this.listOnlineUsers());
                    }
					if("bye".equals(line)){
						out.println("bye");
					break;}
                    // ��һ�ν��룬��������
                    if (flag++ == 0) {
                        name = line;
                        user_list.add(name);
                        thread_list.add(this);
                        out.println(name + "���,���Կ�ʼ������...");
                        this.pushMessage("Client<" + name + ">����������...");
                    } else {
                        this.pushMessage("Client<" + name + "> say : " + line);
                    }
                    line = in.readLine();
					
                }
                out.println("byeClient");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {// �û��˳�������
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                thread_list.remove(this);
                user_list.remove(name);
                pushMessage("Client<" + name + ">�˳���������");
            }
        }

        // ������Ϣ����ĩβ��׼�����͸��ͻ���
        private void pushMessage(String msg) {
            message_list.addLast(msg);
            isPrint = true;
        }

        // ��ͻ��˷���һ����Ϣ
        private void sendMessage(String msg) {
            out.println(msg);
        }

        // ͳ�������û��б�
        private String listOnlineUsers() {
            String s = "--- �����û��б� ---\015\012";
            for (int i = 0; i < user_list.size(); i++) {
                s += "[" + user_list.get(i) + "]\015\012";
            }
            s += "--------------------";
            return s;
        }
    }

    public static void main(String[] args) throws IOException {
        new Server2();// ���������
    }
}