import okhttp3.*;
import org.wso2.client.api.ApiClient;
import org.wso2.client.api.EmployeeAPIM.DefaultApi;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Test {
    public static void main(String[] args) throws InterruptedException {
String at="eyJ4NXQiOiJNell4TW1Ga09HWXdNV0kwWldObU5EY3hOR1l3WW1NNFpUQTNNV0kyTkRBelpHUXpOR00wWkdSbE5qSmtPREZrWkRSaU9URmtNV0ZoTXpVMlpHVmxOZyIsImtpZCI6Ik16WXhNbUZrT0dZd01XSTBaV05tTkRjeE5HWXdZbU00WlRBM01XSTJOREF6WkdRek5HTTBaR1JsTmpKa09ERmtaRFJpT1RGa01XRmhNelUyWkdWbE5nX1JTMjU2IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJhZG1pbkBjYXJib24uc3VwZXIiLCJhdWQiOiJhSXVNbkFWaWFLN2s0dnNOOFJ2eF90WkVneHNhIiwibmJmIjoxNjUwNDMyNzM3LCJhenAiOiJhSXVNbkFWaWFLN2s0dnNOOFJ2eF90WkVneHNhIiwic2NvcGUiOiJhbV9hcHBsaWNhdGlvbl9zY29wZSBkZWZhdWx0IiwiaXNzIjoiaHR0cHM6XC9cL2xvY2FsaG9zdDo5NDQzXC9vYXV0aDJcL3Rva2VuIiwiZXhwIjoxNjUwNDM2MzM3LCJpYXQiOjE2NTA0MzI3MzcsImp0aSI6Ijc1OTVjYjQyLTA0MDEtNDkxNS04MGQ2LWM5ODZiZWIxODkyZSJ9.iB1xrtkpB-ekWR1v0l8HzCo1X3isYwBhnJKimb6NqsdcccPofyd6-TRW-a61Ljj-kaoAh4aLG7NptQV8jaIjeJr_zOgCtR-AxB0epGJe4Q7YezqLCdrJrgW5wL_5IYo4Wc-84HpdBAeQFz9QdsF8byYEOCc2AT3S89XYlYEUiR7pUYF7HH3V3MzqHTaLXFgh5-wuoZQW0HKS21PgvdKPRlUeg2F_Fsv8Xj9Z3OQyorSeLm4R3f2gLVV-lz1uXqvifZ7lvKC0SCPSlN8fA-ilHfCYITlKB2HYL1rlYiqzmRaXDTI7jKjkFkx1ve7K0vCFGMapmb2UEJFHeKqVG-Z43w";
        String http="http://wso2.southindia.cloudapp.azure.com:8280/wso2/1";
        String https="https://wso2.southindia.cloudapp.azure.com:8243/wso2/1";

//        Interceptor renewTokenInterceptor = new Interceptor() {
//            String accessToken = null;
//
//            public Response intercept(Chain chain) throws IOException {
//                // If there is an access token already, use it for the next request, otherwise generate a token
//                if (accessToken == null) {
//                    getAccessToken();
//                }
//
//                // Send the original request with the Authorization header added and get the response
//                Request originalRequest = chain.request().newBuilder().addHeader("Authorization", accessToken).build();
//                Response response = chain.proceed(originalRequest);
//
//                // If the response fails, retry the request with a new access token
//                if (!response.isSuccessful()) {
//                    //Closing the previous response.
//                    response.close();
//                    //Getting the new access token.
//                    getAccessToken();
//                    //Initiating the API request with the access token.
//                    Request newRequest = originalRequest.newBuilder().removeHeader("Authorization")
//                            .addHeader("Authorization", accessToken).build();
//                    //Capture the response
//                    response = chain.proceed(newRequest);
//                }
//                return response;
//            }
//
//            private void getAccessToken() throws IOException {
//                accessToken = "Bearer " + at;
//            }
//        };

//        DefaultApi defaultApi = new DefaultApi();
//        ApiClient apiClient = defaultApi.getApiClient();
//        OkHttpClient okHttpClient = apiClient.getHttpClient().newBuilder().addInterceptor(renewTokenInterceptor).build();
//        apiClient.setHttpClient(okHttpClient);
//        apiClient.addDefaultHeader("Accept", "application/json");
//        apiClient.setLenientOnJson(true);
//        //parse the base path
//        apiClient.setBasePath(http);

        DefaultApi defaultApi = new DefaultApi();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", "Bearer " + at);
        ApiClient apiClient = defaultApi.getApiClient();
        apiClient.addDefaultHeader("Accept", "application/json");

        apiClient.addDefaultHeader("Authorization", "Bearer " + at);
        apiClient.setLenientOnJson(true);
        apiClient.setBasePath(http);
        defaultApi.setApiClient(apiClient);


        API api =new API();

        System.out.println("Create/POST Method");
        api.createEmployee(defaultApi,"12","Sharma","3333333333","sharma@email.com","40000");

//        System.out.println("Delete Method");
//        api.deleteEmployee(defaultApi,12);

        System.out.println("Get Method");
        api.listEmployee(defaultApi);




    }
}
