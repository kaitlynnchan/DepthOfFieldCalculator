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
import cmpt276.assign1.depthoffieldcalculator.model.Lens;
import cmpt276.assign1.depthoffieldcalculator.model.LensManager;

public class AddLensActivity extends AppCompatActivity {

    public static final String EXTRA_USER_MAKE = "user make";
    public static final String EXTRA_USER_FOCAL_lENGTH = "user focal length";
    public static final String EXTRA_USER_APERTURE = "user aperture";

    private static final String EXTRA_EXISTED = "existed lens";
    private static final String EXTRA_LENS_INDEX = "extra lens index";
    private LensManager manager;
    private Boolean existed;
    private Lens lens;

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
            int lensIndex = intent.getIntExtra(EXTRA_LENS_INDEX, 0);
            setEditTexts(lensIndex);
        }

        setupButtonSave();
        setupButtonCancel();
    }

    private void setEditTexts(int lensIndex) {
        manager = LensManager.getInstance();
        lens = manager.getLens(lensIndex);

        EditText userMakeEntry = (EditText) findViewById(R.id.editTextMake);
        userMakeEntry.setText(lens.getMake());

        EditText userFocalLengthEntry = (EditText) findViewById(R.id.editTextFocalLength);
        userFocalLengthEntry.setText("" + lens.getFocalLength());

        EditText userApertureEntry = (EditText) findViewById(R.id.editTextAperture);
        userApertureEntry.setText("" + lens.getMaxAperture());
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

                if(existed){
                    if(lens.getMake() != userMake){
                        lens.setMake(userMake);
                    }
                    if(lens.getFocalLength() != userFocalLength){
                        lens.setFocalLength(userFocalLength);
                    }
                    if(lens.getMaxAperture() != userAperture){
                        lens.setMaxAperture(userAperture);
                    }
                } else{
                    intent.putExtra(EXTRA_USER_MAKE, userMake);
                    intent.putExtra(EXTRA_USER_FOCAL_lENGTH, userFocalLength);
                    intent.putExtra(EXTRA_USER_APERTURE, userAperture);
                }

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
