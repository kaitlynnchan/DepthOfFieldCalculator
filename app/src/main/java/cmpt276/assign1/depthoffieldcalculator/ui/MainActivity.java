package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import cmpt276.assign1.depthoffieldcalculator.R;
import cmpt276.assign1.depthoffieldcalculator.model.Lens;
import cmpt276.assign1.depthoffieldcalculator.model.LensManager;

/**
 * Launch Activity
 * Displays lens in a list View
 * Can add lens by the floating action button
 * Can click the lens in list
 *
 * Code for toolbar taken from: https://youtu.be/Fw6v7zFUjWU
 */
public class MainActivity extends AppCompatActivity {

    public static final int RESULT_CODE_ADD_LENS = 42;
    public static final int RESULT_CODE_CALCULATE_DOF = 43;

    private LensManager manager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        manager.add(new Lens("Nikkor", 2.8, 24, R.drawable.icon_picture));
        manager.add(new Lens("Sony", 1.8, 35, R.drawable.icon_picture));
        manager.add(new Lens("Canon", 3.5, 80, R.drawable.icon_picture));
    }

    private void populateListView(){
        ArrayAdapter<Lens> adapter = new MyListAdapter();
        ListView list = findViewById(R.id.listViewLens);
        list.setAdapter(adapter);

        setupEmptyListView(list);

    }

    private class MyListAdapter extends ArrayAdapter<Lens> {
        public MyListAdapter() {
            super(MainActivity.this, R.layout.item_view_lens, manager.getLenses());
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

            ImageView icon = itemView.findViewById(R.id.item_imageViewIcon);
            icon.setImageResource(currentLens.getIconID());
            return itemView;
        }
    }

    private void setupEmptyListView(ListView list) {
        TextView text1 = findViewById(R.id.emptyList_text1);
        TextView text2 = findViewById(R.id.emptyList_text2);
        ImageView arrow = findViewById(R.id.emptyList_imageArrow);
        if(manager.getSize() <= 0){
            list.setVisibility(View.INVISIBLE);
            text1.setVisibility(View.VISIBLE);
            text2.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.VISIBLE);
        } else{
            list.setVisibility(View.VISIBLE);
            text1.setVisibility(View.INVISIBLE);
            text2.setVisibility(View.INVISIBLE);
            arrow.setVisibility(View.INVISIBLE);
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
                int iconID = data.getIntExtra(LensDetailsActivity.EXTRA_USER_ICON_ID, 0);

                manager.add(new Lens(make, aperture, focalLength, iconID));
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
