import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author zxz
 * @date 2024/1/26 14:46
 */
public class TestSth {

    public static void main(String[] args) {
//        String s = "123456789123123123123123";
//        System.out.println("s = " + s.substring(0, 100));
//        List<String> list = new ArrayList<>(Arrays.asList("12","23"));
//        System.out.println("list = " + list);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://www.bilibili.com/correspond/1/xxx")
                .addHeader("Cookie", "")
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
