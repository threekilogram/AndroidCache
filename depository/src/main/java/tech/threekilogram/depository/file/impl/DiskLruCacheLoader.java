package tech.threekilogram.depository.file.impl;

import com.jakewharton.disklrucache.DiskLruCache;
import com.jakewharton.disklrucache.DiskLruCache.Editor;
import com.jakewharton.disklrucache.DiskLruCache.Snapshot;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import tech.threekilogram.depository.file.BaseFileLoadSupport;
import tech.threekilogram.depository.file.FileConverter;
import tech.threekilogram.depository.function.CloseFunction;

/**
 * 底层使用{@link DiskLruCache}缓存数据到文件夹
 *
 * @param <K> kye 类型
 * @param <V> value 类型
 *
 * @author liujin
 */
public class DiskLruCacheLoader<K, V> extends BaseFileLoadSupport<K, V> {

      /**
       * 保存数据
       */
      private DiskLruCache mDiskLruCache;

      /**
       * 辅助该类完成stream到{@link V}的转换工作
       */
      private FileConverter<K, V> mConverter;

      /**
       * @param folder which dir to save data
       * @param maxSize max data size
       * @param converter function to do
       *
       * @throws IOException 创建缓存文件异常
       */
      public DiskLruCacheLoader (
          File folder,
          long maxSize,
          FileConverter<K, V> converter) throws IOException {
            /* create DiskLruCache */

            mDiskLruCache = DiskLruCache.open(folder, 1, 1, maxSize);

            mConverter = converter;
      }

      @Override
      public V save (K key, V value) {

            String name = mConverter.fileName(key);

            V result = null;

            if(mSaveStrategy == SAVE_STRATEGY_RETURN_OLD) {
                  result = load(key);
            }

            try {
                  mDiskLruCache.remove(name);
            } catch(IOException e) {
                  e.printStackTrace();
            }

            Editor editor = null;

            try {
                  editor = mDiskLruCache.edit(name);
            } catch(IOException e) {
                  e.printStackTrace();
            }
            if(editor == null) {
                  return null;
            }

            OutputStream outputStream = null;

            try {
                  outputStream = editor.newOutputStream(0);
            } catch(IOException e) {
                  e.printStackTrace();
            }
            if(outputStream == null) {
                  return null;
            }
            try {

                  mConverter.saveValue(key, outputStream, value);
                  CloseFunction.close(outputStream);
                  editor.commit();
            } catch(IOException e) {
                  e.printStackTrace();
                  abortEditor(editor);
                  if(mExceptionHandler != null) {
                        mExceptionHandler.onSaveValueToFile(e, key, value);
                  }
            } finally {

                  CloseFunction.close(outputStream);
            }

            try {
                  mDiskLruCache.flush();
            } catch(IOException e) {
                  e.printStackTrace();
            }

            return result;
      }

      private void abortEditor (Editor edit) {

            try {
                  if(edit != null) {
                        edit.abort();
                  }
            } catch(IOException e) {

                  e.printStackTrace();
            }
      }

      @Override
      public V remove (K key) {

            V result = null;

            if(mSaveStrategy == SAVE_STRATEGY_RETURN_OLD) {
                  result = load(key);
            }

            try {
                  String fileName = mConverter.fileName(key);
                  mDiskLruCache.remove(fileName);
            } catch(IOException e) {

                  e.printStackTrace();
            }
            return result;
      }

      @Override
      public V load (K key) {

            String stringKey = mConverter.fileName(key);

            /* try to get snapShort */

            Snapshot snapshot = null;
            InputStream inputStream = null;
            try {

                  snapshot = mDiskLruCache.get(stringKey);
            } catch(IOException e) {

                  e.printStackTrace();
            }

            /* try to load value from snapShot's stream */

            if(snapshot != null) {

                  inputStream = snapshot.getInputStream(0);

                  try {

                        return mConverter.toValue(key, inputStream);
                  } catch(Exception e) {

                        e.printStackTrace();

                        if(mExceptionHandler != null) {
                              mExceptionHandler.onConvertToValue(e, key);
                        }
                  } finally {

                        CloseFunction.close(inputStream);
                        CloseFunction.close(snapshot);
                  }
            }

            return null;
      }

      @Override
      public boolean containsOf (K key) {

            String name = mConverter.fileName(key);

            try {

                  Snapshot snapshot = mDiskLruCache.get(name);
                  boolean result = snapshot != null;
                  if(result) {
                        snapshot.close();
                  }
                  return result;
            } catch(IOException e) {

                  e.printStackTrace();
            }
            return false;
      }
}
