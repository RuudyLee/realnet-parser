/* created on Sept 12, 2017 by Rudy Lee */

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

    public String[] getAllValues() {
        String[] everything = { mDevelopmentName, mLocation, mBuilder, mOpeningDate, mUnitSizes, mPriceRange,
                mCurrentlyAvailable, mOriginalPSF, mSales, mUnitsSold, mNumberOfUnits, mConstructionStatus };
        return everything;
    }

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