import okhttp3.*;
import org.wso2.client.api.ApiClient;
import org.wso2.client.api.EmployeeAPIM.DefaultApi;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Test {
    public static void main(String[] args) throws Exception {
        String http="http://wso2.southindia.cloudapp.azure.com:8280/wso2/1";
        String https="https://wso2.southindia.cloudapp.azure.com:8243/wso2/1";


//        Interceptor renewTokenInterceptor = new Interceptor() {
//            String accessToken = null;
//
//            public Response intercept(Chain chain) throws IOException {
//                // If there is an access token already, use it for the next request, otherwise generate a token
//                if (accessToken == null) {
//                    try {
//                        getAccessToken();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
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
//                    try {
//                        getAccessToken();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    //Initiating the API request with the access token.
//                    Request newRequest = originalRequest.newBuilder().removeHeader("Authorization")
//                            .addHeader("Authorization", accessToken).build();
//                    //Capture the response
//                    response = chain.proceed(newRequest);
//                }
//                return response;
//            }
//
//            private void getAccessToken() throws Exception {
//                AutoRefresh af =new AutoRefresh();
//                String att=af.getToken();
//            System.out.println(att);
//            accessToken = "Bearer " + att;
//            }
//        };



            AutoRefresh af =new AutoRefresh();
            String at=af.getToken();
            System.out.println(at);

//        String at="eyJ4NXQiOiJNell4TW1Ga09HWXdNV0kwWldObU5EY3hOR1l3WW1NNFpUQTNNV0kyTkRBelpHUXpOR00wWkdSbE5qSmtPREZrWkRSaU9URmtNV0ZoTXpVMlpHVmxOZyIsImtpZCI6Ik16WXhNbUZrT0dZd01XSTBaV05tTkRjeE5HWXdZbU00WlRBM01XSTJOREF6WkdRek5HTTBaR1JsTmpKa09ERmtaRFJpT1RGa01XRmhNelUyWkdWbE5nX1JTMjU2IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJhZG1pbkBjYXJib24uc3VwZXIiLCJhdWQiOiJIejNLenFPelVaQmFlcE1mSVFOQ1NCQ0RrenNhIiwibmJmIjoxNjQ5NzYyMTM3LCJhenAiOiJIejNLenFPelVaQmFlcE1mSVFOQ1NCQ0RrenNhIiwic2NvcGUiOiJhbV9hcHBsaWNhdGlvbl9zY29wZSBkZWZhdWx0IiwiaXNzIjoiaHR0cHM6XC9cL2xvY2FsaG9zdDo5NDQzXC9vYXV0aDJcL3Rva2VuIiwiZXhwIjoxNjQ5NzY1NzM3LCJpYXQiOjE2NDk3NjIxMzcsImp0aSI6IjI5MzdiYjYyLTFiNTgtNDA0ZS04YTc4LWRjMDYzMzg4YTQ2YiJ9.sGJvzHy48pucuVky8fEkmxSfxt8A-G81r9cd0j4pqVLojVGCvdWVput2J-KI0AybYoIw3BZX0bHX0IX3oY-PuR42XXNvt1cSSJa4DmzirlrUjO8PEZjVpxLv7-t0G5KaOxafiIUwcnK79d0mAk1VvvFVvHH-AQm85x2ICliPhb7cWe_no_H7XDh_XJktXB9RP6OXxuhKUlkmALU8OhAhngwnYDePJx28wLPc6kyXHSyBwIHoUaZWI0a0EDbYKbwq3FqLh4KFbwFKzPXsMZ0glMZWSCxiCIAMEwQqUpCouqXWG3rvJ2xgH54HTvs9zwdfUEmQeNJDkZRbw9FbAmM4uQ";
        DefaultApi defaultApi = new DefaultApi();
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("Accept", "application/json");
//        headers.put("Authorization", "Bearer " + at);
        ApiClient apiClient = defaultApi.getApiClient();
        apiClient.addDefaultHeader("Accept", "application/json");
        apiClient.addDefaultHeader("Authorization", "Bearer " + at);
        apiClient.setLenientOnJson(true);
        apiClient.setBasePath(http);
        defaultApi.setApiClient(apiClient);


        API api =new API();

//        System.out.println("Create/POST Method");
//        api.createEmployee(defaultApi,"12","Sharma","3333333333","sharma@email.com","40000");

//        System.out.println("Delete Method");
//        api.deleteEmployee(defaultApi,12);

        System.out.println("Get Method");
        api.listEmployee(defaultApi);





    }
}
