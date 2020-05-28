package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cmpt276.assign1.depthoffieldcalculator.R;
import cmpt276.assign1.depthoffieldcalculator.model.DepthOfFieldCalculator;
import cmpt276.assign1.depthoffieldcalculator.model.Lens;
import cmpt276.assign1.depthoffieldcalculator.model.LensManager;

public class CalculateDepthOfFieldActivity extends AppCompatActivity {

    private static final String EXTRA_LENS_INDEX = "extra lens index";
    private LensManager manager;
    private Lens lens;

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

        TextView lensText = (TextView) findViewById(R.id.textViewLens);
        lensText.setText(lens.toString());

        setupButtonCalculate();
    }

    private void setupButtonCalculate() {
        Button btnCalculator = (Button) findViewById(R.id.buttonCalculate);
        btnCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userCOCEntry = (EditText) findViewById(R.id.editTextCOC);
                String userCOCData = userCOCEntry.getText().toString();
                double userCOC = Double.parseDouble(userCOCData);

                EditText userDistanceEntry = (EditText) findViewById(R.id.editTextDistance);
                String userDistanceData = userDistanceEntry.getText().toString();
                double userDistance = Double.parseDouble(userDistanceData);

                EditText userApertureEntry = (EditText) findViewById(R.id.editTextAperture);
                String userApertureData = userApertureEntry.getText().toString();
                double userAperture = Double.parseDouble(userApertureData);

                // Multiply userDistance by 1000 to convert units from m to mm
                calculateDepthOfField(userCOC, userDistance * 1000, userAperture);
            }
        });
    }

    private void calculateDepthOfField(double userCOC, double userDistance, double userAperture){
        TextView nearFocalDistance = (TextView) findViewById(R.id.txtNearFocalDistance);
        TextView farFocalDistance = (TextView) findViewById(R.id.txtFarFocalDistance);
        TextView depthOfField = (TextView) findViewById(R.id.txtDepthOfField);
        TextView hyperFocalDistance = (TextView) findViewById(R.id.txtHyperFocalDistance);

        if(userAperture < lens.getMaxAperture()){
            nearFocalDistance.setText("Invalid aperture");
            farFocalDistance.setText("Invalid aperture");
            depthOfField.setText("Invalid aperture");
            hyperFocalDistance.setText("Invalid aperture");
        } else{
            DepthOfFieldCalculator doFCalculator = new DepthOfFieldCalculator(
                    lens, userAperture, userDistance, userCOC
            );

            // Divide distances by 1000 to convert units from mm to m
            nearFocalDistance.setText(doFCalculator.nearFocalPoint() / 1000 + "m");
            hyperFocalDistance.setText(doFCalculator.hyperFocalDistance() / 1000 + "m");

            if(doFCalculator.farFocalPoint() == Double.POSITIVE_INFINITY){
                farFocalDistance.setText("Infinitym");
                depthOfField.setText("Infinitym");
            } else{
                farFocalDistance.setText(doFCalculator.farFocalPoint() / 1000 + "m");
                depthOfField.setText(doFCalculator.depthOfField() / 1000 + "m");
            }
        }
    }
}
