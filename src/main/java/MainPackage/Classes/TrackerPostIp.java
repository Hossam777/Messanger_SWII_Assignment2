package MainPackage.Classes;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;

public class TrackerPostIp {

    public interface CallBack{
        public void completed(JSONArray jsonArray) throws Throwable;
        public void failed();
    }

    private static final String serverIp = "http://192.168.43.100" ;
    //private static final String testserverIp = "http://localhost" ;
    //private static final String testserverIp2 = "http://192.168.43.68/My%20Files/tracker/index.php" ;


    public void postIp(String ip,CallBack callBack) throws Throwable {
        HttpResponse<JsonNode> jsonResponse = Unirest.post( serverIp+ "/serverTracker/Tracker.php")
                .field("appid", "4777")
                .field("ip", ip)
                .field("func", "add")
                .asJson();
        if(jsonResponse.getStatus() == 200){
            callBack.completed(jsonResponse.getBody().getArray());
        }else callBack.failed();
    }
    public void deleteIp(String ip,CallBack callBack) throws Throwable {
        HttpResponse<JsonNode> jsonResponse = Unirest.post( serverIp+ "/serverTracker/Tracker.php")
                .field("appid", "4777")
                .field("ip", ip)
                .field("func", "delete")
                .asJson();
        if(jsonResponse.getStatus() == 200){
            callBack.completed(jsonResponse.getBody().getArray());
        }else callBack.failed();
    }
}
