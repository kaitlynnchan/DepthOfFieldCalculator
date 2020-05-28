package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cmpt276.assign1.depthoffieldcalculator.R;

/**
 * Displays and allows user to input make, focal length, and aperture of lens
 * Able to add or edit lens
 */
public class LensDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_USER_MAKE = "user make";
    public static final String EXTRA_USER_FOCAL_lENGTH = "user focal length";
    public static final String EXTRA_USER_APERTURE = "user aperture";

    private static final String EXTRA_EXISTED = "existed lens";

    public static Intent makeLaunchIntent(Context context, Boolean existed){
        Intent intent = new Intent(context, LensDetailsActivity.class);
        intent.putExtra(EXTRA_EXISTED, existed);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lens);

        Intent intent = getIntent();
        Boolean existed = intent.getBooleanExtra(EXTRA_EXISTED, false);
        if(existed){
            String make = intent.getStringExtra(CalculateDepthOfFieldActivity.EXTRA_LENS_MAKE);
            int focalLength = intent.getIntExtra(CalculateDepthOfFieldActivity.EXTRA_LENS_FOCAL_LENGTH, 0);
            double aperture = intent.getDoubleExtra(CalculateDepthOfFieldActivity.EXTRA_LENS_APERTURE, 0);
            setEditTexts(make, focalLength, aperture);
        }

        setupButtonSave();
        setupButtonCancel();
    }

    private void setEditTexts(String make, int focalLength, double aperture) {
        EditText userMakeEntry = findViewById(R.id.editTextMake);
        userMakeEntry.setText(make);

        EditText userFocalLengthEntry = findViewById(R.id.editTextFocalLength);
        userFocalLengthEntry.setText("" + focalLength);

        EditText userApertureEntry = findViewById(R.id.editTextAperture);
        userApertureEntry.setText("" + aperture);
    }

    private void setupButtonSave() {
        Button btnSave = findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userMakeEntry = findViewById(R.id.editTextMake);
                String userMake = userMakeEntry.getText().toString();

                EditText userFocalLengthEntry = findViewById(R.id.editTextFocalLength);
                String userFocalLengthData = userFocalLengthEntry.getText().toString();

                EditText userApertureEntry = findViewById(R.id.editTextAperture);
                String userApertureData = userApertureEntry.getText().toString();

                if(userMake.isEmpty()
                        || userFocalLengthData.isEmpty()
                        || userApertureData.isEmpty())
                {
                    Toast.makeText(LensDetailsActivity.this,
                            "Make, Focal length, and Aperture cannot be empty",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                int userFocalLength = Integer.parseInt(userFocalLengthData);
                double userAperture = Double.parseDouble(userApertureData);

                if(userAperture < 1.4){
                    Toast.makeText(LensDetailsActivity.this,
                            "Aperture cannot be less than 1.4",
                            Toast.LENGTH_LONG)
                            .show();
                } else{
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_USER_MAKE, userMake);
                    intent.putExtra(EXTRA_USER_FOCAL_lENGTH, userFocalLength);
                    intent.putExtra(EXTRA_USER_APERTURE, userAperture);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

            }
        });
    }

    private void setupButtonCancel() {
        Button btnCancel = findViewById(R.id.buttonCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
    }


}
