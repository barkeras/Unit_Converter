package unitconverter.edu.uc.unitconverter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.NoRouteToHostException;
import java.util.ArrayList;


public class MainActivity extends Activity implements PopupMenu.OnMenuItemClickListener,AdapterView.OnItemSelectedListener {



    private MassSaver masses; //mass database saver
    private LengthSaver lengths2; //length database saver
    private ArrayList<Unit> unitLengthArray; //length units
    private ArrayList<Unit> unitMassArray; //mass units
    ArrayAdapter<String> lengthAdapter; //adapter for spinners for lengths
    ArrayAdapter<String> massAdapter; //adapter for spinners for masses
    private ArrayList<String> lengthNames = new ArrayList<String>(); //names of lengths
    private ArrayList<String> massNames = new ArrayList<String>(); //names of masses
    private String currentUnitType = "Length"; //the current data type

    //Initializing all the textviews of the numbers and functions

    private TextView unitsTV;
    private TextView numberDelete;
    private Spinner toSpinner;
    private Spinner fromSpinner;
    private EditText fromInputText;
    private EditText toOutputText;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;
    private TextView tv9;
    private TextView tv0;
    private TextView tvDec;
    private TextView tvPM;
    private TextView tvSwap;
    private TextView tvFavorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //setting all the textview number and function buttons

        unitsTV = (TextView)findViewById(R.id.unitTypesTextView);
        numberDelete = (TextView)findViewById(R.id.numberDelete);
        toSpinner = (Spinner)findViewById(R.id.toSpinner);
        fromSpinner = (Spinner)findViewById(R.id.fromSpinner);
        tv1 = (TextView)findViewById(R.id.numberOne);
        tv2 = (TextView)findViewById(R.id.numberTwo);
        tv3 = (TextView)findViewById(R.id.numberThree);
        tv4 = (TextView)findViewById(R.id.numberFour);
        tv5 = (TextView)findViewById(R.id.numberFive);
        tv6 = (TextView)findViewById(R.id.numberSix);
        tv7 = (TextView)findViewById(R.id.numberSeven);
        tv8 = (TextView)findViewById(R.id.numberEight);
        tv9 = (TextView)findViewById(R.id.numberNine);
        tv0 = (TextView)findViewById(R.id.numberZero);
        tvDec = (TextView)findViewById(R.id.numberDecimal);
        tvPM = (TextView)findViewById(R.id.numberPlusMinus);
        tvSwap = (TextView)findViewById(R.id.numberSwap);
        tvFavorite = (TextView)findViewById(R.id.numberFavorite);
        fromInputText = (EditText)findViewById(R.id.fromInputText);
        toOutputText = (EditText)findViewById(R.id.toOutputText);
        toSpinner.setOnItemSelectedListener(this);
        fromSpinner.setOnItemSelectedListener(this);


        //creating new mass and length saver for databases
        masses = new MassSaver(getApplicationContext());
        lengths2 = new LengthSaver(getApplicationContext());

        //creating initial values if needed
        lengths2.initialCreate();
        masses.initialCreate();

        //pulling items from the database
        unitMassArray = masses.fetchAllUnits();
        unitLengthArray = lengths2.fetchAllUnits();

        //making the string arrays for mass and length for easy access
        populateStringArrays();

        //creating length and mass adapters for spinners
        lengthAdapter= new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item, lengthNames);
        massAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item, massNames);
        toSpinner.setAdapter(lengthAdapter);
        fromSpinner.setAdapter(lengthAdapter);



        final String[] unitTypes = {"Length", "Mass"};
        final TextView fromDisplay = (TextView)findViewById(R.id.fromInputText);
        final TextView toDisplay = (TextView)findViewById(R.id.toOutputText);


        //below are the onclick listeners for all of the numpad buttons and function buttons
        unitsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this,R.style.CustomDialogTheme));
                builder.setTitle("Select Unit Conversion Type");
                builder.setItems(unitTypes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getApplicationContext(),unitTypes[which],Toast.LENGTH_SHORT).show();
                        currentUnitType = unitTypes[which];
                        if(currentUnitType.equalsIgnoreCase("Length")){
                            toSpinner.setAdapter(lengthAdapter);
                            fromSpinner.setAdapter(lengthAdapter);
                        }
                        else{
                            toSpinner.setAdapter(massAdapter);
                            fromSpinner.setAdapter(massAdapter);
                        }
                        convert();

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(true);
            }

        });

        numberDelete.setLongClickable(true);

        numberDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String shortDisplay;
                String fromDisplayText = fromDisplay.getText().toString();

                if (fromDisplayText.length() == 1) {
                    fromDisplay.setText("0");
                } else {
                    shortDisplay = fromDisplayText.substring(0, fromDisplay.length() - 1);
                    //Toast.makeText(MainActivity.this, shortDisplay, Toast.LENGTH_LONG).show();
                    fromDisplay.setText(shortDisplay);
                }
                convert();


            }
        });


        numberDelete.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                fromDisplay.setText("0");
                toDisplay.setText("0");
                //Toast.makeText(MainActivity.this, fromDisplay.getText(), Toast.LENGTH_LONG).show();

                return true;
            }
        });


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromDisplay.getText().toString().substring(0, 1).equals("0") && !fromDisplay.getText().toString().contains(".") && !fromDisplay.getText().toString().contains(".")) {
                    fromDisplay.setText("1");
                } else {
                    fromDisplay.setText(fromDisplay.getText() + "1");
                }
                convert();

            }
        });

        tv2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fromDisplay.getText().toString().substring(0, 1).equals("0") && !fromDisplay.getText().toString().contains(".")){
                    fromDisplay.setText("2");
                }
                else{
                    fromDisplay.setText(fromDisplay.getText() + "2");
                }
                convert();
            }
        });

        tv3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fromDisplay.getText().toString().substring(0, 1).equals("0") && !fromDisplay.getText().toString().contains(".")){
                    fromDisplay.setText("3");
                }
                else{
                    fromDisplay.setText(fromDisplay.getText() + "3");
                }
                convert();
            }
        });

        tv4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fromDisplay.getText().toString().substring(0, 1).equals("0") && !fromDisplay.getText().toString().contains(".")){
                    fromDisplay.setText("4");
                }
                else{
                    fromDisplay.setText(fromDisplay.getText() + "4");
                }
                convert();
            }
        });

        tv5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fromDisplay.getText().toString().substring(0, 1).equals("0") && !fromDisplay.getText().toString().contains(".")){
                    fromDisplay.setText("5");
                }
                else{
                    fromDisplay.setText(fromDisplay.getText() + "5");
                }
                convert();
            }
        });

        tv6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fromDisplay.getText().toString().substring(0, 1).equals("0") && !fromDisplay.getText().toString().contains(".")){
                    fromDisplay.setText("6");
                }
                else{
                    fromDisplay.setText(fromDisplay.getText() + "6");
                }
                convert();
            }
        });

        tv7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fromDisplay.getText().toString().substring(0, 1).equals("0") && !fromDisplay.getText().toString().contains(".")){
                    fromDisplay.setText("7");
                }
                else{
                    fromDisplay.setText(fromDisplay.getText() + "7");
                }
                convert();
            }
        });

        tv8.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fromDisplay.getText().toString().substring(0, 1).equals("0") && !fromDisplay.getText().toString().contains(".")){
                    fromDisplay.setText("8");
                }
                else{
                    fromDisplay.setText(fromDisplay.getText() + "8");
                }
                convert();
            }
        });

        tv9.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fromDisplay.getText().toString().substring(0, 1).equals("0") && !fromDisplay.getText().toString().contains(".")){
                    fromDisplay.setText("9");
                }
                else{
                    fromDisplay.setText(fromDisplay.getText() + "9");
                }
                convert();
            }
        });

        tv0.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fromDisplay.getText().toString().substring(0, 1).equals("0") && !fromDisplay.getText().toString().contains(".")){
                    fromDisplay.setText("0");
                }
                else{
                    fromDisplay.setText(fromDisplay.getText() + "0");
                }
                convert();
            }
        });

        tvDec.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(fromDisplay.getText().toString().contains(".")){
                    //this block does nothing so that the user cannot have multiple decimals in a number
                }
                else{
                    fromDisplay.setText(fromDisplay.getText() + ".");
                }
                convert();


            }
        });

        tvPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstChar = fromDisplay.getText().toString();


                if (fromDisplay.getText().toString().substring(0,1).equals("-")) {
                    fromDisplay.setText(firstChar.substring(1,firstChar.length()));
                }
                else{
                    fromDisplay.setText("-" + fromDisplay.getText());

                }
                convert();


            }
        });

        //onclick listener for swapping the from and to categories
        tvSwap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Swap to and From", Toast.LENGTH_LONG).show();
                int unitTo = toSpinner.getSelectedItemPosition();
                int unitFrom = fromSpinner.getSelectedItemPosition();
                toSpinner.setSelection(unitFrom);
                fromSpinner.setSelection(unitTo);
            }
        });

        tvFavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Favorite Toggled", Toast.LENGTH_LONG).show();
            }
        });


//        findViewById(R.id.unitTypesTextView).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
//                popupMenu.setOnMenuItemClickListener(MainActivity.this);
//                popupMenu.inflate(R.menu.units_menu);
//                popupMenu.show();
//            }
//        });

        /**
         * This sets the unique text for the symbols as the font for the star and swap and negate icons
         */


        Typeface fontFamily = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        TextView swap = (TextView) findViewById(R.id.numberSwap);
        TextView fav = (TextView) findViewById(R.id.numberFavorite);
        swap.setTypeface(fontFamily);
        swap.setText("\uf0ec"); // set swap icon
        swap.setRotation(90);
        fav.setTypeface(fontFamily);
        fav.setText("\uf006");





    }

    /**
     * converts the units by grabbing the units from the to and from spinners
     * and then pulling from the unit arrays. Does so no matter what category of
     * units is selected
     *
     *
     *
     */
    public void convert() {
        String unitTo = toSpinner.getSelectedItem().toString();
        Unit theUnitTo = unitLengthArray.get(1);
        Unit theUnitFrom = unitLengthArray.get(1);
        String unitFrom = fromSpinner.getSelectedItem().toString();
        if(currentUnitType.equalsIgnoreCase("Length")){
            for(int i=0; i<unitLengthArray.size(); i++){
                if(unitLengthArray.get(i).getName().equalsIgnoreCase(unitTo)){
                    theUnitTo = unitLengthArray.get(i);

                }
                if(unitLengthArray.get(i).getName().equalsIgnoreCase(unitFrom)){
                    theUnitFrom = unitLengthArray.get(i);

                }


            }
            double number = Double.parseDouble(fromInputText.getText().toString());
            number = number*theUnitFrom.getConversionFactor();
            number = number/theUnitTo.getConversionFactor();
            BigDecimal bd = new BigDecimal(number);
            bd = bd.round(new MathContext(fromInputText.getText().toString().length()));
            toOutputText.setText("" + bd);

        }
        else{
            for(int i=0; i<unitMassArray.size(); i++){
                if(unitMassArray.get(i).getName().equalsIgnoreCase(unitTo)){
                    theUnitTo = unitMassArray.get(i);

                }
                if(unitMassArray.get(i).getName().equalsIgnoreCase(unitFrom)){
                    theUnitFrom = unitMassArray.get(i);

                }


            }
            double number = Double.parseDouble(fromInputText.getText().toString());
            number = number*theUnitFrom.getConversionFactor();
            number = number/theUnitTo.getConversionFactor();
            BigDecimal bd = new BigDecimal(number);
            bd = bd.round(new MathContext(fromInputText.getText().toString().length()));
            toOutputText.setText("" + bd);

        }
    }

    //converts everytime a new item is selected
    public void onItemSelected(AdapterView<?> parent,
                               View view, int pos, long id) {
        convert();

    }


    public void onNothingSelected(AdapterView parent) {
        // Do nothing.
    }

    /*
     * inserts a new unit into one of the databases
     * database chosen is based on current unit type could also be based on an inputted string
     * inserts into database using the insert method that takes a unit
     * unit has name, category and conversion factor
     * conversion factor for lengths are in inches
     * conversion factor for masses are in kilograms
     *
     * Once an item is inserted I don't know how to remove it and if it is incorrectly inputted
     * it may break the database and therefore the rest of the code. I don't know how to remove
     * items so if it breaks I just change the named of the database created so it creates a new one
     */
    public void insertNewUnit(){
        String name = "";
        String category = "";
        Float conversionFactor = 1f; //conversion factor for length is inches, kg for mass
        Unit newUnit = new Unit(name,category,conversionFactor);
        if(currentUnitType.equalsIgnoreCase("Length")){
            lengths2.insert(newUnit);

        }
        else {
            masses.insert(newUnit);
        }

        //pulling items from the database
        unitMassArray = masses.fetchAllUnits();
        unitLengthArray = lengths2.fetchAllUnits();

        //making the string arrays for mass and length for easy access
        populateStringArrays();

    }

    /*
    * fills the length and mass string arrays with the units strings
    * from the databases.
     */
    public void populateStringArrays(){
        for(int i =0; i < 7; i++) {

            lengthNames.add(unitLengthArray.get(i).getName());

        }
        for(int i =0; i < unitMassArray.size(); i++) {
            massNames.add(unitMassArray.get(i).getName());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }




}


