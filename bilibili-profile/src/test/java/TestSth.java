import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
                .url("https://www.bilibili.com/correspond/1/172f0d92831eeba0a8ff7c6c33444aac0f16ed526b4214a55e48dd124fc3b5dda9678b7b9c10bceccba6818cf49cd09c2b073eae0e6090fb135dc986a629324872e715808ceb72ae96493c37249abb219d330d6d4d770e5eb2aca0a512da0da216323e4f989ea839368bc54790772d58582291ff814f574e13bb35388b6d1a3a")
                .addHeader("Cookie", "SESSDATA=c0ff6525%2C1722873629%2C461e8%2A21CjB-IJgvNmvt1Z0aeCc_XCoFPZg4eOi81ICvzgsVaGX1MkLiiTIAWwEs1mpyTs7sxzASVkg5dUNva3ZvLXphaXc4dTdXMTVyWGhndUxrXzdMVTFuQ2ZOUU9OX1p5Nk5PQW5EY1htMUdBSjNYSzc2RFpMT3hRRU1ZaXRyR1YyTjZuU1FZbGVKNzlBIIEC; SESSDATA=d4e5e5b0%2C1722881524%2Ca4dc8%2A21CjCs7ZpgjJd7IRGXwdYT3s0-QJpPXYhsInkfhaad1rifd6CqiwbrEL0UYVEy87egzk4SVl9sTVVJd2dzRmlBcE1iY2NHVE80amliODhyZjgtVGY5WnNHU2pva3lyNUl4djkwLVNMZ3BOd1ZXRFJ6SmdZQzc4ZG1UVVhGcmZVY2lDNGtTM09rMXJ3IIEC; bili_jct=faa8d461fee75bc6cfda840f399dc368; DedeUserID=296335306; DedeUserID__ckMd5=aed0d6cb356754b8; sid=g9xrptfj")
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
