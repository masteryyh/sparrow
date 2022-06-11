package win.minaandyyh.sparrow.spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import win.minaandyyh.sparrow.Sparrow;
import win.minaandyyh.sparrow.codec.Decoder;
import win.minaandyyh.sparrow.codec.Encoder;
import win.minaandyyh.sparrow.http.RequestHandler;
import win.minaandyyh.sparrow.spring.SparrowCreationException;

/**
 * Sparrow client factory bean
 *
 * @author masteryyh
 */
public class SparrowClientFactoryBean<T> implements FactoryBean<T>, BeanFactoryAware {
    private final Class<T> beanClass;

    private BeanFactory beanFactory;

    public SparrowClientFactoryBean(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public T getObject() {
        RequestHandler handler;
        Encoder encoder;
        Decoder decoder;
        try {
            handler = beanFactory.getBean(RequestHandler.class);
            encoder = beanFactory.getBean(Encoder.class);
            decoder = beanFactory.getBean(Decoder.class);
        } catch (NoUniqueBeanDefinitionException e) {
            throw new SparrowCreationException("Unable to create Sparrow client, multiple bean instances of " + e.getBeanName() + " found.", e);
        } catch (NoSuchBeanDefinitionException e) {
            throw new SparrowCreationException("Unable to create Sparrow client, a required bean not found.", e);
        }

        return Sparrow.<T>newClient()
                .handler(handler)
                .client(beanClass)
                .encoder(encoder)
                .decoder(decoder)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return beanClass;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
