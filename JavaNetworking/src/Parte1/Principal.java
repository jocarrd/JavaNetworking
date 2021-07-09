package Parte1;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Principal {

	public static ExecutorService pool = Executors.newCachedThreadPool();

	public static void main(String[] args) {
		Scanner entrada = new Scanner(System.in);
		System.out.println("Introduzca la URL del destino a descargar");
		String r = entrada.nextLine();
		File dir;
		CountDownLatch countdown = new CountDownLatch(3);
		do {
		System.out.println("Introduza el nombre del directorio donde desea guardarlo");
		String path = entrada.nextLine();
		 dir = new File(path);
		}while(!dir.exists());
		
		try {
			URL url = new URL(r);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("HEAD");
		
			long content_length = con.getContentLengthLong();
			
			for(int i=0;i<3;i++) {
				
				Descargador d=null;
				
				if(i==0) {
					 d = new Descargador(r,dir,0,(content_length/3),countdown);
					 System.out.println("Byte inicial :  "+0);
						System.out.println("Byte final :  "+(content_length/3));
				}
				if(i==1 ) {
					 d = new Descargador(r,dir,i*(content_length/3)+1,i+1*(content_length/3),countdown);
					System.out.println("Byte inicial :  "+(i*(content_length/3)+1));
					System.out.println("Byte final :  "+((i+1)*(content_length/3)));
				}
				
				if(i==2) {
					 d = new Descargador(r,dir,i*(content_length/3)+1,content_length-1,countdown);
					System.out.println("Byte inicial :  "+(i*(content_length/3)+1));
					System.out.println("Byte final :  "+content_length);
				}
				
				
				System.out.println("Total : "+content_length);
				
				pool.execute(d);
			}
			
			System.out.println("Esperando a que terminen");
			countdown.await();
			
			System.out.println("Descarga finalizada");
			
			pool.shutdown();
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
