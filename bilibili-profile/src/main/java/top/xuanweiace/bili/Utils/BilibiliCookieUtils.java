package top.xuanweiace.bili.Utils;

/**
 * @author zxz
 * @date 2024/2/7 3:10
 */

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

public class BilibiliCookieUtils {
    public static void main(String[] args) throws Exception {
//        System.out.println(getCorrespondPath(System.currentTimeMillis()));
        System.out.println(getCorrespondPath(1707321588353L));
    }
    private static final String publicKeyPEM = "-----BEGIN PUBLIC KEY----- " +
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLgd2OAkcGVtoE3ThUREbio0Eg\n" +
        "Uc/prcajMKXvkCKFCWhJYJcLkcM2DKKcSeFpD/j6Boy538YXnR6VhcuUJOhH2x71\n" +
        "nzPjfdTcqMz7djHum0qSZA0AyCBDABUqCrfNgCiJ00Ra7GmRj+YCK1NJEuewlb40\n" +
        "JNrRuoEUXpabUzGB8QIDAQAB\n" +
        "-----END PUBLIC KEY-----";
    public static String getCorrespondPath(long timestamp) throws Exception {


        byte[] decodedKeyBytes = Base64.getDecoder().decode(publicKeyPEM
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\n", "")
                                .trim());

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey,
                new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));

        byte[] encryptedBytes = cipher.doFinal(("refresh_" + timestamp).getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : encryptedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}