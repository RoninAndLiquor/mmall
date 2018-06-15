import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class MapTest {

    public static void main(String[] args) {
        Map<Integer,Object> map = new HashMap<>();
        for(int i=0;i<13;i++){
            map.put(i,i);
        }
    }

}
