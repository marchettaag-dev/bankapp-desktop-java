package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
	
	public static String hashear(String texto) {
		
		try {
			
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(texto.getBytes(StandardCharsets.UTF_8));
			
			// Convertir los bytes a String hexadecimal
			StringBuilder sb = new StringBuilder();
			
			for(byte b : hash) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
			
		}catch(NoSuchAlgorithmException e) {
			 throw new RuntimeException("Error al hashear", e);
		}
	}
}
