package Parte1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Descargador implements Runnable {

	private String recurso;
	private File dir;
	private long inicial;
	private long fin;

	public Descargador(String recurso, File dir, long inicial, long fin) {
		this.recurso = recurso;
		this.dir = dir;
		this.inicial = inicial;
		this.fin = fin;

	}

	public void run() {
		URL u;
		try {
			u = new URL(recurso);
			HttpURLConnection con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("GET");
			String d = "bytes=" + this.inicial + "-" + this.fin;
			con.addRequestProperty("Range", d);
			InputStream server = con.getInputStream();

			RandomAccessFile sr = new RandomAccessFile(this.dir.getPath(), "rw");
			sr.seek(this.inicial);

			int c = 0;
			byte g[] = new byte[1000];

			while ((c = server.read(g)) != -1) {
				sr.write(g);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
