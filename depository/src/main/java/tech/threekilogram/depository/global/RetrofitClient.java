package tech.threekilogram.depository.global;

import retrofit2.Retrofit;
import tech.threekilogram.depository.net.retrofit.get.GetService;

/**
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-07-31
 * @time: 10:26
 */
public class RetrofitClient {

      /**
       * this baseUrl means nothing, because real url is from {@link tech.threekilogram.depository.net.NetMapper}
       * to {@link GetService}'s params
       */
      public static Retrofit INSTANCE = new Retrofit
          .Builder()
          .baseUrl("https://github.com/")
          .build();
}