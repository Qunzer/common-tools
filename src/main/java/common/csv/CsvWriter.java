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
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by rq on 2016/4/20.
 */
public class CsvWriter {
    /**
     * 默认的单元格数据处理器
     */
    private static final CellProcessor DEFAULT_CELL_PROCESSOR = new DefaultCellProcess();

    /**
     * @param objects             数据对象列表
     * @param beanNameAndTitleMap 对象的beanName（key）和标题（value)的映射关系
     * @param writer
     */
    public static <T> void write(Iterable<T> objects, LinkedHashMap<String, String> beanNameAndTitleMap, Writer writer) throws IOException {
        write(objects, beanNameAndTitleMap, writer, DEFAULT_CELL_PROCESSOR);
    }

    /**
     * 将数据输出到writer
     *
     * @param objects             数据对象列表
     * @param beanNameAndTitleMap 对象的beanName（key）和标题（value)的映射关系
     * @param writer
     */
    public static <T> void write(Iterable<T> objects, LinkedHashMap<String, String> beanNameAndTitleMap, Writer writer, CellProcessor cellProcessor) throws IOException {
        checkArgument(beanNameAndTitleMap != null && !beanNameAndTitleMap.isEmpty() && cellProcessor != null, "title,bean map,cellProcessor is null");

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
        ICsvBeanWriter beanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);

        try {
            // 写标题
            beanWriter.writeHeader(titles);
            beanWriter.flush();

            // 写数据
            for (Object object : objects) {
                beanWriter.write(object, beanNames, processors);
            }
            beanWriter.flush();
        } finally {
            beanWriter.close();
        }
    }

    /**
     * @param objects
     * @param beanNameAndTitleMap
     * @param writer
     * @param <T>
     * @throws IOException
     */
    public static <T> void writeListObj(Iterable<List<T>> objects, LinkedHashMap<String, String> beanNameAndTitleMap, Writer writer) throws IOException {
        writeListObj(objects, beanNameAndTitleMap, writer, DEFAULT_CELL_PROCESSOR);
    }

    /**
     * 将数据输出到writer
     *
     * @param objects             数据对象列表
     * @param beanNameAndTitleMap 对象的beanName（key）和标题（value)的映射关系
     * @param writer
     */
    public static <T> void writeListObj(Iterable<List<T>> objects, LinkedHashMap<String, String> beanNameAndTitleMap, Writer writer, CellProcessor cellProcessor) throws IOException {
        checkArgument(beanNameAndTitleMap != null && !beanNameAndTitleMap.isEmpty() && cellProcessor != null, "title,bean map,cellProcessor is null");

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
        ICsvBeanWriter beanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);

        try {
            // 写标题
            beanWriter.writeHeader(titles);
            beanWriter.flush();

            // 写数据
            for (List object : objects) {
                for (Object obj : object) {
                    beanWriter.write(obj, beanNames, processors);
                    beanWriter.flush();
                }
            }
            beanWriter.flush();
        } finally {
            // 关闭writer
            beanWriter.close();
        }
    }

    /**
     * @param iterables
     * @param beanNameAndTitleMap
     * @param writer
     * @param <T>
     * @throws IOException
     */
    public static <T> void writeListObjs(List<Iterable<List<T>>> iterables, LinkedHashMap<String, String> beanNameAndTitleMap, Writer writer) throws IOException {
        writeListObjs(iterables, beanNameAndTitleMap, writer, DEFAULT_CELL_PROCESSOR);
    }

    /**
     * @param iterables
     * @param beanNameAndTitleMap
     * @param writer
     * @param cellProcessor
     * @param <T>
     * @throws IOException
     */
    public static <T> void writeListObjs(List<Iterable<List<T>>> iterables, LinkedHashMap<String, String> beanNameAndTitleMap, Writer writer, CellProcessor cellProcessor) throws IOException {
        checkArgument(beanNameAndTitleMap != null && !beanNameAndTitleMap.isEmpty() && cellProcessor != null, "title,bean map,cellProcessor is null");
        if (Iterables.isEmpty(iterables)) {
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
        ICsvBeanWriter beanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);

        try {
            // 写标题
            beanWriter.writeHeader(titles);
            beanWriter.flush();

            // 写数据
            for (Iterable<List<T>> iterable : iterables) {
                for (List<T> list : iterable) {
                    for (T t : list) {
                        beanWriter.write(t, beanNames, processors);
                    }
                    //推出一部分数据
                    beanWriter.flush();
                }
            }
            // 刷出数据
            beanWriter.flush();
        } finally {
            // 关闭writer
            beanWriter.close();
        }
    }
}
