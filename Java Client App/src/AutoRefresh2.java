import okhttp3.MediaType;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AutoRefresh2 {
    public static String getAccessToken() throws IOException {
        String consumerKey="zajeKCPRKaoLbT5mpWLsi1AB9hwa";
        String consumerSecret="zRRwFGoMp7pvM9hqIStFCWueBdca";
        String toencode=consumerKey+":"+consumerSecret;
        byte[] bytesEncoded = Base64.encodeBase64(toencode.getBytes());
        String base64 = "Basic "+ new String(Base64.encodeBase64(toencode.getBytes()));

        OkHttpClient client = new OkHttpClient.Builder().build();
//                .hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                })
//                .build();
        
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials");
        Request request = new Request.Builder()
                .url("https://wso2.southindia.cloudapp.azure.com:8243/token")
                .method("POST", body)
                .addHeader("Authorization", base64)
//                .addHeader("Content-Type", "text/plain")
                .build();
        Response response = client.newCall(request).execute();
//        String accessToken = (String)responseMap.get("access_token")
//        System.out.println(response);
        String at;
        JSONObject obj =new JSONObject(response.body().string());
        at= (String) obj.get("access_token");
//        String json = response.body().string();
        System.out.println(at);
        return " ";
    }

    public static void main(String[] args) throws IOException {
        getAccessToken();
    }
}
