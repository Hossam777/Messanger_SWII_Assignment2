package MainPackage.Classes;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;

public class TrackerPostIp {

    public interface CallBack{
        public void completed(JSONArray jsonArray);
        public void failed();
    }

    private static final String serverIp = "http://192.168.0.100" ;
    private static final String testserverIp = "http://localhost" ;

    public void postIp(String ip,CallBack callBack) throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.post( testserverIp+ "/serverTracker/Tracker.php")
                .field("appid", "4777")
                .field("ip", ip)
                .asJson();
        if(jsonResponse.getStatus() == 200){
            callBack.completed(jsonResponse.getBody().getArray());
        }else callBack.failed();
    }
}
