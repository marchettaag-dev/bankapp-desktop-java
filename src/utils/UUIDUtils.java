package utils;

import java.util.UUID;

public class UUIDUtils {
	
	public static UUID colocarGuiones(String sinGuiones) {
		
		String conGuiones = sinGuiones.substring(0,8) + "-" +
							sinGuiones.substring(8,12)+ "-" +
							sinGuiones.substring(12,16) + "-" +
							sinGuiones.substring(16,20)+ "-" +
							sinGuiones.substring(20);
		
		return UUID.fromString(conGuiones);
	}
}
