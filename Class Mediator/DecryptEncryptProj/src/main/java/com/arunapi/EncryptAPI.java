package com.arunapi;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.core.axis2.Axis2MessageContext;  
import org.apache.synapse.mediators.AbstractMediator;
import org.codehaus.jettison.json.JSONException;  
import org.codehaus.jettison.json.JSONObject;  
import org.apache.synapse.commons.json.JsonUtil;  

public class EncryptAPI extends AbstractMediator { 

    private static final Log log = LogFactory.getLog(EncryptAPI.class.getName());

    private CipherAPI cipherAPI;
    String stringToBeEncrypted;
    public EncryptAPI() {
        this.cipherAPI = new AES256Cipher();
    }

    public boolean mediate(MessageContext messageContext) {
        String jsonPayLoad = StringUtils.stripToEmpty((String) messageContext  
                .getProperty("payload"));  
//        System.out.println("Json Payload: "+jsonPayLoad);

        try {
        	JSONObject payLoad = new JSONObject(jsonPayLoad);
            JSONArray data= payLoad.getJSONArray("data");
        	System.out.println("Raw: "+data);
            

            for(int i=0;i<data.length();i++) {
                JSONObject obj = data.getJSONObject(i);
//                System.out.println(obj.get("salary"));
                String encryptedString = this.cipherAPI.encrypt(obj.get("salary").toString());
                obj.put("salary",encryptedString);
            } 
 
            System.out.println("Encrypted Payload: : "+payLoad.toString());
            JsonUtil.newJsonPayload(((Axis2MessageContext) messageContext).getAxis2MessageContext(), payLoad.toString(), true, true);         
//        	log.error("Encryption Started =================!!!!!!!===================="+stringToBeEncrypted);
            return true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public void setpayload(String str) {
    	stringToBeEncrypted=str;
    }
}
