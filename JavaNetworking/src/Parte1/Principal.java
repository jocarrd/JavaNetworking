package Parte1;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Principal {

	public static ExecutorService pool = Executors.newCachedThreadPool();

	public static void main(String[] args) {
		Scanner entrada = new Scanner(System.in);
		System.out.println("Introduzca la URL del destino a descargar");
		String r = entrada.nextLine();
		File dir;
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
				
				Descargador d;
				if(i==2) {
					 d = new Descargador(r,dir,i*(content_length/3),content_length);
					System.out.println("Byte inicial :  "+(i*(content_length/3)));
					System.out.println("Byte final :  "+((i+1)*(content_length/3)));
				}else {
					 d = new Descargador(r,dir,i*(content_length/3),i+1*(content_length/3));
					System.out.println("Byte inicial :  "+(i*(content_length/3)));
					System.out.println("Byte final :  "+((i+1)*(content_length/3)));
					
				}
				
				System.out.println("Total : "+content_length);
				
				pool.execute(d);
			}
			
			

			
			
			
			pool.shutdown();
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
