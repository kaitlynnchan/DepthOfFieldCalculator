package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import cmpt276.assign1.depthoffieldcalculator.R;
import cmpt276.assign1.depthoffieldcalculator.model.Lens;
import cmpt276.assign1.depthoffieldcalculator.model.LensManager;

public class CalculateDepthOfFieldActivity extends AppCompatActivity {

    private static final String EXTRA_LENS_INDEX = "Extra-lens-Index";

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

        LensManager manager = LensManager.getInstance();
        Lens l = manager.getLens(lensIndex);

        TextView lens = (TextView) findViewById(R.id.textViewLens);
        lens.setText(l.toString());
    }
}
