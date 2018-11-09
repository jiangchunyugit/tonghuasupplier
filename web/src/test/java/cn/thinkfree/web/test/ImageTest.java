package cn.thinkfree.web.test;

import cn.thinkfree.database.vo.remote.SyncTransactionVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Type;
import java.util.Map;

public class ImageTest {

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
        Type type = new TypeToken<LinkedMultiValueMap<String, Object>>() {}.getType();
        SyncTransactionVO syncTransactionVO = new SyncTransactionVO();
        syncTransactionVO.setCwgsdm(10);
        syncTransactionVO.setAddress("asdasd");

        System.out.println(gson.toJson(syncTransactionVO));
        LinkedMultiValueMap<String, Object> map2 = gson.fromJson(gson.toJson(syncTransactionVO), type);

        System.out.println(map2);
    }

}
