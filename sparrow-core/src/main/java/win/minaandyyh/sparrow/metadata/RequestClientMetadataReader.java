package win.minaandyyh.sparrow.metadata;

import org.apache.commons.lang3.ArrayUtils;
import win.minaandyyh.sparrow.annotation.SparrowClient;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * HTTP request client metadata reader
 *
 * @author masteryyh
 */
public class RequestClientMetadataReader {
    private RequestClientMetadataReader() {}

    private static volatile RequestClientMetadataReader instance;

    private static final Lock LOCK = new ReentrantLock();

    public static RequestClientMetadataReader getInstance() {
        if (instance == null) {
            LOCK.lock();
            try {
                if (instance == null) {
                    instance = new RequestClientMetadataReader();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    public Map<String, RequestMethodMetadata> readMethods(Method[] methods) {
        if (ArrayUtils.isEmpty(methods)) {
            return Collections.emptyMap();
        }

        RequestMethodMetadataReader reader = RequestMethodMetadataReader.getInstance();
        Map<String, RequestMethodMetadata> result = new LinkedHashMap<>();
        for (Method method : methods) {
            result.put(method.getName(), reader.read(method));
        }

        return result;
    }

    public RequestClientMetadata read(Class<?> clientClass) {
        if (clientClass == null) {
            throw new NullPointerException("Class object is null.");
        }

        SparrowClient annotation = clientClass.getAnnotation(SparrowClient.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Invalid client class - not annotated with @SparrowClient annotation.");
        }

        return new RequestClientMetadata()
                .name(clientClass.getName())
                .url(annotation.url())
                .methods(readMethods(clientClass.getMethods()));
    }
}
