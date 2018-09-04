package tech.threekilogram.depository.memory;

import tech.threekilogram.depository.Container;

/**
 * 这是内存缓存的总接口,所有内存缓存均实现了该接口:
 * {@link tech.threekilogram.depository.memory.map.MemoryMap}
 * {@link tech.threekilogram.depository.memory.map.MemoryList}
 * {@link tech.threekilogram.depository.memory.lru.MemoryLruCache}
 *
 * @author liujin
 */
public interface Memory<K, V> extends Container<K, V> {

      /**
       * 清空
       */
      void clear ( );

      /**
       * 数据量
       *
       * @return 数据量
       */
      int size ( );
}
