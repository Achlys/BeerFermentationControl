package org.xer.beerfermcontrol.core.util;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author Achlys
 */
public class TPLinkControl {

    private static final Logger LOGGER = LogManager.getLogger(TPLinkControl.class);

    private final String username;
    private final String password;
    private final String ip;
    private final String uuid;
    private String privateKey;
    private String publicKey;
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private String tapoCookie;
    private String tapoKey;
    private Cipher f21776a_enc;
    private Cipher f21777b_dec;
    private String token;

    public TPLinkControl(String ip, String uuid, String email, String password) throws Exception {
        this.ip = ip;
        this.uuid = uuid;
        this.generateKeyPair();
        this.makeHandShake();
        this.username = this.encodeToString(this.shaDigestUsername(email).getBytes());
        this.password = this.encodeToString(password.getBytes());
        this.makeSecurePassthrough();
    }

    private void generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator instance = KeyPairGenerator.getInstance("RSA");
        instance.initialize(1024, new SecureRandom());
        KeyPair generateKeyPair = instance.generateKeyPair();
        publicKey = new String(java.util.Base64.getMimeEncoder().encode(((RSAPublicKey) generateKeyPair.getPublic()).getEncoded()));
        privateKey = new String(java.util.Base64.getMimeEncoder().encode(((RSAPrivateKey) generateKeyPair.getPrivate()).getEncoded()));
    }

    private void makeHandShake() throws Exception {
        Response response = this.makePost(String.format("http://%s/app", this.ip),
                String.format("{\"method\": \"handshake\", \"params\": {\"key\": \"-----BEGIN PUBLIC KEY-----\\n%s\\n-----END PUBLIC KEY-----\\n\", \"requestTimeMils\": %d}}",
                        publicKey, Calendar.getInstance().getTimeInMillis()),
                null);
        this.tapoKey = new JSONObject(response.body().string()).getJSONObject("result").getString("key");
        LOGGER.error("makeHandShake() key: " + this.tapoKey);
        this.tapoCookie = response.header("Set-Cookie").split(";")[0];
        LOGGER.error("makeHandShake() cookie: " + this.tapoCookie);
        this.decodeTapoKey();
    }

    private Response makePost(String url, String json, String cookie) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder builder = new Request.Builder();
        if (cookie != null) {
            builder.addHeader("Cookie", cookie);
        }
        builder.url(url)
                .post(body);
        Request request = builder.build();
        boolean executed = false;
        Response response = null;
        int i = 0;
        while (!executed && i++ < 10) {
            try {
                response = okHttpClient.newCall(request).execute();
                executed = true;
            } catch (IOException ex) {
                LOGGER.error("Request failed, retry...");
            }
        }
        return response;
    }

    private void decodeTapoKey() throws Exception {
        byte[] decode = this.decode(this.tapoKey.getBytes("UTF-8"));
        byte[] decode2 = this.decode(this.privateKey);
        Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey p = kf.generatePrivate(new PKCS8EncodedKeySpec(decode2));
        instance.init(Cipher.DECRYPT_MODE, p);
        byte[] doFinal = instance.doFinal(decode);
        byte[] bArr = new byte[16];
        byte[] bArr2 = new byte[16];
        System.arraycopy(doFinal, 0, bArr, 0, 16);
        System.arraycopy(doFinal, 16, bArr2, 0, 16);
        this.c658a(bArr, bArr2);
    }

    private void c658a(byte[] bArr, byte[] bArr2) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
        f21776a_enc = Cipher.getInstance("AES/CBC/PKCS5Padding");
        f21776a_enc.init(1, secretKeySpec, ivParameterSpec);
        f21777b_dec = Cipher.getInstance("AES/CBC/PKCS5Padding");
        f21777b_dec.init(2, secretKeySpec, ivParameterSpec);
    }

    private String mo38009b_enc(String str) throws Exception {
        byte[] doFinal;
        doFinal = this.f21776a_enc.doFinal(str.getBytes());
        String encrypted = this.encodeToString(doFinal);
        return encrypted.replace("\r\n", "");
    }

    private String mo38006a_dec(String str) throws Exception {
        byte[] data = this.decode(str.getBytes("UTF-8"));
        byte[] doFinal;
        doFinal = this.f21777b_dec.doFinal(data);
        return new String(doFinal);
    }

    private String encodeToString(byte[] src) {
        return Base64.getMimeEncoder().encodeToString(src);
    }

    private byte[] decode(byte[] src) {
        return Base64.getMimeDecoder().decode(src);
    }

    private byte[] decode(String src) {
        return Base64.getMimeDecoder().decode(src);
    }

    private void makeSecurePassthrough() throws Exception {
        String request = "{\"method\": \"login_device\", \"params\": {\"password\": \"%s\", \"username\": \"%s\"}, \"requestTimeMils\": 0}";
        Response response = this.makePost(String.format("http://%s/app", this.ip),
                String.format("{\"method\": \"securePassthrough\", \"params\": {\"request\": \"%s\"}}",
                        this.mo38009b_enc(String.format(request, this.password, this.username))),
                this.tapoCookie);
        String encryptedResponse = new JSONObject(response.body().string()).getJSONObject("result").getString("response");
        LOGGER.error("makeSecurePassthrough() encryptedResponse: " + encryptedResponse);
        String decriptedResponse = this.mo38006a_dec(encryptedResponse);
        LOGGER.error("makeSecurePassthrough() decriptedResponse: " + decriptedResponse);
        this.token = new JSONObject(decriptedResponse).getJSONObject("result").getString("token");
        LOGGER.error("makeSecurePassthrough() token: " + this.token);
    }

    private String shaDigestUsername(String str) throws Exception {
        byte[] bArr = str.getBytes();
        byte[] digest = MessageDigest.getInstance("SHA1").digest(bArr);
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                sb.append("0");
                sb.append(hexString);
            } else {
                sb.append(hexString);
            }
        }
        return sb.toString();
    }

    public String turnOn() throws Exception {
        Response response = this.makePost(String.format("http://%s/app?token=%s", this.ip, this.token),
                String.format("{\"method\": \"set_device_info\", \"params\":{\"device_on\": True}, \"requestTimeMils\": %d, \"terminalUUID\": \"%s\"}",
                        Calendar.getInstance().getTimeInMillis(), this.uuid),
                this.tapoCookie);
        return response.body().string();
    }

    public String turnOff() throws Exception {
        Response response = this.makePost(String.format("http://%s/app?token=%s", this.ip, this.token),
                String.format("{\"method\": \"set_device_info\", \"params\":{\"device_on\": False}, \"requestTimeMils\": %d, \"terminalUUID\": \"%s\"}",
                        Calendar.getInstance().getTimeInMillis(), this.uuid),
                this.tapoCookie);
        return response.body().string();
    }

}
