package plutarch.nlp.model;

import plutarch.nlp.helper.DateHelper;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by joshs on 6/28/2017.
 */
@Embeddable
public class HistoryDate {
    private Date date;
    private String format;
    @Enumerated(EnumType.STRING)
    private DateType dateType;
    private String formattedDate;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;

        String unscrubbedDate = DateHelper.getInstance().Format(this.getDate(), this.format);

        // Removes leading 0's unless the value is actually 0 in any number in the string
        String scrubbedDate = unscrubbedDate.replaceAll("\\s0+(?!$)", " ");
        scrubbedDate = scrubbedDate.replaceAll("^0+(?!$)", "");
        this.formattedDate = scrubbedDate;
    }

    public DateType getDateType() {
        return dateType;
    }

    public void setDateType(DateType dateType) {
        this.dateType = dateType;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public HistoryDate() {}

    public HistoryDate(Date date, String format) {
        setDate(date);

        if (getDate() == null) {
            setDateType(DateType.Empty);
        } else {
            setFormat(format);

            if (format.contains("d")) {
                setDateType(DateType.Full);
            } else if (format.contains("m") || format.contains("M")) {
                setDateType(DateType.Month);
            } else {
                setDateType(DateType.Year);
            }
        }
    }

    public boolean equals(Object o) {
        if (o instanceof HistoryDate) {
            HistoryDate toCompare = (HistoryDate)o;
            return this.getDate().toString().equals(toCompare.getDate().toString());
        }
        return false;
    }

    public String DateString() {
        if (getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            String dateString = sdf.format(getDate());
            return dateString;
        }
        return null;
    }
}
