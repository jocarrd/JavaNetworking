package Parte1;

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

		try {
			URL url = new URL(r);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("HEAD");
			long content_length = con.getContentLengthLong();
			
			for(int i=0;i<3;i++) {
				
				Descargador d = new Descargador();
				
				pool.execute(d);
			}
			
			

		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
