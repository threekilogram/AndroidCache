package tech.threekilogram.depository.client;

import android.graphics.Bitmap;
import java.io.File;
import java.io.IOException;
import tech.threekilogram.depository.bitmap.BitmapConverter;
import tech.threekilogram.depository.bitmap.BitmapConverter.ScaleMode;
import tech.threekilogram.depository.file.converter.FileStreamConverter.OnProgressUpdateListener;
import tech.threekilogram.depository.memory.lru.MemoryBitmap;
import tech.threekilogram.depository.net.retrofit.loader.RetrofitDowner;

/**
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-08-16
 * @time: 21:44
 */
public class BitmapLoader {

      /**
       * 内存缓存
       */
      protected MemoryBitmap<String> mMemory;
      /**
       * bitmap 转换
       */
      protected BitmapConverter      mBitmapConverter;
      /**
       * 下载
       */
      protected RetrofitDowner       mDowner;

      public BitmapLoader ( int maxMemorySize, File cacheDir ) {

            mMemory = new MemoryBitmap<>( maxMemorySize );
            mDowner = new RetrofitDowner( cacheDir );
            mBitmapConverter = new BitmapConverter();
      }

      public BitmapLoader ( int maxMemorySize, File cacheDir, int maxFileSize ) throws IOException {

            mMemory = new MemoryBitmap<>( maxMemorySize );
            mDowner = new RetrofitDowner( cacheDir, maxFileSize );
            mBitmapConverter = new BitmapConverter();
      }

      /**
       * 配置bitmap加载配置
       *
       * @param width 需求宽度
       * @param height 需求高度
       * @param scaleMode 读取方式
       */
      public void configBitmap ( int width, int height, @ScaleMode int scaleMode ) {

            mBitmapConverter.setWidth( width );
            mBitmapConverter.setHeight( height );
            mBitmapConverter.setMode( scaleMode );
      }

      /**
       * 从内存读取
       *
       * @param url url
       *
       * @return bitmap or null
       */
      public Bitmap loadFromMemory ( String url ) {

            return mMemory.load( url );
      }

      /**
       * @return 当前使用内存大小
       */
      public int memorySize ( ) {

            return mMemory.size();
      }

      /**
       * 清空内存
       */
      public void clearMemory ( ) {

            mMemory.clear();
      }

      /**
       * 从本地文件读取
       *
       * @param url url
       *
       * @return bitmap or null
       */
      public Bitmap loadFromFile ( String url ) {

            File file = mDowner.getFile( url );
            if( file != null ) {

                  Bitmap bitmap = mBitmapConverter.read( file );
                  mMemory.save( url, bitmap );
                  return bitmap;
            }
            return null;
      }

      /**
       * get url file
       *
       * @param url url
       *
       * @return file or null
       */
      public File getFile ( String url ) {

            return mDowner.getFile( url );
      }

      /**
       * file dir
       *
       * @return dir
       */
      public File getDir ( ) {

            return mDowner.getDir();
      }

      /**
       * 从网络读取
       *
       * @param url url
       *
       * @return bitmap or null
       */
      public Bitmap loadFromNet ( String url ) {

            File file = mDowner.load( url );
            if( file != null ) {

                  Bitmap bitmap = mBitmapConverter.read( file );
                  mMemory.save( url, bitmap );
                  return bitmap;
            }
            return null;
      }

      /**
       * 获取设置的下载进度监听
       *
       * @return 监听
       */
      public OnProgressUpdateListener getOnProgressUpdateListener ( ) {

            return mDowner.getOnProgressUpdateListener();
      }

      /**
       * 设置下载进度监听
       *
       * @param onProgressUpdateListener 监听
       */
      public void setOnProgressUpdateListener (
          OnProgressUpdateListener onProgressUpdateListener ) {

            mDowner.setOnProgressUpdateListener( onProgressUpdateListener );
      }
}