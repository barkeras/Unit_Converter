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
public class MassSaver extends SQLiteOpenHelper {


    public static final String MASSES = "MASSES";
    public static final String CACHE_ID = "CACHE_ID";
    public static final String IN_KG = "IN_KG";
    public static final String CATEGORY = "CATEGORY";
    public static final String NAME = "NAME";

    int numberOfColumns = 0;

    public static final int NAME_INDEX = 1;
    public static final int CATEGORY_INDEX = 2;
    public static final int IN_KG_INDEX = 3;

    public MassSaver(Context ctx) {
        super(ctx, "masses4.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        numberOfColumns = 4;
        String createTimes = "CREATE TABLE " + MASSES + " ( " + CACHE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +  NAME + " TEXT, " + CATEGORY + " TEXT, " + IN_KG + " FLOAT " + ");";
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
        cv.put(IN_KG, unit.getConversionFactor());
        cv.put(CATEGORY, unit.getCategory());
        long cacheID = getWritableDatabase().insert(MASSES, IN_KG, cv);
        unit.setCacheID(cacheID);
        return cacheID;
    }

    /*
    Initializes the database
     */
    public void initialCreate(){

        String sql = "SELECT *  FROM  MASSES";

        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        if(!(cursor.getCount() >0)) {
            Unit kg = new Unit("kg", "mass", 1);
            Unit mg = new Unit("mg", "mass", .000001f);
            Unit g = new Unit("g", "mass", .001f);
            Unit lbs = new Unit("lbs", "mass", .453f);
            Unit ounce = new Unit("ounce", "mass", .028f);
            insert(kg);
            insert(mg);
            insert(g);
            insert(lbs);
            insert(ounce);
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
        String sql = "SELECT *  FROM  MASSES";
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
