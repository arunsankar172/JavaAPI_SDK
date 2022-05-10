import okhttp3.*;

import java.io.*;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Properties;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.client.api.ApiClient;
import org.wso2.client.api.ApiException;
import org.wso2.client.api.EmployeeAPIM.DefaultApi;
//import org.wso2.client.model.EmployeeAPIM.EmpArr;
import org.wso2.client.model.EmployeeAPIM.Employee;
import org.wso2.client.model.EmployeeAPIM.Update;

import javax.net.ssl.*;

public class API {

    public CacheManager cacheManager;
    public Cache cache;
    private Properties appProperty;
    public DefaultApi defaultApi;
    public ApiClient apiClient;

    public API() {
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

        //Method to Skip SSL certificate verification while invoking Token API return OkHttp client without Hostname Verifier
        private static OkHttpClient unsafeOkHttpClient() {
            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };
                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

                OkHttpClient okHttpClient = builder.build();
                return okHttpClient;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //Get new Token from Token Endpoint using OkHttp client
        public String getTokenFromServer() throws IOException {
            OkHttpClient client = unsafeOkHttpClient();
//            Encode consumerKey and consumerSecret in Base64 format
            String base64Encoded = "Basic " + new String(Base64.encodeBase64((appProperty.getProperty("consumerKey")+ ":" +appProperty.getProperty("consumerSecret")).getBytes()));
            RequestBody formBody = new FormBody.Builder()
                    .addEncoded("grant_type", URLEncoder.encode("client_credentials", "UTF-8"))
                    .build();
//          Create new Request
            Request request = new Request.Builder()
                    .url(appProperty.getProperty("tokenEndpoint"))
                    .post(formBody)
                    .addHeader("Content-Type", " application/x-www-form-urlencoded")
                    .addHeader("Authorization", base64Encoded)
                    .build();
            Response response = client.newCall(request).execute();
                if (!response.isSuccessful()){
//                    System.out.println(response.code());
                }
                JSONObject accessTokenJSON = new JSONObject(response.body().string());
//            Return Access Token from response json as string
                return accessTokenJSON.get("access_token").toString();
        }

}
class APILogic extends API{
    //GET method to get all data from database
    public void listEmployee(){
        try{
            System.out.println(defaultApi.listGet());
//            JSONObject payLoad = new JSONObject(arr);
//            JSONArray data= payLoad.getJSONArray("data");
//            System.out.println("Raw: "+data);
//            for(int i=0;i<data.length();i++) {
//                JSONObject obj = data.getJSONObject(i);
//                String decryptedString = this.cipherAPI.decrypt(obj.get("salary").toString());
//                obj.put("salary",decryptedString);
//            }
//            System.out.println("Decrypted: "+data);
        } catch (ApiException e) {
//            System.out.println("Status code: " + e.getCode());
            System.out.println("Reason: " + e.getResponseBody());

        }
    }

    //POST method to insert new Employee into Database
    public void createEmployee(String employeeId, String employeeName, String contact, String email, String salary){
//        Create new Employee object to store employee details
        Employee employee = new Employee();
        employee.eid(employeeId);
        employee.ename(employeeName);
        employee.contact(contact);
        employee.email(email);
//        String encryptedString = this.cipherAPI.encrypt(sal);
//        employee.salary(encryptedString);
        employee.salary(salary);
//        employee.resume("");
//        try{
////            this.defaultApi.createPost(employee);
////            System.out.println(res);
//        } catch (ApiException e) {
//            System.out.println("Status code: " + e.getCode());
//            System.out.println("Reason: " + e.getResponseBody());
//        }

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
