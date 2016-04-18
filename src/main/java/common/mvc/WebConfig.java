package common.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.lang.reflect.Method;
import java.util.List;

/** 不需要使用<mvc:annotation-driven/>
 * Created by rq on 2016/4/14.
 */
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurationSupport {
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter();
        List<HandlerMethodReturnValueHandler> returnValueHandlers = getDefaultReturnValueHandlers(adapter);
        returnValueHandlers.add(0, new ReturnBodyMethodProcessor());
        adapter.setReturnValueHandlers(returnValueHandlers);

        return adapter;
    }

    @SuppressWarnings("unchecked")
    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers(RequestMappingHandlerAdapter adapter) {
        try {
            Method method = RequestMappingHandlerAdapter.class.getDeclaredMethod("getDefaultReturnValueHandlers");
            method.setAccessible(true);
            return (List<HandlerMethodReturnValueHandler>) method.invoke(adapter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
