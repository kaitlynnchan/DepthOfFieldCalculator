package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lens);

        setupButtonSave();
        setupButtonCancel();
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
