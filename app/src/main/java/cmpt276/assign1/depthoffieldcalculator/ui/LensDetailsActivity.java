package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    public static final String EXTRA_USER_ICON_ID = "user icon ID";

    private static final String EXTRA_EXISTED = "existed lens";
    private int iconID;

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
            int iconID = intent.getIntExtra(CalculateDepthOfFieldActivity.EXTRA_LENS_ICON_ID, 0);
            setExistedParameters(make, focalLength, aperture, iconID);
        }

        setupButtonIcons();
        setupButtonSave();
        setupButtonCancel();
    }


    private void setExistedParameters(String make, int focalLength, double aperture, int iconID) {
        EditText existedMake = findViewById(R.id.editTextMake);
        existedMake.setText(make);

        EditText existedFocalLength = findViewById(R.id.editTextFocalLength);
        existedFocalLength.setText("" + focalLength);

        EditText existedAperture = findViewById(R.id.editTextAperture);
        existedAperture.setText("" + aperture);

        ImageView existedIcon = findViewById(R.id.imageViewIcon);
        existedIcon.setImageResource(iconID);
    }

    private void setupButtonIcons() {
        ImageView icon = findViewById(R.id.imageViewIcon);

        ImageButton btnIconLensYellow = findViewById(R.id.buttonIconLensYellow);
        btnIconLensYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon.setImageResource(R.drawable.icon_lens_yellow);
                iconID = R.drawable.icon_lens_yellow;
            }
        });

        ImageButton btnIconLensBlue = findViewById(R.id.buttonIconLensBlue);
        btnIconLensBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon.setImageResource(R.drawable.icon_lens_blue);
                iconID = R.drawable.icon_lens_blue;
            }
        });

        ImageButton btnIconLensRed = findViewById(R.id.buttonIconLensRed);
        btnIconLensRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon.setImageResource(R.drawable.icon_lens_red);
                iconID = R.drawable.icon_lens_red;
            }
        });

        ImageButton btnIconLensBlack1 = findViewById(R.id.buttonIconLensBlack1);
        btnIconLensBlack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon.setImageResource(R.drawable.icon_lens_black1);
                iconID = R.drawable.icon_lens_black1;
            }
        });

        ImageButton btnIconLensBlack2 = findViewById(R.id.buttonIconLensBlack2);
        btnIconLensBlack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon.setImageResource(R.drawable.icon_lens_black2);
                iconID = R.drawable.icon_lens_black2;
            }
        });

        ImageButton btnIconLensBlack3 = findViewById(R.id.buttonIconLensBlack3);
        btnIconLensBlack3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon.setImageResource(R.drawable.icon_lens_black3);
                iconID = R.drawable.icon_lens_black3;
            }
        });

        ImageButton btnIconPicture = findViewById(R.id.buttonIconPicture);
        btnIconPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon.setImageResource(R.drawable.icon_picture);
                iconID = R.drawable.icon_picture;
            }
        });
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
                    intent.putExtra(EXTRA_USER_ICON_ID, iconID);

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
