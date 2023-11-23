import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class App {
	public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		BigInteger a = new BigInteger("2496428677436407289514354666495975348799");
		BigInteger p = new BigInteger("124325339146889384540494091085456630009856882741872806181731279018491820800119460022367403769795008250021191767583423221479185609066059226301250167164084041279837566626881119772675984258163062926954046545485368458404445166682380071370274810671501916789361956272226105723317679562001235501455748016154805420913");
		BigInteger g = new BigInteger("115740200527109164239523414760926155534485715860090261532154107313946218459149402375178179458041461723723231563839316251515439564315555249353831328479173170684416728715378198172203100328308536292821245983596065287318698169565702979765910089654821728828592422299160041156491980943427556153020487552135890973413");

		BigInteger bigA = g.modPow(a, p);
		
		System.out.println("Valor A em decimal:");
		System.out.println(bigA);
		
		String bigAHex = new BigInteger(bigA.toString(), 10)
				.toString(16)
				.toUpperCase();
		
		System.out.println("Valor A em hexadecimal:");
		System.out.println(bigAHex);
		
		BigInteger bigB = new BigInteger("101677889441950173874510062929228019414543458944286517369542507949693655659174725216217852930050162290602425933223224752312502568921609378432433094699305727424214027595511523295624296314112162027979008690354167142951399501515022077025643237112528252667207381950450418380453657370677035917408528283264162327240");
		
		BigInteger bigV = bigB.modPow(a, p);
		
		byte[] bigVArr = bigV.toByteArray();
		
		System.out.println("Valor V em decimal:");
		System.out.println(bigV);
		
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        
        md.update(bigVArr, 0, bigVArr.length);
        
        byte[] bigS = md.digest();

        byte[] pwd = Arrays.copyOfRange(bigS, 0, 16);
        
        String pwdHex = new BigInteger(bigS.toString(), 10)
				.toString(16)
				.toUpperCase();
        
        System.out.println("PWD:");
        System.out.println(pwdHex);

        String hexText = "1D4582F301DD70769DE185CA2BB5DE75375B4AB6FAEE995F8BFA576FBF54FE5F05292DE779494F09865B2288E16668749651424E5BFFA38708CA9D5162A92CD47F24FE00603B36D9170426134677E42C4458B98215A87D204C672AD736C2DCF94B6DEC479347ACDC761C3AF24A5A5BCB";
        byte[] bytesText = new BigInteger(hexText, 16).toByteArray();
        
//        byte[][] bytesToKeyReturn = bytesToKey(bytesText);
//        
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        SecretKeySpec chave = new SecretKeySpec(bytesToKeyReturn[0], "AES");
//        
//        byte[] chaveBytes = chave.getEncoded();
//        BigInteger chaveInteger = new BigInteger(chaveBytes);
//        String chaveHex = chaveInteger.toString(16).toUpperCase();
//        
//        System.out.println("Chave:");
//        System.out.println(chaveHex);
//        
//        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(bytesToKeyReturn[0], "AES"), new IvParameterSpec(bytesToKeyReturn[1]));
//
//        byte[] decrypted = cipher.doFinal(bytesText);
//        
//        String decryptedString = new String(decrypted);
//        System.out.println(decryptedString);
	}
	
	private static byte[][] bytesToKey(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		int keyLength = 32;
		int ivLength = 16;
		
		byte[][] both = new byte[2][];
		
		byte[] key = new byte[keyLength];
		int keyIX = 0;
		
		byte[] iv = new byte[ivLength];
		int ivIX = 0;
		
		both[0] = key;
		both[1] = iv;
		
		byte[] mdBuf = null;
		
		int nKey = keyLength;
		int nIV = ivLength;
		
		int addMD = 0;
		
		int count = 1;
		int i = 0;
		
		while(true) {
			md.reset();
			
			if (addMD++ > 0) {
				md.update(mdBuf);
			}
			
			md.update(data);
			
			mdBuf = md.digest();
			
			for (i = 1; i < count; i++) {
				md.reset();
				
				md.update(mdBuf);
				
				mdBuf = md.digest();
			}
			
			i = 0;
			
			if (nKey > 0) {
				while(true) {
					if (nKey == 0) {
						break;
					}
					
					if (i == mdBuf.length) {
						break;
					}
					key[keyIX++] = mdBuf[i];
					nKey--;
					i++;
				}
			}
			
			if (nIV > 0 && i != mdBuf.length) {
				while(true) {
					if (nIV == 0) {
						break;
					}
					
					if (i == mdBuf.length) {
						break;
					}
					
					iv[ivIX++] = mdBuf[i];
					nIV--;
					i++;
				}
			}
			
			if (nKey == 0 && nIV == 0) {
				break;
			}
		}
		
		for (i = 0; i < mdBuf.length; i++) {
			mdBuf[i] = 0;
		}
		
		return both;
	}
}
