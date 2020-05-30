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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cmpt276.assign1.depthoffieldcalculator.R;
import cmpt276.assign1.depthoffieldcalculator.model.DepthOfFieldCalculator;
import cmpt276.assign1.depthoffieldcalculator.model.Lens;
import cmpt276.assign1.depthoffieldcalculator.model.LensManager;

/**
 * Compute depth of field values for the selected Lens
 * Allows user to input circle of confusion, distance, and aperture
 * Able to edit and delete selected lens
 *
 * Code for toolbar back button taken from:
 * https://stackoverflow.com/questions/26651602/display-back-arrow-on-toolbar
 */
public class CalculateDepthOfFieldActivity extends AppCompatActivity {

    public static final int RESULT_CODE_EDIT_LENS = 42;
    public static final String EXTRA_LENS_MAKE = "lens make";
    public static final String EXTRA_LENS_FOCAL_LENGTH = "lens focal length";
    public static final String EXTRA_LENS_APERTURE = "lens aperture";
    public static final String EXTRA_LENS_ICON_ID = "lens icon ID";

    private static final String EXTRA_LENS_INDEX = "extra lens index";
    private static final String INVALID_APERTURE = "Invalid aperture";
    private LensManager manager;
    private Lens lens;
    private TextView lensText;

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
        getSupportActionBar().setTitle("Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        int lensIndex = intent.getIntExtra(EXTRA_LENS_INDEX, 0);

        manager = LensManager.getInstance();
        lens = manager.getLens(lensIndex);

        lensText = findViewById(R.id.textViewLens);
        lensText.setText(lens.toString());

        setupAutoCalculate();
        setupButtonEdit();
        setupButtonDelete();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
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
                    double userCOC = Double.parseDouble(userCOCData);
                    double userDistance = Double.parseDouble(userDistanceData);
                    double userAperture = Double.parseDouble(userApertureData);

                    // Multiply userDistance by 1000 to convert units from m to mm
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
        TextView hyperFocalDistance = findViewById(R.id.txtHyperFocalDistance);

        if(userAperture < lens.getMaxAperture()){
            nearFocalDistance.setText(INVALID_APERTURE);
            farFocalDistance.setText(INVALID_APERTURE);
            depthOfField.setText(INVALID_APERTURE);
            hyperFocalDistance.setText(INVALID_APERTURE);
        } else{
            DepthOfFieldCalculator doFCalculator = new DepthOfFieldCalculator(
                    lens, userAperture, userDistance, userCOC
            );

            // Divide distances by 1000 to convert units from mm to m
            nearFocalDistance.setText(doFCalculator.nearFocalPoint() / 1000 + "m");
            farFocalDistance.setText(doFCalculator.farFocalPoint() / 1000 + "m");
            hyperFocalDistance.setText(doFCalculator.hyperFocalDistance() / 1000 + "m");
            depthOfField.setText(doFCalculator.depthOfField() / 1000 + "m");
        }
    }

    private void setupButtonEdit(){
        Button btnEdit = findViewById(R.id.buttonEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LensDetailsActivity.makeLaunchIntent(
                        CalculateDepthOfFieldActivity.this, true);
                intent.putExtra(EXTRA_LENS_MAKE, lens.getMake());
                intent.putExtra(EXTRA_LENS_FOCAL_LENGTH, lens.getFocalLength());
                intent.putExtra(EXTRA_LENS_APERTURE, lens.getMaxAperture());
                intent.putExtra(EXTRA_LENS_ICON_ID, lens.getIconID());
                startActivityForResult(intent, RESULT_CODE_EDIT_LENS);
            }
        });
    }

    private void setupButtonDelete() {
        Button btnDelete = findViewById(R.id.buttonDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.remove(lens);
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_CANCELED){
            String message = "Canceled";
            Toast.makeText(CalculateDepthOfFieldActivity.this, message, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        switch (requestCode){
            case RESULT_CODE_EDIT_LENS:
                String userMake = data.getStringExtra(LensDetailsActivity.EXTRA_USER_MAKE);
                int userFocalLength = data.getIntExtra(LensDetailsActivity.EXTRA_USER_FOCAL_lENGTH, 0);
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

                lensText.setText(lens.toString());
                break;
            default:
                assert false;
        }
    }
}
