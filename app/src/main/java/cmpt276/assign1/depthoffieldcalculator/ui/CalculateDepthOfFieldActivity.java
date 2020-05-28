package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
 */
public class CalculateDepthOfFieldActivity extends AppCompatActivity {

    public static final int RESULT_CODE_EDIT_LENS = 42;
    public static final String EXTRA_LENS_MAKE = "lens make";
    public static final String EXTRA_LENS_FOCAL_LENGTH = "lens focal length";
    public static final String EXTRA_LENS_APERTURE = "lens aperture";

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

        Intent intent = getIntent();
        int lensIndex = intent.getIntExtra(EXTRA_LENS_INDEX, 0);

        manager = LensManager.getInstance();
        lens = manager.getLens(lensIndex);

        lensText = findViewById(R.id.textViewLens);
        lensText.setText(lens.toString());

        setupButtonCalculate();
        setupButtonEdit();
        setupButtonDelete();
    }

    private void setupButtonCalculate() {
        Button btnCalculator = findViewById(R.id.buttonCalculate);
        btnCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userCOCEntry = findViewById(R.id.editTextCOC);
                String userCOCData = userCOCEntry.getText().toString();
                double userCOC = Double.parseDouble(userCOCData);

                EditText userDistanceEntry = findViewById(R.id.editTextDistance);
                String userDistanceData = userDistanceEntry.getText().toString();
                double userDistance = Double.parseDouble(userDistanceData);

                EditText userApertureEntry = findViewById(R.id.editTextAperture);
                String userApertureData = userApertureEntry.getText().toString();
                double userAperture = Double.parseDouble(userApertureData);

                // Multiply userDistance by 1000 to convert units from m to mm
                calculateDepthOfField(userCOC, userDistance * 1000, userAperture);
            }
        });
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

                if(!userMake.equals(lens.getMake())){
                    lens.setMake(userMake);
                }
                if(lens.getFocalLength() != userFocalLength){
                    lens.setFocalLength(userFocalLength);
                }
                if(lens.getMaxAperture() != userAperture){
                    lens.setMaxAperture(userAperture);
                }

                lensText.setText(lens.toString());
                break;
            default:
                assert false;
        }
    }
}
