package org.xer.beerfermcontrol.core.util;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Achlys
 */
public class TPLinkControlV2 {

    private static final Logger LOGGER = LogManager.getLogger(TPLinkControlV2.class);

    private final String username;
    private final String password;
    private final String ip;
    private byte[] localRemoteAuthHash = null;
    private String tapoCookie;
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final MediaType BYTE_ARRAY = MediaType.parse("application/octet-stream");
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final MessageDigest mdSha1 = MessageDigest.getInstance("SHA1");
    private final MessageDigest mdSha256 = MessageDigest.getInstance("SHA-256");
    
    public TPLinkControlV2(String ip, String email, String password) throws Exception {
        this.ip = ip;
        this.username = email;
        this.password = password;
        this.makeHandShakes();
    }
    
    private void makeHandShakes() throws Exception {
        
        // Creamos un nuevo local seed
        Random random = new Random();
        byte[] localSeed = new byte[16];
        random.nextBytes(localSeed);
        
        // Primer handshake
        Response response = this.makePost(String.format("http://%s/app/handshake1", this.ip),
                localSeed, null);
        
        // Procesamos la respuesta
        this.tapoCookie = response.header("Set-Cookie").split(";")[0];
        byte[] responseBA = response.body().bytes();
        byte[] remoteSeed = Arrays.copyOfRange(responseBA, 0, 16);
        byte[] serverHash = Arrays.copyOfRange(responseBA, 16, 48);
        byte[] userSha1 = mdSha1.digest(this.username.getBytes());
        byte[] passSha1 = mdSha1.digest(this.password.getBytes());
        byte[] combinacion = new byte[userSha1.length + passSha1.length];
        ByteBuffer bf = ByteBuffer.wrap(combinacion);
        bf.put(userSha1);
        bf.put(passSha1);
        byte[] authHash = mdSha256.digest(bf.array());
        combinacion = new byte[localSeed.length + remoteSeed.length + authHash.length];
        bf = ByteBuffer.wrap(combinacion);
        bf.put(localSeed);
        bf.put(remoteSeed);
        bf.put(authHash);
        this.localRemoteAuthHash = bf.array();
        byte[] localSeedAuthHash = mdSha256.digest(this.localRemoteAuthHash);
        if(Arrays.equals(serverHash, localSeedAuthHash)){
            LOGGER.debug("Fuck yeah!! Todo controlado!!!");
        } else {
            throw new RuntimeException("Mierda en bote... a buscar el error...");
        }
        
        // Vamos a por el segundo handshake
        bf = ByteBuffer.wrap(combinacion);
        bf.put(remoteSeed);
        bf.put(localSeed);
        bf.put(authHash);
        response = this.makePost(String.format("http://%s/app/handshake2", this.ip),
                mdSha256.digest(bf.array()), this.tapoCookie);
        LOGGER.debug("Segundo handshake: " + response.code() + ", " + response.body().string());
        
    }

    public String turnOnOff(boolean on) throws Exception {
        
        // Calculamos el key
        byte[] combinacion = new byte["lsk".getBytes().length + this.localRemoteAuthHash.length];
        ByteBuffer bf = ByteBuffer.wrap(combinacion);
        bf.put("lsk".getBytes());
        bf.put(this.localRemoteAuthHash);
        byte[] key = Arrays.copyOfRange(mdSha256.digest(bf.array()), 0, 16);
        
        // Calculamos el ivSeq
        combinacion = new byte["iv".getBytes().length + this.localRemoteAuthHash.length];
        bf = ByteBuffer.wrap(combinacion);
        bf.put("iv".getBytes());
        bf.put(this.localRemoteAuthHash);
        byte[] ivSeq = mdSha256.digest(bf.array());
        byte[] iv = Arrays.copyOfRange(ivSeq, 0, 12);
        int seq = ByteBuffer.wrap(Arrays.copyOfRange(ivSeq, ivSeq.length - 4, ivSeq.length)).getInt();
        
        // Calculamos el sig
        combinacion = new byte["ldk".getBytes().length + this.localRemoteAuthHash.length];
        bf = ByteBuffer.wrap(combinacion);
        bf.put("ldk".getBytes());
        bf.put(this.localRemoteAuthHash);
        byte[] sig = Arrays.copyOfRange(mdSha256.digest(bf.array()), 0, 28);
        
        // Vamos poco a poco
        String data = "{\"method\": \"set_device_info\", \"params\":{\"device_on\": ";
        if(on){
            data += "true}}";
        }else{
            data += "false}}";
        }
        seq += 1;
        byte[] seqBa = ByteBuffer.allocate(4).putInt(seq).array();
        
        // Add PKCS#7 padding
        int pad1 = 16 - (data.length() % 16);
        String pad1Str = Integer.toHexString(pad1);
        byte pad1Byte = Byte.parseByte(pad1Str, 16);
        combinacion = new byte[data.getBytes().length + pad1];
        bf = ByteBuffer.wrap(combinacion);
        bf.put(data.getBytes());
        for(int i=0;i<pad1;i++){
            bf.put(pad1Byte);
        }
        byte[] dataPadded = bf.array();
                
        // Enrypt data with key
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        combinacion = new byte[iv.length + seqBa.length];
        bf = ByteBuffer.wrap(combinacion);
        bf.put(iv);
        bf.put(seqBa);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bf.array());
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(1, secretKeySpec, ivParameterSpec);
        byte[] ciphertext = aesCipher.doFinal(dataPadded);
        
        // Signature
        combinacion = new byte[sig.length + seqBa.length + ciphertext.length];
        bf = ByteBuffer.wrap(combinacion);
        bf.put(sig);
        bf.put(seqBa);
        bf.put(ciphertext);
        byte[] sigSha256 = mdSha256.digest(bf.array());
        combinacion = new byte[sigSha256.length + ciphertext.length];
        bf = ByteBuffer.wrap(combinacion);
        bf.put(sigSha256);
        bf.put(ciphertext);
        byte[] encrypted = bf.array();
        
        // Vamos a encender o a apagar
        Response response = this.makePost(String.format("http://%s/app/request?seq=%d", this.ip, seq),
                encrypted, this.tapoCookie);
        String resultado = response.body().string();
        LOGGER.error("Respuesta: " + response.code() + ", body: " + resultado);
        return resultado;
        
    }

    private Response makePost(String url, byte[] ba, String cookie) throws IOException {
        RequestBody body = RequestBody.create(BYTE_ARRAY, ba);
        Request.Builder builder = new Request.Builder();
        if (cookie != null) {
            builder.addHeader("Cookie", cookie);
        }
        builder.url(url).post(body);
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

}
