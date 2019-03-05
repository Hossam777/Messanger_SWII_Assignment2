package MainPackage.Classes;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;

public class TrackerPostIp {

    public interface CallBack{
        public void completed(JSONArray jsonArray) throws Exception;
        //public void completed(String ip) throws Exception;
        public void failed();
    }

    private static final String serverIp = "http://192.168.0.100" ;
    //private static final String testserverIp = "http://localhost" ;
    //private static final String testserverIp2 = "http://192.168.43.68/My%20Files/tracker/index.php" ;


    public void postIp(String ip,CallBack callBack) throws Exception {
        HttpResponse<JsonNode> jsonResponse = Unirest.post( serverIp+ "/serverTracker/Tracker.php")
                .field("appid", "4777")
                .field("ip", ip)
                .asJson();
        if(jsonResponse.getStatus() == 200){
            callBack.completed(jsonResponse.getBody().getArray());
        }else callBack.failed();
    }
   /*public void test(String ip,CallBack callBack) throws Exception {
        HttpResponse<JsonNode> jsonResponse = Unirest.post( testserverIp2+ "/serverTracker/Tracker.php")
                .field("auth", "kke82j#*fha#7%62jdhx")
                .field("myIP", ip)
                .field("op", "addIP")
                .asJson();
            callBack.completed(jsonResponse.getBody().getObject().getJSONArray("data").getString(0));
    }*/
}
