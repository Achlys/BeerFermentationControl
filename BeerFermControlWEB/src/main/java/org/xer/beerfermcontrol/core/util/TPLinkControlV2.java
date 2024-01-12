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
import javax.xml.bind.DatatypeConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author Achlys
 */
public class TPLinkControlV2 {

    private static final Logger LOGGER = LogManager.getLogger(TPLinkControlV2.class);

    private final String username;
    private final String password;
    private final String ip;
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient okHttpClient = new OkHttpClient();

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
                String.format("{\"data\": \"%s\", \"timeout\": 2, \"params\": \"None\"}",
                        DatatypeConverter.printBase64Binary(localSeed)),
                null);
        // Procesamos la respuesta
        String responseStr = response.body().string();
        LOGGER.error("Respuesta: " + response.body().string());
        byte[] responseBA = DatatypeConverter.parseBase64Binary(new JSONObject(responseStr).getString("Content"));
        byte[] remoteSeed = Arrays.copyOfRange(responseBA, 0, 16);
        byte[] serverHash = Arrays.copyOfRange(responseBA, 16, 48);
        MessageDigest mdSha1 = MessageDigest.getInstance("SHA1");
        MessageDigest mdSha256 = MessageDigest.getInstance("SHA256");
        byte[] userSha1 = mdSha1.digest(this.username.getBytes());
        byte[] passSha1 = mdSha1.digest(this.password.getBytes());
        byte[] combinacion = new byte[userSha1.length + passSha1.length];
        ByteBuffer bf = ByteBuffer.wrap(combinacion);
        bf.put(userSha1);
        bf.put(passSha1);
        byte[] autoHash = mdSha256.digest(bf.array());
        combinacion = new byte[localSeed.length + remoteSeed.length + autoHash.length];
        bf = ByteBuffer.wrap(combinacion);
        bf.put(localSeed);
        bf.put(remoteSeed);
        bf.put(autoHash);
        byte[] localSeedAuthHash = mdSha256.digest(bf.array());
        LOGGER.error("serverHash: " + Arrays.toString(serverHash));
        LOGGER.error("localSeedAuthHash: " + Arrays.toString(localSeedAuthHash));
        if(Arrays.equals(serverHash, localSeedAuthHash)){
            LOGGER.error("Fuck yeah!! Todo controlado!!!");
        } else {
            throw new RuntimeException("Mierda en bote... a buscar el error...");
        }       
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

    public String turnOn() throws Exception {
        return null;
    }

    public String turnOff() throws Exception {
        return null;
    }

}
