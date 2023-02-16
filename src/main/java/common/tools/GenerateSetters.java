package common.tools;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;

import java.lang.reflect.Field;

/**
 * 生成set方法，便于进行逻辑复制
 * <p>
 *
 * @author yuanrq
 * @date 2017/04/14
 */
public class GenerateSetters {
    private static final Joiner joiner_space = Joiner.on(" ");
    private static final Joiner joiner_no_space = Joiner.on("");

    public static void generateSetters(Class clazz) {
        String simpleName = clazz.getSimpleName();
        String declareName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, simpleName);
        System.out.println(joiner_space.join(simpleName, declareName, "=new", simpleName, "();"));
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(joiner_no_space.join(declareName, ".", "set", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, declaredField.getName()), "();"));
        }
    }

    public static void main(String[] args) {
        generateSetters(Foo.class);
    }


}
