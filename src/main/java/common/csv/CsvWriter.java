package common.csv;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by rq on 2016/4/20.
 */
public class CsvWriter {
    /**
     * 将数据输出到writer
     *
     * @param objects             数据对象列表
     * @param beanNameAndTitleMap 对象的beanName（key）和标题（value)的映射关系
     * @param writer
     */
    public static void write(Iterable<?> objects, LinkedHashMap<String, String> beanNameAndTitleMap, Writer writer) throws IOException {
        write(objects, beanNameAndTitleMap, writer, new DefaultCellProcess());
    }

    /**
     * 将数据输出到writer
     *
     * @param objects             数据对象列表
     * @param beanNameAndTitleMap 对象的beanName（key）和标题（value)的映射关系
     * @param writer
     */
    public static void write(Iterable<?> objects, LinkedHashMap<String, String> beanNameAndTitleMap, Writer writer, CellProcessor cellProcessor) throws IOException {
        checkArgument(beanNameAndTitleMap != null && !beanNameAndTitleMap.isEmpty(), "title and bean map is null");

        if (Iterables.isEmpty(objects)) {
            return;
        }

        // 设置单元格的处理器
        int size = beanNameAndTitleMap.size();
        CellProcessor[] processors = new CellProcessor[size];
        Arrays.fill(processors, cellProcessor);

        // 得到bean的名称列表
        String[] beanNames = FluentIterable.from(beanNameAndTitleMap.keySet()).toArray(String.class);

        // 得到标题
        String[] titles = FluentIterable.from(beanNameAndTitleMap.values()).toArray(String.class);

        // 定义bean writer的格式
        try (ICsvBeanWriter beanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE)) {
            // 写标题
            beanWriter.writeHeader(titles);

            // 写数据
            for (Object object : objects) {
                beanWriter.write(object, beanNames, processors);
            }

            // 刷出数据
            beanWriter.flush();
        }
    }
}
