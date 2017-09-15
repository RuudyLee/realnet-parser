/**
 * File: c:\Users\Rudy\Desktop\html-parser\src\main\java\SummaryPayload.java
 * Created Date: Wednesday, September 13th 2017, 12:42:29 am
 * Author: RuudyLee
 * -----
 * Last Modified: Fri Sep 15 2017
 * Modified By: RuudyLee
 * -----
 */

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SummaryPayload {
    public String mDevelopmentName;
    public String mLocation;
    public String mBuilder;
    public String mOpeningDate;
    public String mUnitSizes;
    public String mPriceRange;
    public String mCurrentlyAvailable;
    public String mOriginalPSF;
    public String mSales;
    public String mUnitsSold;
    public String mNumberOfUnits;
    public String mConstructionStatus;

    public SummaryPayload() {
        mDevelopmentName = "";
        mLocation = "";
        mBuilder = "";
        mOpeningDate = "";
        mUnitSizes = "";
        mPriceRange = "";
        mCurrentlyAvailable = "";
        mOriginalPSF = "";
        mSales = "";
        mUnitsSold = "";
        mNumberOfUnits = "";
        mConstructionStatus = "";
    }

    public SummaryPayload(String filename) throws IOException {
        Document doc = Jsoup.parse(new File(filename), "utf-8"); // the html file
        Elements tables = doc.select("table[class=recordDetailFields"); // the tables

        // Parse the html for the values we need
        for (Element table : tables) {
            Elements rows = table.select("tr");
            for (Element e : rows) {
                Elements keyVals = e.select("td");
                String key = keyVals.get(0).text();
                String val = "";
                // if keyVals is less than 1, that means there is no value to go with the key
                if (keyVals.size() > 1) {
                    val = keyVals.get(1).text();
                }
                this.readValue(key, val);
            }
        }
    }

    public SummaryPayload(File file) throws IOException {
        Document doc = Jsoup.parse(file, "utf-8"); // the html file
        Elements tables = doc.select("table[class=recordDetailFields"); // the tables

        // Parse the html for the values we need
        for (Element table : tables) {
            Elements rows = table.select("tr");
            for (Element e : rows) {
                Elements keyVals = e.select("td");
                String key = keyVals.get(0).text();
                String val = "";
                // if keyVals is less than 1, that means there is no value to go with the key
                if (keyVals.size() > 1) {
                    val = keyVals.get(1).text();
                }
                this.readValue(key, val);
            }
        }
    }

    /**
     * Returns all the values in the payload as a String array
     * @return String array of all encapsulated values
     */
    public String[] getAllValues() {
        String[] everything = { mDevelopmentName, mLocation, mBuilder, mOpeningDate, mUnitSizes, mPriceRange,
                mCurrentlyAvailable, mOriginalPSF, mSales, mUnitsSold, mNumberOfUnits, mConstructionStatus };
        return everything;
    }

    /**
     * Reads a value based on its key
     * @param key the key
     * @param val the value
     */
    public void readValue(String key, String val) {
        switch (key) {
        case "Development Name:":
            mDevelopmentName = val;
            break;
        case "Location:":
            mLocation = val;
            break;
        case "Builder:":
            mBuilder = val;
            break;
        case "Opening Date:":
            mOpeningDate = val;
            break;
        case "Unit Sizes (sf):":
            mUnitSizes = val;
            break;
        case "Unit Prices:":
            mPriceRange = val;
            break;
        case "Currently Available $/sf:":
            mCurrentlyAvailable = val;
            break;
        case "Original $/sf:":
            mOriginalPSF = val;
            break;
        case "Construction Status:":
            mConstructionStatus = val;
            break;
        }
    }

    public void debugPrint() {
        System.out.println(mDevelopmentName);
        System.out.println(mLocation);
        System.out.println(mBuilder);
        System.out.println(mOpeningDate);
        System.out.println(mUnitSizes);
        System.out.println(mPriceRange);
        System.out.println(mCurrentlyAvailable);
        System.out.println(mOriginalPSF);
        System.out.println(mSales);
        System.out.println(mUnitsSold);
        System.out.println(mNumberOfUnits);
        System.out.println(mConstructionStatus);
    }
}