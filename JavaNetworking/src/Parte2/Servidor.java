package Parte2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
	
	public static void main(String [] args) {
		ExecutorService pool=Executors.newCachedThreadPool();
		try(ServerSocket servidor = new ServerSocket(8080);){

			while (true) {

				Socket client = servidor.accept();
				pool.execute(new AtenderPeticion(client));
				
				
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally {
			pool.shutdown();
		}
		
		
		
		
		
	}

}
