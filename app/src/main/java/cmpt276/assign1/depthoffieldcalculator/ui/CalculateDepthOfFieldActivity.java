package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

import cmpt276.assign1.depthoffieldcalculator.R;
import cmpt276.assign1.depthoffieldcalculator.model.DepthOfFieldCalculator;
import cmpt276.assign1.depthoffieldcalculator.model.Lens;
import cmpt276.assign1.depthoffieldcalculator.model.LensManager;

/**
 * Compute depth of field values for the selected Lens
 * Allows user to input circle of confusion, distance, and aperture
 * Able to edit and delete selected lens
 */
public class CalculateDepthOfFieldActivity extends AppCompatActivity {

    public static final int RESULT_CODE_EDIT_LENS = 42;
    public static final String EXTRA_LENS_MAKE = "lens make";
    public static final String EXTRA_LENS_FOCAL_LENGTH = "lens focal length";
    public static final String EXTRA_LENS_APERTURE = "lens aperture";
    public static final String EXTRA_LENS_ICON_ID = "lens icon ID";

    private static final String EXTRA_LENS_INDEX = "lens index";
    private static final String INVALID_APERTURE = "Invalid aperture";
    private static final String INVALID_VALUES = "Enter values greater than 0";
    private LensManager manager;
    private Lens lens;
    private TextView textLens;

    public static Intent makeLaunchIntent(Context context, int lensIdx){
        Intent intent = new Intent(context, CalculateDepthOfFieldActivity.class);
        intent.putExtra(EXTRA_LENS_INDEX, lensIdx);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_depth_of_field);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        int lensIndex = intent.getIntExtra(EXTRA_LENS_INDEX, 0);

        manager = LensManager.getInstance();
        lens = manager.get(lensIndex);

        textLens = findViewById(R.id.textViewLens);
        textLens.setText(lens.toString());

        setupAutoCalculate();
    }

    private void setupAutoCalculate(){
        EditText userCOCEntry = findViewById(R.id.editTextCOC);
        EditText userDistanceEntry = findViewById(R.id.editTextDistance);
        EditText userApertureEntry = findViewById(R.id.editTextAperture);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String userCOCData = userCOCEntry.getText().toString();
                String userDistanceData = userDistanceEntry.getText().toString();
                String userApertureData = userApertureEntry.getText().toString();

                if(!userCOCData.isEmpty()
                        && !userDistanceData.isEmpty()
                        && !userApertureData.isEmpty())
                {
                    double userCOC = Double.parseDouble(userCOCData);              // [mm]
                    double userDistance = Double.parseDouble(userDistanceData);    // [m]
                    double userAperture = Double.parseDouble(userApertureData);

                    // Multiply userDistance by 1000 to convert units from [m] to [mm]
                    calculateDepthOfField(userCOC, userDistance * 1000, userAperture);
                }
            }
        };

        userCOCEntry.addTextChangedListener(watcher);
        userDistanceEntry.addTextChangedListener(watcher);
        userApertureEntry.addTextChangedListener(watcher);
    }

    private void calculateDepthOfField(double userCOC, double userDistance, double userAperture){
        TextView nearFocalDistance = findViewById(R.id.txtNearFocalDistance);
        TextView farFocalDistance = findViewById(R.id.txtFarFocalDistance);
        TextView depthOfField = findViewById(R.id.txtDepthOfField);
        TextView hyperfocalDistance = findViewById(R.id.txtHyperfocalDistance);

        if(userAperture < lens.getMaxAperture()){
            nearFocalDistance.setText(INVALID_APERTURE);
            farFocalDistance.setText(INVALID_APERTURE);
            depthOfField.setText(INVALID_APERTURE);
            hyperfocalDistance.setText(INVALID_APERTURE);
        } else if(userCOC <= 0 || userDistance <= 0){
            nearFocalDistance.setText(INVALID_VALUES);
            farFocalDistance.setText(INVALID_VALUES);
            depthOfField.setText(INVALID_VALUES);
            hyperfocalDistance.setText(INVALID_VALUES);
        } else{
            // depthOfFieldCalculator (lens, aperture, distance [mm], circle of confusion [mm])
            DepthOfFieldCalculator doFCalculator = new DepthOfFieldCalculator(
                    lens, userAperture, userDistance, userCOC);

            // Divide distances by 1000 to convert units from [mm] to [m]
            nearFocalDistance.setText(formatM(doFCalculator.nearFocalPoint() / 1000));
            farFocalDistance.setText(formatM(doFCalculator.farFocalPoint() / 1000));
            hyperfocalDistance.setText(formatM(doFCalculator.hyperfocalDistance() / 1000));
            depthOfField.setText(formatM(doFCalculator.depthOfField() / 1000));
        }
    }

    private String formatM(double distanceInM) {
        if(distanceInM == Double.POSITIVE_INFINITY){
            // Concatenate "" to convert distanceInM to a string
            return distanceInM + "";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(distanceInM) + "m";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_CANCELED){
            return;
        }

        switch (requestCode){
            case RESULT_CODE_EDIT_LENS:
                String userMake = data.getStringExtra(LensDetailsActivity.EXTRA_USER_MAKE);
                int userFocalLength = data.getIntExtra(LensDetailsActivity.EXTRA_USER_FOCAL_LENGTH, 0);    // [mm]
                double userAperture = data.getDoubleExtra(LensDetailsActivity.EXTRA_USER_APERTURE, 0);
                int userIconID = data.getIntExtra(LensDetailsActivity.EXTRA_USER_ICON_ID, 0);

                if(!userMake.equals(lens.getMake())){
                    lens.setMake(userMake);
                }
                if(lens.getFocalLength() != userFocalLength){
                    lens.setFocalLength(userFocalLength);
                }
                if(lens.getMaxAperture() != userAperture){
                    lens.setMaxAperture(userAperture);
                }
                if(lens.getIconID() != userIconID){
                    lens.setIconID(userIconID);
                }

                textLens.setText(lens.toString());
                break;
            default:
                assert false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calculate_depth_of_field, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()){
            case android.R.id.home:
                intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.menu_itemEdit:
                intent = LensDetailsActivity.makeLaunchIntent(
                        CalculateDepthOfFieldActivity.this, true);
                intent.putExtra(EXTRA_LENS_MAKE, lens.getMake());
                intent.putExtra(EXTRA_LENS_FOCAL_LENGTH, lens.getFocalLength());
                intent.putExtra(EXTRA_LENS_APERTURE, lens.getMaxAperture());
                intent.putExtra(EXTRA_LENS_ICON_ID, lens.getIconID());
                startActivityForResult(intent, RESULT_CODE_EDIT_LENS);
                break;
            case R.id.menu_itemDelete:
                manager.remove(lens);
                intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            default:
                assert false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}
