import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.time.Instant;

public interface ServiceFactory {

    static Object createService(final Object realObject) {

        final ClassLoader classLoader = realObject.getClass().getClassLoader();
        final Class[] classes = realObject.getClass().getInterfaces();

        return Proxy.newProxyInstance(classLoader, classes, (proxy, method, args) -> {

            ExcuteTime excuteTime = realObject.getClass().getMethod(method.getName(), method.getParameterTypes()).getAnnotation(ExcuteTime.class);
            Object object;

            if (excuteTime.printExcuteTime()) {

                Instant start = Instant.now();

                object = method.invoke(realObject, args);

                Instant finish = Instant.now();

                long timeElapsed = Duration.between(start, finish).toMillis();
                System.out.println("Method : " + method.getName() + " excute success in : " + timeElapsed + " ms.");

            } else {

                object = method.invoke(realObject, args);

            }

            return object;

        });
    }

    static Object createService(final Class realClass) throws IllegalAccessException, InstantiationException, ClassNotFoundException {

        Object object;
        Class c = Class.forName(realClass.getName());
        object = c.newInstance();

        final ClassLoader classLoader = object.getClass().getClassLoader();
        final Class[] classes = object.getClass().getInterfaces();

        return Proxy.newProxyInstance(classLoader, classes, (proxy, method, args) -> {

            ExcuteTime excuteTime = object.getClass().getMethod(method.getName(), method.getParameterTypes()).getAnnotation(ExcuteTime.class);
            Object objectInner;

            if (excuteTime.printExcuteTime()) {

                Instant start = Instant.now();

                objectInner = method.invoke(object, args);

                Instant finish = Instant.now();

                long timeElapsed = Duration.between(start, finish).toMillis();
                System.out.println("Method : " + method.getName() + " excute success in : " + timeElapsed + " ms.");

            } else {

                objectInner = method.invoke(object, args);

            }

            return objectInner;
        });

    }

}
