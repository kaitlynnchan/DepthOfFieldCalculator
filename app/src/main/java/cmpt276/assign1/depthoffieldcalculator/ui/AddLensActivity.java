package cmpt276.assign1.depthoffieldcalculator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cmpt276.assign1.depthoffieldcalculator.R;

public class AddLensActivity extends AppCompatActivity {

    private static final String EXTRA_MESSAGE = "Extra-message";

    public static Intent makeLaunchIntent(Context c){ //, String message
        Intent intent = new Intent(c, AddLensActivity.class);
//        intent.putExtra(EXTRA_MESSAGE, message);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lens);

        Intent i = getIntent();
//        String message = i.getStringExtra(EXTRA_MESSAGE);
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        Button btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnCancel.setOnClickListener(v -> Toast.makeText(
                getApplicationContext(),
                "test cancel button",
                Toast.LENGTH_LONG).show()
        );
    }
}
