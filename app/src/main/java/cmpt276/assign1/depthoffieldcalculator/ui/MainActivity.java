package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;

import cmpt276.assign1.depthoffieldcalculator.R;
import cmpt276.assign1.depthoffieldcalculator.model.Lens;
import cmpt276.assign1.depthoffieldcalculator.model.LensManager;

public class MainActivity extends AppCompatActivity {

    public static final int RESULT_CODE_ADD_LENS = 42;
    private LensManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = LensManager.getInstance();

        populateLensManager();
        populateListView();

        FloatingActionButton add = findViewById(R.id.addButton);
        add.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddLensActivity.class);
            startActivityForResult(intent, RESULT_CODE_ADD_LENS);
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
                String make = data.getStringExtra(AddLensActivity.EXTRA_USER_MAKE);
                int focalLength = data.getIntExtra(AddLensActivity.EXTRA_USER_FOCAL_lENGTH, 0);
                double aperture = data.getDoubleExtra(AddLensActivity.EXTRA_USER_APERTURE, 0);

                manager.add(new Lens(make, aperture, focalLength));
                populateListView();

                // Temporary, replace with snackbar
                Toast.makeText(MainActivity.this, "added new lens", Toast.LENGTH_LONG).show();

                break;
        }

    }

    private void populateLensManager(){
        manager.add(new Lens("Nikkor", 2.8, 24));
        manager.add(new Lens("Sony", 1.8, 35));
        manager.add(new Lens("Canon", 3.5, 80));
    }

    private void populateListView(){
        ArrayAdapter<LensManager> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.listViewLens);
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

            TextView makeText = (TextView) itemView.findViewById(R.id.item_textViewMake);
            makeText.setText(currentLens.getMake());

            TextView focalLengthText = (TextView) itemView.findViewById(R.id.item_textViewFocalLength);
            focalLengthText.setText(currentLens.getFocalLength() + "mm");

            TextView apertureText = (TextView) itemView.findViewById(R.id.item_textViewAperture);
            apertureText.setText("F" + currentLens.getMaxAperture());

            return itemView;
        }
    }

    // Create updateUI()
}
