import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.client.api.ApiClient;
import org.wso2.client.api.ApiException;
import org.wso2.client.api.Configuration;
import org.wso2.client.api.auth.OAuth;
import org.wso2.client.api.*;
import org.wso2.client.api.EmployeeAPIM.DefaultApi;
import org.wso2.client.model.EmployeeAPIM.EmpArr;
import org.wso2.client.model.EmployeeAPIM.Employee;
import org.wso2.client.model.EmployeeAPIM.Update;
import java.io.File;
import java.io.Serial;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class API{
    private CipherAPI cipherAPI;
    public API() {
        this.cipherAPI = new AES256Cipher();
    }

        public void createEmployee(DefaultApi apiobj, String eid, String ename, String contact, String email, String sal){
        Employee employee = new Employee();
        employee.eid(eid);
        employee.ename(ename);
        employee.contact(contact);
        employee.email(email);
        String encryptedString = this.cipherAPI.encrypt(sal);
        employee.salary(encryptedString);
        employee.resume("");
        try{
            Employee res=apiobj.createPost(employee);
            System.out.println(res);
        } catch (ApiException e) {
            System.out.println("Status code: " + e.getCode());
            System.out.println("Reason: " + e.getResponseBody());
        }

    }
    public void listEmployee(DefaultApi apiobj){
        try{
            EmpArr arr = apiobj.listGet();
//            System.out.println(arr.getData());
            JSONObject payLoad = new JSONObject(arr);
            JSONArray data= payLoad.getJSONArray("data");
            System.out.println("Raw: "+data);

            for(int i=0;i<data.length();i++) {
                JSONObject obj = data.getJSONObject(i);
//                System.out.println(obj.get("salary"));
                String decryptedString = this.cipherAPI.decrypt(obj.get("salary").toString());
                obj.put("salary",decryptedString);
            }

            System.out.println("Decrypted: "+data);

        } catch (ApiException e) {
            System.out.println();
            System.out.println("Status code: " + e.getCode());
            System.out.println("Reason: " + e.getResponseBody());
        }
    }
    public static void updateEmployee(DefaultApi apiobj,String eid,String col,String val){
        Update update=new Update();
        update.eid(eid);
        update.col(col);
        update.val(val);
        try{
            apiobj.updatePut(update);
        } catch (ApiException e) {
            System.out.println("Status code: " + e.getCode());
            System.out.println("Reason: " + e.getResponseBody());
        }
    }
    public static void deleteEmployee(DefaultApi apiobj,Integer eid){
        try{
            apiobj.deleteEidDelete(eid);
        } catch (ApiException e) {
            System.out.println("Status code: " + e.getCode());
            System.out.println("Reason: " + e.getResponseBody());
        }
    }
    public static void addResume(DefaultApi apiobj,String eid, File resume){

        try {
            apiobj.uploadEidPost(eid,resume);
        }
        catch (ApiException e){
            System.out.println("Status code: " + e.getCode());
            System.out.println("Reason: " + e.getResponseBody());
        }
    }

    public static void download(DefaultApi apiobj,String eid){

        try {
            apiobj.downloadEidGet(eid);
        }
        catch (ApiException e){
            System.out.println("Status code: " + e.getCode());
            System.out.println("Reason: " + e.getResponseBody());
        }
    }

}
public class Main {
    public static void main(String[] args) throws InterruptedException {
        String accessToken="eyJ4NXQiOiJNell4TW1Ga09HWXdNV0kwWldObU5EY3hOR1l3WW1NNFpUQTNNV0kyTkRBelpHUXpOR00wWkdSbE5qSmtPREZrWkRSaU9URmtNV0ZoTXpVMlpHVmxOZyIsImtpZCI6Ik16WXhNbUZrT0dZd01XSTBaV05tTkRjeE5HWXdZbU00WlRBM01XSTJOREF6WkdRek5HTTBaR1JsTmpKa09ERmtaRFJpT1RGa01XRmhNelUyWkdWbE5nX1JTMjU2IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJhZG1pbkBjYXJib24uc3VwZXIiLCJhdWQiOiJIejNLenFPelVaQmFlcE1mSVFOQ1NCQ0RrenNhIiwibmJmIjoxNjQ5NzYyMTM3LCJhenAiOiJIejNLenFPelVaQmFlcE1mSVFOQ1NCQ0RrenNhIiwic2NvcGUiOiJhbV9hcHBsaWNhdGlvbl9zY29wZSBkZWZhdWx0IiwiaXNzIjoiaHR0cHM6XC9cL2xvY2FsaG9zdDo5NDQzXC9vYXV0aDJcL3Rva2VuIiwiZXhwIjoxNjQ5NzY1NzM3LCJpYXQiOjE2NDk3NjIxMzcsImp0aSI6IjI5MzdiYjYyLTFiNTgtNDA0ZS04YTc4LWRjMDYzMzg4YTQ2YiJ9.sGJvzHy48pucuVky8fEkmxSfxt8A-G81r9cd0j4pqVLojVGCvdWVput2J-KI0AybYoIw3BZX0bHX0IX3oY-PuR42XXNvt1cSSJa4DmzirlrUjO8PEZjVpxLv7-t0G5KaOxafiIUwcnK79d0mAk1VvvFVvHH-AQm85x2ICliPhb7cWe_no_H7XDh_XJktXB9RP6OXxuhKUlkmALU8OhAhngwnYDePJx28wLPc6kyXHSyBwIHoUaZWI0a0EDbYKbwq3FqLh4KFbwFKzPXsMZ0glMZWSCxiCIAMEwQqUpCouqXWG3rvJ2xgH54HTvs9zwdfUEmQeNJDkZRbw9FbAmM4uQ";
        String http="http://wso2.southindia.cloudapp.azure.com:8280/wso2/1";
        String https="https://wso2.southindia.cloudapp.azure.com:8243/wso2/1";
//        DefaultApi defaultApi = new DefaultApi();
//        ApiClient apiClient = defaultApi.getApiClient();
//        apiClient.addDefaultHeader("Accept", "application/json");
//        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
//        apiClient.setLenientOnJson(true);
//        apiClient.setBasePath("http://wso2.southindia.cloudapp.azure.com:8280/wso2/1");
//
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("Accept", "application/json");
//        headers.put("Authorization", "Bearer " + accessToken);
//        defaultApi.setApiClient(apiClient);


        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath(http);
//        OAuth oa = (OAuth) defaultClient.getAuthentication("default");
        defaultClient.setAccessToken(accessToken);
        defaultClient.addDefaultHeader("Accept", "application/json");
        defaultClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        DefaultApi apiInstance = new DefaultApi(defaultClient);
        API api=new API();

//        OAuth oa = (OAuth) apiClient.getAuthentication("default");
//        oa.setAccessToken(accessToken);

//        System.out.println("Create Method");
//        api.createEmployee(apiInstance,"12","Sharma","5555555555","sharma@email.com","80000");
//        TimeUnit.SECONDS.sleep(1);
//
//
//        System.out.println("Get Method");
//        api.listEmployee(apiInstance);
////        api.listEmployee(defaultApi);
//        TimeUnit.SECONDS.sleep(1);

//        System.out.println("Update Method");
//        api.updateEmployee(apiInstance,"5","contact","7777");
//        TimeUnit.SECONDS.sleep(1);

//        System.out.println("Delete Method");
//        api.deleteEmployee(apiInstance,9);

//        System.out.println("Get Method");
//        api.listEmployee(apiInstance);

//        System.out.println("File Upload");
//        File myfile=new File("resume.docx");
//        API.addResume(apiInstance,"13",myfile);

//        System.out.println("File Download");
//        API.download(apiInstance,"02");

    }
}