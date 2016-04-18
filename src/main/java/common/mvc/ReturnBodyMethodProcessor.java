package common.mvc;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * Created by rq on 2016/4/14.
 */
public class ReturnBodyMethodProcessor implements HandlerMethodReturnValueHandler {
    /**
     * Whether the given {@linkplain MethodParameter method return type} is
     * supported by this handler.
     *
     * @param returnType the method return type to check
     * @return {@code true} if this handler supports the supplied return type;
     * {@code false} otherwise
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(ReturnBody.class) != null;
    }

    /**
     * Handle the given return value by adding attributes to the model and
     * setting a view or setting the
     * {@link ModelAndViewContainer#setRequestHandled} flag to {@code true}
     * to indicate the response has been handled directly.
     *
     * @param returnValue  the value returned from the handler method
     * @param returnType   the type of the return value. This type must have
     *                     previously been passed to {@link #supportsReturnType} which must
     *                     have returned {@code true}.
     * @param mavContainer the ModelAndViewContainer for the current request
     * @param webRequest   the current request
     * @throws Exception if the return value handling results in an error
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);
        HttpServletResponse nativeResponse = webRequest.getNativeResponse(HttpServletResponse.class);
        Method method = returnType.getMethod();
        ReturnBody jsonBody = method.getAnnotation(ReturnBody.class);
        nativeResponse.setContentType("application/javascript; charset=UTF-8");
        PrintWriter writer = nativeResponse.getWriter();
        writer.write(jsonBody.name() + ":" + returnValue.toString());
        writer.flush();
    }
}
