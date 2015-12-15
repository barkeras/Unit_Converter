package unitconverter.edu.uc.unitconverter;

/**
 * Created by Andrew Barker on 10/28/2015
 */
public class Unit {
    private float conversionFactor; //conversion factor
    private String category = "No Category";
    private String name = "No Name";

    private long cacheID = 0;


    public Unit(String nameA, String categoryA, float conversionFactorA ){
        name = nameA;
        category = categoryA;
        conversionFactor = conversionFactorA;

    }

    public double getConversionFactor(){return conversionFactor;}
    public String getCategory(){return category;}
    public String getName(){return name;}
    public long getCacheID(){return cacheID;}
    public void setCacheID(long cacheID2){cacheID =cacheID2;}
}

