package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;

import cmpt276.assign1.depthoffieldcalculator.R;
import cmpt276.assign1.depthoffieldcalculator.model.Lens;
import cmpt276.assign1.depthoffieldcalculator.model.LensManager;

/**
 * Launch Activity
 * Displays lens in a list View
 * Can add lens by the floating action button
 * Can click the lens in list
 */
public class MainActivity extends AppCompatActivity {

    public static final int RESULT_CODE_ADD_LENS = 42;
    public static final int RESULT_CODE_CALCULATE_DOF = 43;

    private LensManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = LensManager.getInstance();

        populateLensManager();
        populateListView();
        registerClickCallback();

        FloatingActionButton add = findViewById(R.id.buttonAdd);
        add.setOnClickListener(view -> {
            Intent intent = LensDetailsActivity.makeLaunchIntent(MainActivity.this, false);
            startActivityForResult(intent, RESULT_CODE_ADD_LENS);
        });
    }

    private void populateLensManager(){
        manager.add(new Lens("Nikkor", 2.8, 24));
        manager.add(new Lens("Sony", 1.8, 35));
        manager.add(new Lens("Canon", 3.5, 80));
    }

    private void populateListView(){
        ArrayAdapter<LensManager> adapter = new MyListAdapter();
        ListView list = findViewById(R.id.listViewLens);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<LensManager> {
        public MyListAdapter() {
            super(MainActivity.this, R.layout.item_view_lens, Collections.singletonList(manager));
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item_view_lens, parent, false);
            }

            Lens currentLens = manager.getLens(position);

            TextView makeText = itemView.findViewById(R.id.item_textViewMake);
            makeText.setText(currentLens.getMake());

            TextView focalLengthText = itemView.findViewById(R.id.item_textViewFocalLength);
            focalLengthText.setText(currentLens.getFocalLength() + "mm");

            TextView apertureText = itemView.findViewById(R.id.item_textViewAperture);
            apertureText.setText("F" + currentLens.getMaxAperture());

            return itemView;
        }
    }

    private void registerClickCallback() {
        ListView list = findViewById(R.id.listViewLens);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = CalculateDepthOfFieldActivity.makeLaunchIntent(
                        MainActivity.this,
                        position
                );
                startActivityForResult(intent, RESULT_CODE_CALCULATE_DOF);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_CANCELED){
            String message = "Canceled";
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode){
            case RESULT_CODE_ADD_LENS:

                // Unneeded snackbar
                Snackbar.make(findViewById(R.id.myMainActivity),
                        "Added new lens",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();

                String make = data.getStringExtra(LensDetailsActivity.EXTRA_USER_MAKE);
                int focalLength = data.getIntExtra(LensDetailsActivity.EXTRA_USER_FOCAL_lENGTH, 0);
                double aperture = data.getDoubleExtra(LensDetailsActivity.EXTRA_USER_APERTURE, 0);

                manager.add(new Lens(make, aperture, focalLength));
                populateListView();

                break;

            case RESULT_CODE_CALCULATE_DOF:
                populateListView();
                break;
            default:
                assert false;
        }
    }

}
