package unitconverter.edu.uc.unitconverter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Barker on 10/28/2015
 */
public class LengthSaver extends SQLiteOpenHelper {


    public static final String LENGTHS = "LENGTHS";
    public static final String CACHE_ID = "CACHE_ID";
    public static final String IN_INCHES = "IN_INCHES";
    public static final String CATEGORY = "CATEGORY";
    public static final String NAME = "NAME";

    int numberOfColumns = 0;

    public static final int NAME_INDEX = 1;
    public static final int CATEGORY_INDEX = 2;
    public static final int IN_KG_INDEX = 3;

    public LengthSaver(Context ctx) {
        super(ctx, "lengths4.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        numberOfColumns = 4;
        String createTimes = "CREATE TABLE " + LENGTHS + " ( " + CACHE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +  NAME + " TEXT, " + CATEGORY + " TEXT, " + IN_INCHES + " FLOAT " + ");";
        db.execSQL(createTimes);

    }

    /**
     * Inserts a Unit into the database
     * @param  Unit entry
     * @return  long cacheID
     *
     */
    public long insert(Unit unit){
        ContentValues cv = new ContentValues();
        cv.put(NAME, unit.getName());
        cv.put(IN_INCHES, unit.getConversionFactor());
        cv.put(CATEGORY, unit.getCategory());
        long cacheID = getWritableDatabase().insert(LENGTHS, IN_INCHES, cv);
        unit.setCacheID(cacheID);
        return cacheID;
    }

    /*
    Initializes the database
     */
    public void initialCreate(){



        String sql = "SELECT *  FROM  LENGTHS";
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        if(!(cursor.getCount() >0)) {
            Unit inches = new Unit("inches", "length", 1f);
            Unit feet = new Unit("feet", "length", 12f);
            Unit yards = new Unit("yards", "length", 36f);
            Unit miles = new Unit("miles", "length", 63360f);
            Unit centimeters = new Unit("centimeters", "length", .3937f);
            Unit meters = new Unit("meters", "length", 39.37f);
            Unit kilometers = new Unit("kilometers", "length", 3937);
            insert(inches);
            insert(feet);
            insert(yards);
            insert(miles);
            insert(centimeters);
            insert(meters);
            insert(kilometers);
        }
        cursor.close();

    }



    /**
     * Fetches all of the times from the database and returns them
     * as an arrayList of data type Individual times uses a while to loop through
     * each entry and then grabs from each column
     * @return  ArrayList<IndividualTime>
     *
     */
    public ArrayList<Unit> fetchAllUnits(){
        ArrayList<Unit> results = new ArrayList<Unit>();
        String sql = "SELECT *  FROM  LENGTHS";
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                String name = cursor.getString(NAME_INDEX);
                String category = cursor.getString(CATEGORY_INDEX);
                float convFactor = cursor.getFloat(IN_KG_INDEX);


                Unit newUnit= new Unit(name, category, convFactor);
                results.add(newUnit);
                cursor.moveToNext();

            }
        }
        cursor.close();
        return results;


    }

    /*
    public ArrayList<ArrayList<Unit>> getNumberOfCategories(){
        ArrayList<Unit> results = new ArrayList<Unit>();
        String sql = "SELECT *  FROM  UNITS";
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){

                String category = cursor.getString(CATEGORY_INDEX);





                cursor.moveToNext();

            }
        }
        cursor.close();
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}