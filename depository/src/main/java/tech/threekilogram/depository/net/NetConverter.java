package tech.threekilogram.depository.net;

import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * 使用 {@link K} 类型向网络发送请求,收到 {@link P} 类型响应数据,转为 {@link V} 类型结果
 *
 * @param <K> key 类型
 * @param <V> value 类型
 * @param <P> 网络响应类型
 *
 * @author liujin
 */
public interface NetConverter<K, V, P> extends UrlConverter<K> {

      /**
       * get a success response then convert it to value
       *
       * @param key key
       * @param response response
       *
       * @return value
       *
       * @throws Exception when convert  {@link ResponseBody} to {@link V} may occur a exception
       *                   {@see #onConvertException(Object, String, Exception)}
       */
      V onExecuteSuccess (K key, P response) throws Exception;

      /**
       * when cant get a correct response {@link Response#isSuccessful()} return failed
       *
       * @param key key
       * @param httpCode http code
       * @param errorResponse error body
       */
      void onExecuteFailed (K key, int httpCode, P errorResponse);

      /**
       * @author: Liujin
       * @version: V1.0
       * @date: 2018-08-05
       * @time: 12:41
       */
      interface NetExceptionHandler<K> {

            /**
             * 当从网络下载的文件转换时的异常{@link #onExecuteSuccess(Object, Object)}
             *
             * @param key key key
             * @param url url url from {@link #urlFromKey(Object)}
             * @param e exception exception
             */
            void onConvertException (K key, String url, Exception e);

            /**
             * 无法连接网络
             *
             * @param key key key
             * @param url url url from {@link #urlFromKey(Object)}
             * @param e exception exception
             */
            void onConnectException (K key, String url, IOException e);
      }
}