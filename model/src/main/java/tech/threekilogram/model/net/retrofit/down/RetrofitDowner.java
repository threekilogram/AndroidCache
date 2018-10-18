package tech.threekilogram.model.net.retrofit.down;

import java.io.File;
import java.io.IOException;
import tech.threekilogram.model.net.retrofit.loader.RetrofitLoader;

/**
 * 使用 retrofit 从网络下载文件,保存到指定文件夹
 *
 * @author liujin
 */
public class RetrofitDowner extends RetrofitLoader<File> {

      /**
       * @param dir 保存文件夹
       */
      public RetrofitDowner ( File dir ) {

            super( new BodyDownConverter( dir ) );
      }

      /**
       * @param dir 保存文件夹
       * @param maxSize 文件夹最大大小
       *
       * @throws IOException 创建文件夹失败
       */
      public RetrofitDowner ( File dir, long maxSize ) throws IOException {

            super( new BodyDownConverter( dir, maxSize ) );
      }

      /**
       * 获取设置的下载进度监听
       */
      public OnDownloadUpdateListener getOnDownLoadUpdateListener ( ) {

            return getDownConverter().mOnDownloadUpdateListener;
      }

      /**
       * 设置下载进度监听
       */
      public void setOnDownLoadUpdateListener (
          OnDownloadUpdateListener onDownloadUpdateListener ) {

            getDownConverter().mOnDownloadUpdateListener = onDownloadUpdateListener;
      }

      /**
       * 工具方法
       */
      private BodyDownConverter getDownConverter ( ) {

            return (BodyDownConverter) mNetConverter;
      }

      /**
       * @param url 获取该url对应的文件,文件可能不存在{@link File#exists()}
       *
       * @return 文件
       */
      public File getFile ( String url ) {

            return getDownConverter().getFileLoader().getFile( url );
      }

      /**
       * @return 保存文件夹
       */
      public File getDir ( ) {

            return getDownConverter().getDir();
      }

      /**
       * 删除该url对应的文件
       *
       * @param url url
       */
      public void removeFile ( String url ) {

            getDownConverter().getFileLoader().remove( url );
      }

      /**
       * 清除所有下载的文件
       */
      public void clearFile ( ) {

            getDownConverter().getFileLoader().clear();
      }

      /**
       * 加载文件如果文件已经存在将直接返回,否则返回从网络下载
       *
       * @param url key
       *
       * @return file
       */
      @Override
      public File load ( String url ) {

            BodyDownConverter converter = getDownConverter();
            File file = converter.getFileLoader().getFile( url );

            if( file != null && file.exists() ) {
                  OnDownloadUpdateListener onDownloadUpdateListener = getOnDownLoadUpdateListener();
                  if( onDownloadUpdateListener != null ) {
                        onDownloadUpdateListener.onFinished( url );
                  }
                  return file;
            }

            return super.load( url );
      }

      /**
       * 监视下载进度
       *
       * @author liujin
       */
      public interface OnDownloadUpdateListener {

            /**
             * 回调监听
             *
             * @param url url
             * @param total 数据总长
             * @param current 当前下载长度
             */
            void onProgressUpdate ( String url, long total, long current );

            /**
             * 下载完成的回调
             *
             * @param url url
             */
            void onFinished ( String url );
      }
}