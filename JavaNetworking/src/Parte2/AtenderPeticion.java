package Parte2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URLConnection;
import java.util.Date;

public class AtenderPeticion implements Runnable {

	Socket client;
	public AtenderPeticion(Socket client) {
		this.client = client;

	}
	public void run() {

		try(DataInputStream str = new DataInputStream(this.client.getInputStream());
				DataOutputStream sal = new DataOutputStream(this.client.getOutputStream());) {
			
			String primera_linea = str.readLine();

			File d = this.buscaFichero(primera_linea);
			if (d.exists()) {
				this.sendMIMEHeading(sal, 200, URLConnection.guessContentTypeFromName(d.getName()), d.length());
				FileInputStream archivo = new FileInputStream(d);
				
				byte g[] = new byte[1000];
				int c =0;
				while ((c = archivo.read(g)) != -1) {
					sal.write(g,0,c);
				}
				sal.flush();
			}else{
				String f = this.makeHTMLErrorText(404, "ERROR");
				this.sendMIMEHeading(sal, 404, URLConnection.guessContentTypeFromName(f), f.length());
				sal.writeBytes(f);		
			}	

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private File buscaFichero(String m) {
		 String fileName="";
		 if (m.startsWith("GET ")){
		 // A partir de una cadena de mensaje (m) correcta (comienza por GET)
		 fileName = m.substring(4, m.indexOf(" ", 5));
		 if (fileName.equals("/")) {
		 fileName += "index.html";
		 }}
		 if (m.startsWith("HEAD ")){
		 // A partir de una cadena de mensaje (m) correcta (comienza por HEAD)
		 fileName = m.substring(6, m.indexOf(" ", 7));
		 if (fileName.equals("/")) {
		 fileName += "index.html";

} }
		 return new File(".\\TrabajoHTML", fileName);
	 }
	
	private void sendMIMEHeading(OutputStream os, int code, String cType, long fSize) {
		 PrintStream dos = new PrintStream(os);
		 dos.print("HTTP/1.1 " + code + " ");
		 if (code == 200) {
		 dos.print("OK\r\n");
		 dos.print("Date: " + new Date() + "\r\n");
		 dos.print("Server: Cutre http Server ver. -6.0\r\n");
		 dos.print("Connection: close\r\n");
		 dos.print("Content-length: " + fSize + "\r\n");
		 dos.print("Content-type: " + cType + "\r\n");
		 dos.print("\r\n");
		 } else if (code == 404) {
		 dos.print("File Not Found\r\n");
		 dos.print("Date: " + new Date() + "\r\n");
		 dos.print("Server: Cutre http Server ver. -6.0\r\n");
		 dos.print("Connection: close\r\n");
		 dos.print("Content-length: " + fSize + "\r\n");
		 dos.print("Content-type: " + "text/html" + "\r\n");
		 dos.print("\r\n");
		 } else if (code == 501) {
		 dos.print("Not Implemented\r\n");
		 dos.print("Date: " + new Date() + "\r\n");
		 dos.print("Server: Cutre http Server ver. -6.0\r\n");
		 dos.print("Connection: close\r\n");
		 dos.print("Content-length: " + fSize + "\r\n");
		 dos.print("Content-type: " + "text/html" + "\r\n");
		 dos.print("\r\n");
		 }
		 dos.flush();
		 }
		 private String makeHTMLErrorText(int code, String txt) {
		 StringBuffer msg = new StringBuffer("<HTML>\r\n");
		 msg.append(" <HEAD>\r\n");
		 msg.append(" <TITLE>" + txt + "</TITLE>\r\n");
		 msg.append(" </HEAD>\r\n");
		 msg.append(" <BODY>\r\n");
		 msg.append(" <H1>HTTP Error " + code + ": " + txt + "</H1>\r\n");
		 msg.append(" </BODY>\r\n");
		 msg.append("</HTML>\r\n");
		 return msg.toString();
		}	
}
	
