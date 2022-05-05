
import org.apache.commons.codec.binary.Base64;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class AutoRefresh {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    static String at;
    static String getToken() throws Exception {
        String consumerKey="zajeKCPRKaoLbT5mpWLsi1AB9hwa";
        String consumerSecret="zRRwFGoMp7pvM9hqIStFCWueBdca";
        String toencode=consumerKey+":"+consumerSecret;
        byte[] bytesEncoded = Base64.encodeBase64(toencode.getBytes());

        String base64 = "Basic "+ new String(Base64.encodeBase64(toencode.getBytes()));

        Map<Object, Object> data = new HashMap<>();
        data.put("grant_type", "client_credentials");


        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create("https://wso2.southindia.cloudapp.azure.com:8243/token"))
//                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Authorization", base64)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print status code
//        System.out.println(response.statusCode());

        // print response body
        System.out.println(response.body());
        JSONObject obj =new JSONObject(response.body().toString());
        System.out.println(obj.get("access_token"));
        at= (String) obj.get("access_token");
        return at;

    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        System.out.println(builder.toString());
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }


    public static void main(String[] argv) throws Exception {
        System.out.println("Get Token");
        getToken();
//        System.out.println("Refresh Token");
//        refreshToken();
    }

}

