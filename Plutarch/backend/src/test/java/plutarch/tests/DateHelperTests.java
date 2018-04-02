package plutarch.tests;

import org.junit.Assert;
import org.junit.Test;
import plutarch.nlp.helper.DateHelper;
import plutarch.nlp.model.HistoryDate;

/**
 * Created by joshs on 6/25/2017.
 */
public class DateHelperTests {

    @Test
    public void evaluateDateFormat1() {
        //yyyy-MM-dd GG
        HistoryDate hDate = DateHelper.getInstance().Parse("1323-08-01 BC");
        Assert.assertEquals(hDate.getFormat(), "yyyy-MM-dd GG");
    }

    @Test
    public void evaluateDateFormat2() {
        //yyyy/MM/dd GG
        HistoryDate hDate = DateHelper.getInstance().Parse("1323/08/01 BC");
        Assert.assertEquals(hDate.getFormat(), "yyyy/MM/dd GG");
    }

    @Test
    public void evaluateDateFormat3() {
        //yyyy-MM-dd
        HistoryDate hDate = DateHelper.getInstance().Parse("1354-05-07");
        Assert.assertEquals(hDate.getFormat(), "yyyy-MM-dd");
    }

    @Test
    public void evaluateDateFormat4() {
        //yyyy/MM/dd
        HistoryDate hDate = DateHelper.getInstance().Parse("1354/05/07");
        Assert.assertEquals(hDate.getFormat(), "yyyy/MM/dd");
    }

    @Test
    public void evaluateDateFormat5() {
        //MM-dd-yyyy GG
        HistoryDate hDate = DateHelper.getInstance().Parse("10-18-1211 BC");
        Assert.assertEquals(hDate.getFormat(), "MM-dd-yyyy GG");
    }

    @Test
    public void evaluateDateFormat6() {
        //MM/dd/yyyy GG
        HistoryDate hDate = DateHelper.getInstance().Parse("10/18/1211 BC");
        Assert.assertEquals(hDate.getFormat(), "MM/dd/yyyy GG");
    }

    @Test
    public void evaluateDateFormat7() {
        //dd MMMMM yyyy GG
        HistoryDate hDate = DateHelper.getInstance().Parse("25 March 1333 BC");
        Assert.assertEquals(hDate.getFormat(), "dd MMMM yyyy GG");
    }

    @Test
    public void evaluateDateFormat8() {
        //MM-dd-yyyy
        HistoryDate hDate = DateHelper.getInstance().Parse("11-16-1852");
        Assert.assertEquals(hDate.getFormat(), "MM-dd-yyyy");
    }

    @Test
    public void evaluateDateFormat9() {
        //MM/dd/yyyy
        HistoryDate hDate = DateHelper.getInstance().Parse("11/16/1852");
        Assert.assertEquals(hDate.getFormat(), "MM/dd/yyyy");
    }

    @Test
    public void evaluateDateFormat10() {
        //dd MMMMM yyyy
        HistoryDate hDate = DateHelper.getInstance().Parse("12 February 1985");
        Assert.assertEquals(hDate.getFormat(), "dd MMMM yyyy");
    }

    @Test
    public void evaluateDateFormat11() {
        //MMMMM yyyy GG
        HistoryDate hDate = DateHelper.getInstance().Parse("January 1110 BC");
        Assert.assertEquals(hDate.getFormat(), "MMMM yyyy GG");
    }

    @Test
    public void evaluateDateFormat12() {
        //MM yyyy GG
        HistoryDate hDate = DateHelper.getInstance().Parse("03 1598 BC");
        Assert.assertEquals(hDate.getFormat(), "MM yyyy GG");
    }

    @Test
    public void evaluateDateFormat13() {
        //MMMMM yyyy
        HistoryDate hDate = DateHelper.getInstance().Parse("June 1267");
        Assert.assertEquals(hDate.getFormat(), "MMMM yyyy");
    }

    @Test
    public void evaluateDateFormat14() {
        //MM yyyy
        HistoryDate hDate = DateHelper.getInstance().Parse("05 1075");
        Assert.assertEquals(hDate.getFormat(), "MM yyyy");
    }

    @Test
    public void evaluateDateFormat15() {
        //yyyy MMMMM dd GG
        HistoryDate hDate = DateHelper.getInstance().Parse("2575 June 15 BC");
        Assert.assertEquals(hDate.getFormat(), "yyyy MMMM dd GG");
    }

    @Test
    public void evaluateDateFormat16() {
        //yyyy MMMMM dd
        HistoryDate hDate = DateHelper.getInstance().Parse("1254 June 5");
        Assert.assertEquals(hDate.getFormat(), "yyyy MMMM dd");
    }

    @Test
    public void evaluateDateFormat17() {
        //yyyy GG
        HistoryDate hDate = DateHelper.getInstance().Parse("3500 BC");
        Assert.assertEquals(hDate.getFormat(), "yyyy GG");
    }

    @Test
    public void evaluateDateFormat18() {
        //yyyy
        HistoryDate hDate = DateHelper.getInstance().Parse("1015 ");
        Assert.assertEquals(hDate.getFormat(), "yyyy");
    }

    @Test
    public void evaluateDateFormat19() {
        //MMMMM dd yyyy
        HistoryDate hDate = DateHelper.getInstance().Parse("August 18 1227");
        Assert.assertEquals(hDate.getFormat(), "MMMM dd yyyy");
    }

    @Test
    public void evaluateDateFormat20() {
        //MMMMM yyyy
        HistoryDate hDate = DateHelper.getInstance().Parse("August 253");
        Assert.assertEquals(hDate.getFormat(), "MMMM yyyy");
    }

    @Test
    public void evaluateDateFormat21() {
        //yyyy GG
        HistoryDate hDate = DateHelper.getInstance().Parse("353 BC");
        Assert.assertEquals(hDate.getFormat(), "yyyy GG");

        Assert.assertEquals("353 BC", hDate.getFormattedDate());
    }

    @Test
    public void evaluateDateFormat22() {
        //dd MMMMM yyyy GG
        HistoryDate hDate = DateHelper.getInstance().Parse("21 July 356 BC");
        Assert.assertEquals(hDate.getFormat(), "dd MMMM yyyy GG");
    }

    @Test
    public void evaluateDateFormat23() {
        //dd MMMMM yyyy
        HistoryDate hDate = DateHelper.getInstance().Parse("21 July 356");
        Assert.assertEquals(hDate.getFormat(), "dd MMMM yyyy");
    }

    @Test
    public void evaluateDateFormat24() {
        //yyyy-MM-dd GG
        HistoryDate hDate = DateHelper.getInstance().Parse("August 18, 1227");
        Assert.assertEquals(hDate.getFormat(), "MMMM dd yyyy");

        Assert.assertEquals("August 18 1227", hDate.getFormattedDate());
    }

    @Test
    public void evaluateDateFormat25() {
        //dd MMMMM yyyy
        HistoryDate hDate = DateHelper.getInstance().Parse("21 July 356");
        Assert.assertEquals(hDate.getFormat(), "dd MMMM yyyy");

        Assert.assertEquals("21 July 356", hDate.getFormattedDate());
    }
}
