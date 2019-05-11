package subirimagenservidorremoto;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

/**
 *
 * @author JMalagon
 */
public class Post {

	public Post() {
	}

	public void postFile(File binaryFile, String imageNameOnServer) {
		try {
			String url = "http://localhost/SubirImagenServidorRemoto-Server/index.php";
			String charset = "UTF-8";
			String limite = Long.toHexString(System.currentTimeMillis()); // Genera un id unico obteniendo la fecha en
																			// milisengundos.
			String CRLF = "\r\n"; // salto de linea para separar (obligatorio en multipart/form-data)

			URLConnection connection = new URL(url).openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + limite);
			OutputStream output = connection.getOutputStream();
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

			// Enviar parámetro normal (textos).
			writer.append("--" + limite).append(CRLF);
			writer.append("Content-Disposition: form-data; name=\"imgname\"").append(CRLF);
			writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
			writer.append(CRLF).append(imageNameOnServer).append(CRLF).flush();

			// Enviar fichero binario (imagen).
			writer.append("--" + limite).append(CRLF);
			writer.append("Content-Disposition: form-data; name=\"archivo\"; filename=\"" + binaryFile.getName() + "\"")
					.append(CRLF);
			writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
			writer.append("Content-Transfer-Encoding: binary").append(CRLF);
			writer.append(CRLF).flush();
			Files.copy(binaryFile.toPath(), output);
			output.flush(); // Enviar todos los datos almacenados en la clase por si queda alguno
			writer.append(CRLF).flush(); // CRLF es importante! Indica la finalización del limite.

			// Finalización del multipart/form-data.
			writer.append("--" + limite + "--").append(CRLF).flush();

			// Obtenemos respuesta del servidor
			int responseCode = ((HttpURLConnection) connection).getResponseCode();
			System.out.println(responseCode); // Si es correcto debe ser el 200
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			// Mostramos la respuesta del servidor por consola
			System.out.println(response);
			// cerramos la conexión
			in.close();
		} catch (MalformedURLException ex) {
			System.out.println("subirimagenservidorremoto.Post.postFile() : " + ex);
		} catch (IOException ex) {
			System.out.println("subirimagenservidorremoto.Post.postFile() : " + ex);
		}
	}
}
// Send text file.
/*
 * String param = "value"; File textFile = new File("/path/to/file.txt");
 * writer.append("--" + limite).append(CRLF); writer.
 * append("Content-Disposition: form-data; name=\"textFile\"; filename=\"" +
 * textFile.getName() + "\"").append(CRLF);
 * writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
 * // Text file itself must be saved in this charset!
 * writer.append(CRLF).flush(); Files.copy(textFile.toPath(), output);
 * output.flush(); // Important before continuing with writer!
 * writer.append(CRLF).flush(); // CRLF is important! It indicates end of
 * limite.
 */