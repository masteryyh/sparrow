package win.minaandyyh.sparrow.metadata;

import win.minaandyyh.sparrow.annotation.param.Body;
import win.minaandyyh.sparrow.annotation.param.Cookie;
import win.minaandyyh.sparrow.annotation.param.Header;
import win.minaandyyh.sparrow.annotation.param.Param;
import win.minaandyyh.sparrow.annotation.param.UrlVariable;

import java.lang.reflect.Parameter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * HTTP request value metadata reader
 *
 * @author masteryyh
 */
public class HttpValueMetadataReader {
    private HttpValueMetadataReader() {}

    private static volatile HttpValueMetadataReader instance;

    private static final Lock LOCK = new ReentrantLock();

    public static HttpValueMetadataReader getInstance() {
        if (instance == null) {
            LOCK.lock();
            try {
                if (instance == null) {
                    instance = new HttpValueMetadataReader();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    private HttpValueMetadata tryReadParam(Parameter parameter) {
        Param annotation = parameter.getAnnotation(Param.class);
        if (annotation == null) {
            return null;
        }

        return new HttpValueMetadata()
                .name(annotation.name())
                .valueClass(parameter.getType())
                .type(ValueType.PARAM);
    }

    private HttpValueMetadata tryReadBody(Parameter parameter) {
        Body annotation = parameter.getAnnotation(Body.class);
        if (annotation == null) {
            return null;
        }

        return new HttpValueMetadata()
                .valueClass(parameter.getType())
                .type(ValueType.BODY);
    }

    private HttpValueMetadata tryReadHeader(Parameter parameter) {
        Header annotation = parameter.getAnnotation(Header.class);
        if (annotation == null) {
            return null;
        }

        return new HttpValueMetadata()
                .name(annotation.name())
                .valueClass(parameter.getType())
                .type(ValueType.HEADER);
    }

    private HttpValueMetadata tryReadCookie(Parameter parameter) {
        Cookie annotation = parameter.getAnnotation(Cookie.class);
        if (annotation == null) {
            return null;
        }

        return new HttpValueMetadata()
                .name(annotation.name())
                .valueClass(parameter.getType())
                .type(ValueType.COOKIE);
    }

    private HttpValueMetadata tryReadUrlVariable(Parameter parameter) {
        UrlVariable annotation = parameter.getAnnotation(UrlVariable.class);
        if (annotation == null) {
            return null;
        }

        return new HttpValueMetadata()
                .name(annotation.name())
                .valueClass(parameter.getType())
                .type(ValueType.URL_VARIABLE);
    }

    public HttpValueMetadata read(Parameter parameter) {
        if (parameter == null) {
            throw new NullPointerException("Parameter object is null.");
        }

        HttpValueMetadata metadata = tryReadBody(parameter);
        if (metadata != null) {
            return metadata;
        }
        metadata = tryReadParam(parameter);
        if (metadata != null) {
            return metadata;
        }
        metadata = tryReadHeader(parameter);
        if (metadata != null) {
            return metadata;
        }
        metadata = tryReadCookie(parameter);
        if (metadata != null) {
            return metadata;
        }
        metadata = tryReadUrlVariable(parameter);
        if (metadata != null) {
            return metadata;
        }

        throw new IllegalArgumentException("Invalid parameter - not annotated with any HTTP value annotation.");
    }
}
