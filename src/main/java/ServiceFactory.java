
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.time.Instant;

public interface ServiceFactory {


    static Object createService(final Object realObject) {

        final ClassLoader classLoader = realObject.getClass().getClassLoader();
        final Class[] classes = realObject.getClass().getInterfaces();

        Object object = Proxy.newProxyInstance(classLoader, classes, new InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Method method1 = realObject.getClass().getMethod(method.getName(), method.getParameterTypes());
                Object object;

                if (method1.getAnnotation(ExcuteTime.class) != null) {

                    Instant start = Instant.now();

                    object = method.invoke(realObject, args);

                    Instant finish = Instant.now();

                    long timeElapsed = Duration.between(start, finish).toMillis();
                    System.out.println("Method : " + method.getName() + " excute success in : " + timeElapsed + " ms.");

                } else {

                    object = method.invoke(realObject, args);

                }

                return object;
            }

        });

        return object;

    }


}
