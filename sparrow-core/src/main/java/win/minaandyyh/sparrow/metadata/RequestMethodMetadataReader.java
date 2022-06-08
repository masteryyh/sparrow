package win.minaandyyh.sparrow.metadata;

import org.apache.commons.lang3.ArrayUtils;
import win.minaandyyh.sparrow.MediaType;
import win.minaandyyh.sparrow.RequestMethod;
import win.minaandyyh.sparrow.annotation.request.Delete;
import win.minaandyyh.sparrow.annotation.request.Get;
import win.minaandyyh.sparrow.annotation.request.Patch;
import win.minaandyyh.sparrow.annotation.request.Post;
import win.minaandyyh.sparrow.annotation.request.Put;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * HTTP request method metadata reader
 *
 * @author masteryyh
 */
public class RequestMethodMetadataReader {
    private RequestMethodMetadataReader() {}

    private static volatile RequestMethodMetadataReader instance;

    private static final Lock LOCK = new ReentrantLock();

    public static RequestMethodMetadataReader getInstance() {
        if (instance == null) {
            LOCK.lock();
            try {
                if (instance == null) {
                    instance = new RequestMethodMetadataReader();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    private List<HttpValueMetadata> readParameters(Parameter[] parameters) {
        if (ArrayUtils.isEmpty(parameters)) {
            return Collections.emptyList();
        }

        HttpValueMetadataReader valueReader = HttpValueMetadataReader.getInstance();
        List<HttpValueMetadata> result = new ArrayList<>(parameters.length);
        for (Parameter parameter : parameters) {
            result.add(valueReader.read(parameter));
        }

        return result;
    }

    private RequestMethodMetadata tryReadGet(Method method) {
        Get annotation = method.getAnnotation(Get.class);
        if (annotation == null) {
            return null;
        }

        return new RequestMethodMetadata()
                .name(method.getName())
                .path(annotation.path())
                .method(RequestMethod.GET)
                .consumes(MediaType.NONE)
                .produces(annotation.produces())
                .responseClass(method.getReturnType())
                .parameters(readParameters(method.getParameters()));
    }

    private RequestMethodMetadata tryReadPost(Method method) {
        Post annotation = method.getAnnotation(Post.class);
        if (annotation == null) {
            return null;
        }

        return new RequestMethodMetadata()
                .name(method.getName())
                .path(annotation.path())
                .method(RequestMethod.POST)
                .consumes(annotation.consumes())
                .produces(annotation.produces())
                .responseClass(method.getReturnType())
                .parameters(readParameters(method.getParameters()));
    }

    private RequestMethodMetadata tryReadPut(Method method) {
        Put annotation = method.getAnnotation(Put.class);
        if (annotation == null) {
            return null;
        }

        return new RequestMethodMetadata()
                .name(method.getName())
                .path(annotation.path())
                .method(RequestMethod.PUT)
                .consumes(annotation.consumes())
                .produces(annotation.produces())
                .responseClass(method.getReturnType())
                .parameters(readParameters(method.getParameters()));
    }

    private RequestMethodMetadata tryReadPatch(Method method) {
        Patch annotation = method.getAnnotation(Patch.class);
        if (annotation == null) {
            return null;
        }

        return new RequestMethodMetadata()
                .name(method.getName())
                .path(annotation.path())
                .method(RequestMethod.PATCH)
                .consumes(annotation.consumes())
                .produces(annotation.produces())
                .responseClass(method.getReturnType())
                .parameters(readParameters(method.getParameters()));
    }

    private RequestMethodMetadata tryReadDelete(Method method) {
        Delete annotation = method.getAnnotation(Delete.class);
        if (annotation == null) {
            return null;
        }

        return new RequestMethodMetadata()
                .name(method.getName())
                .path(annotation.path())
                .method(RequestMethod.DELETE)
                .consumes(annotation.consumes())
                .produces(annotation.produces())
                .responseClass(method.getReturnType())
                .parameters(readParameters(method.getParameters()));
    }

    public RequestMethodMetadata read(Method method) {
        if (method == null) {
            throw new NullPointerException("Method object is null.");
        }

        RequestMethodMetadata metadata = tryReadGet(method);
        if (metadata != null) {
            return metadata;
        }
        metadata = tryReadPost(method);
        if (metadata != null) {
            return metadata;
        }
        metadata = tryReadPut(method);
        if (metadata != null) {
            return metadata;
        }
        metadata = tryReadPatch(method);
        if (metadata != null) {
            return metadata;
        }
        metadata = tryReadDelete(method);
        if (metadata != null) {
            return metadata;
        }

        throw new IllegalArgumentException("Invalid request method - not annotated with any valid HTTP request annotation.");
    }
}
