package plutarch.nlp.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.Date;
import java.text.ParseException;

/**
 * Created by joshs on 6/25/2017.
 */
public class FlexibleDateParser {
    private List<ThreadLocal<SimpleDateFormat>> threadLocals = new ArrayList<ThreadLocal<SimpleDateFormat>>();

    public FlexibleDateParser(List<String> formats, final TimeZone tz){
        threadLocals.clear();
        for (final String format : formats) {
            ThreadLocal<SimpleDateFormat> dateFormatTL = new ThreadLocal<SimpleDateFormat>() {
                protected SimpleDateFormat initialValue() {
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    sdf.setTimeZone(tz);
                    sdf.setLenient(false);
                    return sdf;
                }
            };
            threadLocals.add(dateFormatTL);
        }
    }

    public Date parseDate(String dateStr) throws ParseException {
        for (ThreadLocal<SimpleDateFormat> tl : threadLocals) {
            SimpleDateFormat sdf = tl.get();
            try {
                return sdf.parse(dateStr);
            } catch (ParseException e) {
                // Ignore and try next date parser
            }
        }
        // All parsers failed
        return null;
    }
}