package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import cmpt276.assign1.depthoffieldcalculator.R;

/**
 * Displays and allows user to input make, focal length, aperture of lens, and icon
 * Able to add or edit lens
 * Sets a default icon if user does not choose one
 *
 * Code for toolbar back button taken from:
 * https://stackoverflow.com/questions/26651602/display-back-arrow-on-toolbar
 */
public class LensDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_USER_MAKE = "user make";
    public static final String EXTRA_USER_FOCAL_lENGTH = "user focal length";
    public static final String EXTRA_USER_APERTURE = "user aperture";
    public static final String EXTRA_USER_ICON_ID = "user icon ID";

    private static final String EXTRA_EXISTED = "existed lens";
    private int currentIconID;

    public static Intent makeLaunchIntent(Context context, Boolean existed){
        Intent intent = new Intent(context, LensDetailsActivity.class);
        intent.putExtra(EXTRA_EXISTED, existed);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lens_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Lens");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Boolean existed = intent.getBooleanExtra(EXTRA_EXISTED, false);
        if(existed){
            getSupportActionBar().setTitle("Edit Lens");

            String make = intent.getStringExtra(CalculateDepthOfFieldActivity.EXTRA_LENS_MAKE);
            int focalLength = intent.getIntExtra(CalculateDepthOfFieldActivity.EXTRA_LENS_FOCAL_LENGTH, 0);
            double aperture = intent.getDoubleExtra(CalculateDepthOfFieldActivity.EXTRA_LENS_APERTURE, 0);
            int iconID = intent.getIntExtra(CalculateDepthOfFieldActivity.EXTRA_LENS_ICON_ID, 0);
            currentIconID = iconID;
            setExistedParameters(make, focalLength, aperture, iconID);
        }

        setupButtonIcons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lens_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();

        switch (item.getItemId()){
            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

            case R.id.menu_itemSave:
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
                    return false;
                }

                int userFocalLength = Integer.parseInt(userFocalLengthData);
                double userAperture = Double.parseDouble(userApertureData);
                if(currentIconID == R.drawable.ic_add_a_photo_grey_24dp){
                    currentIconID = R.drawable.icon_no_image;
                }

                if(userAperture < 1.4){
                    Toast.makeText(LensDetailsActivity.this,
                            "Aperture cannot be less than 1.4",
                            Toast.LENGTH_LONG)
                            .show();
                } else{
                    intent.putExtra(EXTRA_USER_MAKE, userMake);
                    intent.putExtra(EXTRA_USER_FOCAL_lENGTH, userFocalLength);
                    intent.putExtra(EXTRA_USER_APERTURE, userAperture);
                    intent.putExtra(EXTRA_USER_ICON_ID, currentIconID);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

            default:
                assert false;

        }
        return super.onOptionsItemSelected(item);
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
        if(currentIconID == 0){
            currentIconID = R.drawable.ic_add_a_photo_grey_24dp;
        }

        ImageButton btnIconLensYellow = findViewById(R.id.buttonIconLensYellow);
        btnIconLensYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButtonIcon(icon, R.drawable.icon_lens_yellow);
            }
        });

        ImageButton btnIconLensBlue = findViewById(R.id.buttonIconLensBlue);
        btnIconLensBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButtonIcon(icon, R.drawable.icon_lens_blue);
            }
        });

        ImageButton btnIconLensRed = findViewById(R.id.buttonIconLensRed);
        btnIconLensRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButtonIcon(icon, R.drawable.icon_lens_red);
            }
        });

        ImageButton btnIconLensBlack1 = findViewById(R.id.buttonIconLensBlack1);
        btnIconLensBlack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButtonIcon(icon, R.drawable.icon_lens_black1);
            }
        });

        ImageButton btnIconLensBlack2 = findViewById(R.id.buttonIconLensBlack2);
        btnIconLensBlack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButtonIcon(icon, R.drawable.icon_lens_black2);
            }
        });

        ImageButton btnIconLensBlack3 = findViewById(R.id.buttonIconLensBlack3);
        btnIconLensBlack3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButtonIcon(icon, R.drawable.icon_lens_black3);
            }
        });

        ImageButton btnIconPicture = findViewById(R.id.buttonIconPicture);
        btnIconPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButtonIcon(icon, R.drawable.icon_picture);
            }
        });

        ImageButton btnIconNoImage = findViewById(R.id.buttonIconNoImage);
        btnIconNoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButtonIcon(icon, R.drawable.icon_no_image);
            }
        });
    }

    private void selectButtonIcon(ImageView icon, int drawableID){
        // Select and deselect icon
        if(currentIconID == drawableID){
            currentIconID = R.drawable.ic_add_a_photo_grey_24dp;
            icon.setImageResource(R.drawable.ic_add_a_photo_grey_24dp);
        } else{
            icon.setImageResource(drawableID);
            currentIconID = drawableID;
        }
    }

}
