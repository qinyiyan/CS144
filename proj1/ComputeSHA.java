import java.io.*;
import java.security.*;

public class ComputeSHA
{
	public static void main (String [] args) throws IOException
	{
		File file;
		try 
		{
			String filename = args[0];
			file = new File (args[0]);
			byte [] buffer = new byte [(int) file.length()];
				FileInputStream line = new FileInputStream(file);
				line.read (buffer);
				line.close();


			//compute checksum
			//MessageDigest md;
				MessageDigest md = MessageDigest.getInstance ("SHA1");
				md.update (buffer);
				byte [] hash_result = md.digest();
				String hex_result = bytesToHex(hash_result);
				System.out.println (hex_result);
		}
		catch (FileNotFoundException error0)
		{
			System.out.println("file not found");
			System.exit(-1);
		}
		catch (NoSuchAlgorithmException error1)
		{
			System.out.println ("SHA-1 algorithm not defined");
			error1.getStackTrace();
		}
	}

/*
		public static String bytesToHex (byte [] bytes)
		{
			static char[] hexArray = "0123456789ABCDEF".toCharArray();
			char[] hexChars = new char[bytes.length *2];
			int v;
			for (int j=0; j<bytes.length;j++)
			{
				v = bytes[j] & 0xFF;
				hexChars[j *2] = hexArray[v>>>4];
				hexChars[j*2+1] = hexArray [v & 0x0F];
			}
			return new String(hexChars);
		}
		//credited to http://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
		*/
		public static String bytesToHex (byte[] bytes)
		{
			StringBuilder sbuilder = new StringBuilder (bytes.length *2);
			for (byte b:bytes)
				sbuilder.append (String.format ("%02x", b &0xFF));
			return sbuilder.toString();
		}

}