package plutarch.nlp.helper;

import plutarch.nlp.model.HistoryDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by joshs on 6/25/2017.
 */
public class DateHelper {
    private static DateHelper instance;
    public static DateHelper getInstance() {
        if (instance == null) {
            instance = new DateHelper();

            // Preload with known values to skip parsing
            instance.parseExceptionCache.add("now");
            instance.parseExceptionCache.add("the present day");
            instance.parseExceptionCache.add("past");
            instance.parseExceptionCache.add("the day");
            instance.parseExceptionCache.add("the future");
            instance.parseExceptionCache.add("future");
            instance.parseExceptionCache.add("time");
            instance.parseExceptionCache.add("fall");
            instance.parseExceptionCache.add("supper");
            instance.parseExceptionCache.add("once");
        }
        return instance;
    }

    private HashMap<String, HistoryDate> dateCache = new HashMap<String, HistoryDate>();
    private List<String> parseExceptionCache = new ArrayList<String>();

    public HistoryDate Parse(String dateString) {
        // Cleanse the datestring
        dateString = dateString.replace(",", "");

        // Check cache for values
        if (dateCache.get(dateString) != null) {
            return dateCache.get(dateString);
        }
        if (parseExceptionCache.contains(dateString.toLowerCase())) {
            return null;
        }

        // Check for a year pattern with a regex
        Pattern yearStart = Pattern.compile("^\\d{3,4}.*$");
        Matcher matcher = yearStart.matcher(dateString);
        Boolean startsWithYear = matcher.matches();

        List<String> dateFormats = new ArrayList<String>();
        if (startsWithYear) {
            dateFormats.add("yyyy-MM-dd GG");
            dateFormats.add("yyyy/MM/dd GG");

            dateFormats.add("yyyy-MM-dd");
            dateFormats.add("yyyy/MM/dd");

            dateFormats.add("yyyy MMMM dd GG");
            dateFormats.add("yyyy MMMM dd");

            dateFormats.add("yyyy GG");
            dateFormats.add("yyyy");
        }

        dateFormats.add("MM-dd-yyyy GG");
        dateFormats.add("MM/dd/yyyy GG");
        dateFormats.add("dd MMMM yyyy GG");
        dateFormats.add("dd MMMM yyy GG");

        dateFormats.add("MM-dd-yyyy");
        dateFormats.add("MM/dd/yyyy");
        dateFormats.add("dd MMMM yyyy");

        dateFormats.add("MMMM dd yyyy GG");
        dateFormats.add("MMMM yyyy GG");
        dateFormats.add("MM yyyy GG");

        dateFormats.add("MMMM dd yyyy");
        dateFormats.add("MMMM yyyy");
        dateFormats.add("MM yyyy");

        dateFormats.add("yy GG");

        for (String dateFormat: dateFormats) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            try {
                Date date = sdf.parse(dateString);
                HistoryDate hDate = new HistoryDate(date, dateFormat);

                if (isValid(hDate)) {
                    dateCache.put(dateString, hDate);
                    return hDate;
                }
            } catch (ParseException e) {
                // Ignore and try next date parser
                //System.out.println(e.toString());
            }
        }

        if (!parseExceptionCache.contains(dateString))
            parseExceptionCache.add(dateString);

        return null;
    }

    public String Format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    private boolean isValid(HistoryDate hDate) {
        // Validate date could have happened by checking the time... shouldn't allow future dates
        if (hDate.getDate().getTime() <= new Date().getTime()) {
            return true;
        }

        return false;
    }
}
