import java.io.*;
import java.net.*;

public class Server1 extends Thread {//继承Thread类
	private Socket client;
	
	public Server1(Socket c){//类初始化，接受参数为客户端的请求
		this.client = c;
	}
	
	public void run(){//重写run函数
		try{
			BufferedReader in=
				new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out=new PrintWriter(client.getOutputStream());
			while(true){
				String str=in.readLine();
				System.out.println(str);
				out.println("has receive....");
				out.flush();
				if(str.equals("end"))
					break;   
			}   
			client.close();
		}catch(IOException ex){
		}finally{
		}
	}
	
	public static void main(String[] args) throws IOException{
		ServerSocket server=new ServerSocket(5678);
		while(true){
			Server1 mu=new Server1(server.accept());//每当有客户端请求就新建一个Server类与之通信
			mu.start();
		}
	}   
}