import org.json.JSONArray;
import org.json.JSONObject;

public class Json {
    public static void main(String[] args){
        String jsonPayLoad="[{\"id\":1,\"eid\":\"10\",\"ename\":\"Arun\",\"contact\":\"9865322356\",\"email\":\"arun@email.com\",\"salary\":\"50000\",\"resume\":\"\"}]";
//
//        JSONObject payLoad = new JSONObject(jsonPayLoad);
//        JSONArray data= new JSONArray();

//        for(int i=0;i<data.length();i++) {
//            JSONObject obj = data.getJSONObject(i);
//            System.out.println(obj.get("salary"));
//            obj.put("salary","newsal");
//        }


        JSONArray data= new JSONArray(jsonPayLoad);

        for(int i=0;i<data.length();i++) {
            JSONObject obj = data.getJSONObject(i);
            System.out.println(obj.get("salary"));
//            String encryptedString = this.cipherAPI.encrypt(obj.get("salary").toString());
            obj.put("salary","newsal");
        }
        System.out.println(data);
        JSONObject payload =data.getJSONObject(0);
        System.out.println(payload);
//        JsonUtil.newJsonPayload(((Axis2MessageContext) messageContext).getAxis2MessageContext(), data.toString(), true, true);



    }
}
