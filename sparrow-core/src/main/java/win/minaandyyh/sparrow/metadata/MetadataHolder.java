package win.minaandyyh.sparrow.metadata;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Request client metadata holder
 *
 * @author masteryyh
 */
public class MetadataHolder {
    private final RequestClientMetadataReader reader = RequestClientMetadataReader.getInstance();

    private final LoadingCache<Class<?>, RequestClientMetadata> metadataCache = CacheBuilder.newBuilder()
            .initialCapacity(16)
            .expireAfterWrite(12L, TimeUnit.HOURS)
            .build(new CacheLoader<>() {
                @Override
                public RequestClientMetadata load(Class<?> key) {
                    return reader.read(key);
                }
            });

    private MetadataHolder() {}

    private static volatile MetadataHolder instance;

    private static final Lock LOCK = new ReentrantLock();

    public static MetadataHolder getInstance() {
        if (instance == null) {
            LOCK.lock();
            try {
                if (instance == null) {
                    instance = new MetadataHolder();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public RequestClientMetadata get(Class<?> clazz) {
        return metadataCache.getUnchecked(clazz);
    }
}
