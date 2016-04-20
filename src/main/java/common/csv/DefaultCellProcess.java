package common.csv;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

import java.math.BigDecimal;
import java.util.Date;

/**
 * default  bean processor
 * <p>
 * Created by rq on 2016/4/20.
 */
public class DefaultCellProcess implements CellProcessor {
    private static final String PATTERN = "yyyy-MM-dd";

    /**
     * This method is invoked by the framework when the processor needs to process data or check constraints.
     *
     * @param value   the value to be processed
     * @param context the CSV context
     * @return the result of cell processor execution
     * @since 1.0
     */
    @Override
    public Object execute(Object value, CsvContext context) {
        if (value == null) {
            return "";
        }
        if (value instanceof String) {
            return value;
        }
        if (value instanceof Date) {
            return DateFormatUtils.format((Date) value, PATTERN);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toPlainString();
        }
        return value;
    }
}
