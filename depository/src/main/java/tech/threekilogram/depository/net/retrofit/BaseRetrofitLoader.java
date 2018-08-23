package tech.threekilogram.depository.net.retrofit;

import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import tech.threekilogram.depository.instance.RetrofitClient;
import tech.threekilogram.depository.net.BaseNetLoader;

/**
 * 该类是{@link BaseNetLoader}的retrofit实现版本,用于使用retrofit从网络获取value,需要配置Service才能正常工作
 *
 * @param <V> value 类型
 * @param <S> 用于{@link retrofit2.Retrofit#create(Class)}中的class类型,即:retrofit服务类型
 *
 * @author liujin
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseRetrofitLoader<V, S> extends BaseNetLoader<V, ResponseBody> {

      /**
       * retrofit 客户端
       */
      protected Retrofit mRetrofit = RetrofitClient.INSTANCE;
      /**
       * retrofit service 类型
       */
      protected Class<S> mServiceType;
      /**
       * 创建的service
       */
      protected S        mService;

      /**
       * 最少需要这两个才能正常工作
       *
       * @param serviceType 服务类型
       * @param netConverter 转换器
       */
      protected BaseRetrofitLoader (
          Class<S> serviceType,
          BaseRetrofitConverter<V> netConverter ) {

            mNetConverter = netConverter;
            mServiceType = serviceType;
      }

      public Retrofit getRetrofit ( ) {

            return mRetrofit;
      }

      /**
       * 设置一个新的 {@link Retrofit}
       *
       * @param retrofit 新的{@link Retrofit}
       */
      public void setRetrofit ( Retrofit retrofit ) {

            mRetrofit = retrofit;
      }

      @Override
      public V load ( String key ) {

            /* 1. 获得url */

            /* 2. 制造一个call对象 */
            if( mService == null ) {

                  mService = mRetrofit.create( mServiceType );
            }
            Call<ResponseBody> call = configService( key, mService );

            /* 3. 执行call */
            try {
                  Response<ResponseBody> response = call.execute();

                  /* 4. 如果成功获得数据 */
                  if( response.isSuccessful() ) {

                        ResponseBody responseBody = response.body();

                        try {

                              /* 5. 转换数据 */
                              return mNetConverter.onExecuteSuccess( key, responseBody );
                        } catch(Exception e) {

                              e.printStackTrace();
                              /* 6. 转换异常 */
                              if( mExceptionHandler != null ) {
                                    mExceptionHandler.onConvertException( key, e );
                              }
                        }
                  } else {

                        /* 4. 连接到网络,但是没有获取到数据 */
                        if( mNoResourceHandler != null ) {
                              mNoResourceHandler.onExecuteFailed( key, response.code() );
                        }
                  }
            } catch(IOException e) {

                  /* 4. 没有连接到网络 */
                  e.printStackTrace();
                  if( mExceptionHandler != null ) {
                        mExceptionHandler.onConnectException( key, e );
                  }
            }

            return null;
      }

      /**
       * config retrofit service
       *
       * @param key key
       * @param service service to config
       *
       * @return a call to execute
       */
      protected abstract Call<ResponseBody> configService ( String key, S service );
}
