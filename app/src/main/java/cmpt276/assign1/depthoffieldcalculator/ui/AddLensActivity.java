package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import cmpt276.assign1.depthoffieldcalculator.R;

public class AddLensActivity extends AppCompatActivity {

    public static final String EXTRA_USER_MAKE = "user make";
    public static final String EXTRA_USER_FOCAL_lENGTH = "user focal length";
    public static final String EXTRA_USER_APERTURE = "user aperture";

    private static final String EXTRA_EXISTED = "existed lens";
    private Boolean existed;

    public static Intent makeLaunchIntent(Context context, Boolean existed){
        Intent intent = new Intent(context, AddLensActivity.class);
        intent.putExtra(EXTRA_EXISTED, existed);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lens);

        Intent intent = getIntent();
        existed = intent.getBooleanExtra(EXTRA_EXISTED, false);
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
        EditText userMakeEntry = (EditText) findViewById(R.id.editTextMake);
        userMakeEntry.setText(make);

        EditText userFocalLengthEntry = (EditText) findViewById(R.id.editTextFocalLength);
        userFocalLengthEntry.setText("" + focalLength);

        EditText userApertureEntry = (EditText) findViewById(R.id.editTextAperture);
        userApertureEntry.setText("" + aperture);
    }

    private void setupButtonSave() {
        Button btnSave = (Button) findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userMakeEntry = (EditText) findViewById(R.id.editTextMake);
                String userMake = userMakeEntry.getText().toString();

                EditText userFocalLengthEntry = (EditText) findViewById(R.id.editTextFocalLength);
                String userFocalLengthData = userFocalLengthEntry.getText().toString();
                int userFocalLength = Integer.parseInt(userFocalLengthData);

                EditText userApertureEntry = (EditText) findViewById(R.id.editTextAperture);
                String userApertureData = userApertureEntry.getText().toString();
                double userAperture = Double.parseDouble(userApertureData);

                Intent intent = new Intent();
                intent.putExtra(EXTRA_USER_MAKE, userMake);
                intent.putExtra(EXTRA_USER_FOCAL_lENGTH, userFocalLength);
                intent.putExtra(EXTRA_USER_APERTURE, userAperture);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setupButtonCancel() {
        Button btnCancel = (Button) findViewById(R.id.buttonCancel);
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
