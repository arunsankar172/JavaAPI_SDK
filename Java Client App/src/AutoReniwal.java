import org.wso2.client.api.ApiClient;
import org.wso2.client.api.EmployeeAPIM.DefaultApi;
import org.wso2.client.api.auth.OAuth;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class AutoReniwal {
//    static OkHttpClient httpClient = new OkHttpClient();
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    private static void sendPost() throws Exception {
        String consumerKey="hRW_wT6kMnKmVrgRM7MafaV3mSwa";
        String consumerSecret="FBuVSpMRvMsfpvANkSeHb1n1Kiwa";
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
        System.out.println(response.statusCode());

        // print response body
        System.out.println(response.body());

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



//        private static void sendPost() throws Exception {
//        String consumerKey="hRW_wT6kMnKmVrgRM7MafaV3mSwa";
//        String consumerSecret="FBuVSpMRvMsfpvANkSeHb1n1Kiwa";
//        String toencode=consumerKey+":"+consumerSecret;
//        byte[] bytesEncoded = Base64.encodeBase64(toencode.getBytes());
//
//        String base64 = "Basic "+ new String(Base64.encodeBase64(toencode.getBytes()));
////        String base64 = "Basic "+ "aFJXX3dUNmtNbkttVnJnUk03TWFmYVYzbVN3YTpGQnVWU3BNUnZNc2ZwdkFOa1NlSGIxbjFLaXdh";
//
//
//        RequestBody body = new FormBody.Builder()
//                .add("grant_type", "client_credentials")
//                .build();
//
//        Request request = new Request.Builder()
//                .url("https://wso2.southindia.cloudapp.azure.com:8243/token")
//                .addHeader("Authorization", base64)
//                .post(body)
//                .build();
//
//        try (Response response = httpClient.newCall(request).execute()) {
//
//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//            // Get response body
//            System.out.println(response.body());
//        }

//    }


    public static void main(String[] args) throws Exception {
//        String http="http://wso2.southindia.cloudapp.azure.com:8280/wso2/1";
//        String https="https://wso2.southindia.cloudapp.azure.com:8243/wso2/1";
//
//        String at="hRW_wT6kMnKmVrgRM7MafaV3mSwa";  //access token
//
//        DefaultApi defaultApi = new DefaultApi();
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("Accept", "application/json");
//        headers.put("Authorization", "Bearer " + at);
//        ApiClient apiClient = defaultApi.getApiClient();
//        apiClient.addDefaultHeader("Accept", "application/json");
//        apiClient.addDefaultHeader("Authorization", "Bearer " + at);
//        apiClient.setLenientOnJson(true);
//        apiClient.setBasePath("YOUR_WSO2_API_ENDPOINT");
//        defaultApi.setApiClient(apiClient);
//
//
        sendPost();
//        System.out.println(sendPost());

    }

}


