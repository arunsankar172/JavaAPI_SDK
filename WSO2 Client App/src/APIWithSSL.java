import okhttp3.*;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.client.api.*;
import org.wso2.client.api.ApiClient;
import org.wso2.client.api.ApiException;
import org.wso2.client.api.EmployeeAPIM.DefaultApi;
//import org.wso2.client.model.EmployeeAPIM.EmpArr;
import org.wso2.client.model.EmployeeAPIM.Employee;
import org.wso2.client.model.EmployeeAPIM.EmployeeList;
import org.wso2.client.model.EmployeeAPIM.EmployeePost;
import org.wso2.client.model.EmployeeAPIM.Update;

import javax.net.ssl.*;

public class APIWithSSL {

    public CacheManager cacheManager;
    public Cache cache;
    private Properties appProperty;
    public DefaultApi defaultApi;
    public ApiClient apiClient;

    public APIWithSSL() {
        try (InputStream input = new FileInputStream("app.properties")) {
            this.appProperty = new Properties();
            this.appProperty.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.cacheManager = CacheManager.newInstance();
        this.cache = cacheManager.getCache(appProperty.getProperty("cacheName"));
        String cacheElementName=appProperty.getProperty("cacheElementName");
//        this.cache.put(new Element("accessToken",null));
        Interceptor renewTokenInterceptor = new Interceptor() {
            private Element cacheElement;
            public Response intercept(Chain chain) throws IOException {
                this.cacheElement = cache.get(cacheElementName);
//                System.out.println(cache.isKeyInCache(cacheElementName));
                if (!cache.isKeyInCache(cacheElementName)) {
                    try {
                        System.out.println("New Token");
                        cache.remove(cacheElementName);
                        getAccessToken();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Using Existing Token");
                this.cacheElement=cache.get(cacheElementName);
                System.out.println(this.cacheElement.getObjectValue());
                Request originalRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + this.cacheElement.getObjectValue().toString()).build();
                Response response = chain.proceed(originalRequest);
                if (!response.isSuccessful() && response.code()==401) {
                    response.close();
                    cache.remove(cacheElementName);
                    try {
                        System.out.println("Invalid/Expired Token");
                        getAccessToken();
                        this.cacheElement=cache.get(cacheElementName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Request newRequest = originalRequest.newBuilder().removeHeader("Authorization")
                            .addHeader("Authorization", "Bearer " + this.cacheElement.getObjectValue().toString()).build();
                    response = chain.proceed(newRequest);
                }
                cacheManager.shutdown();
                return response;
            }

            //Get token from server using getTokenFromServer() method
            public void getAccessToken() throws Exception {
                System.out.println("Getting new token from Server");
                cache.put(new Element(cacheElementName, getTokenFromServer()));
                Element cacheElement2=cache.get(cacheElementName);
                System.out.println(cacheElement2.getObjectValue());
            }

        };

//      Initilize Api client with header and API endpoint
        defaultApi = new DefaultApi();
        apiClient = defaultApi.getApiClient();
        OkHttpClient okHttpClient = apiClient.getHttpClient().newBuilder().addInterceptor(renewTokenInterceptor).build();
        apiClient.setHttpClient(okHttpClient);
        apiClient.addDefaultHeader("Accept", "application/json");
        apiClient.setLenientOnJson(true);
        apiClient.setBasePath(appProperty.getProperty("httpEndpoint"));
    }

    public String getTokenFromServer() throws Exception {
       HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        String base64Encoded = "Basic " + new String(Base64.encodeBase64((appProperty.getProperty("consumerKey")+ ":" +appProperty.getProperty("consumerSecret")).getBytes()));
        Map<Object, Object> data = new HashMap<>();
        data.put("grant_type", "client_credentials");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create("https://wso2.southindia.cloudapp.azure.com:8243/token"))
                .header("Authorization", base64Encoded)
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject accessTokenJSON =new JSONObject(response.body().toString());
        return accessTokenJSON.get("access_token").toString();
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
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

}
class APILogicWithSSL extends APIWithSSL{
    private AES256Cipher cipherAPI;


    APILogicWithSSL(){
        this.cipherAPI = new AES256Cipher();
    }

    //GET method to get all data from database
    public void listEmployee(){
        try{
            EmployeeList employeeList=defaultApi.listGet();
            System.out.println("Decrypted: "+this.cipherAPI.decrypt(employeeList.getData().toString()));
        } catch (ApiException e) {
            System.out.println("Status code: " + e.getCode());
            System.out.println("Reason: " + e.getResponseBody());

        }
    }

    //POST method to insert new Employee into Database
    public void createEmployee(String employeeId, String employeeName, String contact, String email, String salary){
//        Create new Employee object to store employee details
        String employeeJsonString="{\n" +
                "  \"eid\": \""+employeeId+"\",\n" +
                "  \"ename\": \""+employeeName+"\",\n" +
                "  \"contact\": \""+contact+"\",\n" +
                "  \"email\": \""+email+"\",\n" +
                "  \"salary\": \""+salary+"\"\n" +
                "}";
        JSONObject employeeJson = new JSONObject(employeeJsonString);
        System.out.println(employeeJson);
        String encryptedString = this.cipherAPI.encrypt(employeeJson.toString());
        EmployeePost employeePost=new EmployeePost();
        employeePost.data(encryptedString);

        try{
            Employee res=this.defaultApi.createPost(employeePost);
            System.out.println(res);
        } catch (ApiException e) {
            System.out.println("Status code: " + e.getCode());
            System.out.println("Reason: " + e.getResponseBody());
        }

    }

    //UPDATE
    public void updateEmployee(String employeeId, String column, String value){
        Update update=new Update();
        update.eid(employeeId);
        update.col(column);
        update.val(value);
        try{
            this.defaultApi.updatePut(update);
        } catch (ApiException e) {
            System.out.println("Status code: " + e.getCode());
            System.out.println("Reason: " + e.getResponseBody());
        }
    }

    //DELETE
    public void deleteEmployee(String employeeId){
        try{
            this.defaultApi.deleteEidDelete(Integer.parseInt(employeeId));
        } catch (ApiException e) {
            System.out.println("Status code: " + e.getCode());
            System.out.println("Reason: " + e.getResponseBody());
        }
    }

    //File Upload
    public void addResume(String employeeId,String filePath){
        File resume=new File(filePath);
        try {
            this.defaultApi.uploadEidPost(employeeId,resume);
        }
        catch (ApiException e){
            System.out.println("Status code: " + e.getCode());
            System.out.println("Reason: " + e.getResponseBody());
        }
    }

    //File Download
    public void download(String employeeId){

        try {
            this.defaultApi.downloadEidGet(employeeId);
        }
        catch (ApiException e){
            System.out.println("Status code: " + e.getCode());
            System.out.println("Reason: " + e.getResponseBody());
        }
    }


}
