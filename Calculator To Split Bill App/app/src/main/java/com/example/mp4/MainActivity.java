package com.example.mp4;

import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner theSpinnerrr;
    private EditText tab;
    private TextView otherTip, othertotal, percentForSliderO;
    private SeekBar theSlider0;
    private double amountWithoutTip, tip, total, tipSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tipSeekBar = 0;

        theSpinnerrr = findViewById(R.id.theSpinner);
        theSpinnerrr.setSelection(0);

        tab = findViewById(R.id.tabb);
        otherTip = findViewById(R.id.tip);
        othertotal = findViewById(R.id.total);
        percentForSliderO = findViewById(R.id.percentForSlider);
        theSlider0 = findViewById(R.id.theSlider);

        theSpinnerrr.setSelection(0);

        theSpinnerrr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int position, long aa) {
                String itemPicked = adapterView.getItemAtPosition(position).toString();
                int amountOfPeople = Integer.parseInt(itemPicked);
                updateSplitUI(amountOfPeople);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tab.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence cs, int beginning, int number, int end) {
            }

            public void onTextChanged(CharSequence cs, int beginning, int previous, int number) {
            }

            public void afterTextChanged(Editable cs) {
                try {
                    amountWithoutTip = Double.parseDouble(tab.getText().toString());
                } catch (Exception ex) {
                }
                updateUI();
            }
        });

        theSlider0.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar theSlider0, int progress, boolean b) {
                tipSeekBar = progress;
                updateSeekUI();
            }

            public void onStartTrackingTouch(SeekBar theSlider0) {
            }

            public void onStopTrackingTouch(SeekBar theSlider0) {
            }
        });
    }

    private void updateSeekUI() {
        percentForSliderO.setText(tipSeekBar + "%");

        tip = amountWithoutTip * tipSeekBar / 100.0;
        total = amountWithoutTip + tip;
        otherTip.setText(String.valueOf(tip));
        othertotal.setText(String.valueOf(total));
    }

    private void updateUI() {
        tip = Math.round(tipSeekBar * amountWithoutTip) / 100.0;
        otherTip.setText(String.valueOf(tip));

        total = amountWithoutTip + tip;
        othertotal.setText(String.valueOf(total));
    }

    public void ClickedIt(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        if (view.getId() == R.id.radioButtonNo) {
            if (checked) {
                updateUI();
            }
        } else if (view.getId() == R.id.radioButtonTip) {
            if (checked) {
                updateTipRUI();
            }
        } else if (view.getId() == R.id.radioButtonTotal) {
            if (checked) {
                updateTotalRUI();
            }
        }
    }

    private void updateTipRUI() {
        double tipR = Math.ceil(tip);
        double totalR = amountWithoutTip + tipR;

        otherTip.setText(String.valueOf(tipR));
        othertotal.setText(String.valueOf(totalR));
    }

    private void updateTotalRUI() {
        double TotalR = Math.ceil(total);

        otherTip.setText(String.valueOf(tip));
        othertotal.setText(String.valueOf(TotalR));
    }

    private void updateSplitUI(int numberOfPeople) {
        double totallSplit = total / numberOfPeople;

        TextView splitTotalTextView = findViewById(R.id.totalSplit);
        splitTotalTextView.setText(String.valueOf(totallSplit));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mI) {
        int id = mI.getItemId();
        if (id == R.id.shareButton) {
            shareButtonnn();
            return true;
        } else if (id == R.id.infoButton) {

            infoDialog();
            return true;
        }
        return super.onOptionsItemSelected(mI);

    }

    private void infoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Info")
                .setMessage("Spinner is used to split the total among friends")
                .setPositiveButton("OK", null)
                .show();
    }

    private void shareButtonnn() {
        String billT = tab.getText().toString();
        String tipT = otherTip.getText().toString();
        String totalT = othertotal.getText().toString();
        String splitT = ((TextView) findViewById(R.id.totalSplit)).getText().toString();

        String mes = "Bill: $" + billT + "\nTip: $" + tipT + "\nTotal: $" + totalT + "\nPer Person: $" + splitT;

        Intent toShare = new Intent(Intent.ACTION_SEND);
        toShare.setType("text/plain");
        toShare.putExtra(Intent.EXTRA_TEXT, mes);

        PackageManager packageManager = getPackageManager();
        ResolveInfo resInfo = packageManager.resolveActivity(toShare, PackageManager.MATCH_DEFAULT_ONLY);
        if (resInfo != null) {
            String shareIinfo = getResources().getString(R.string.shareee);
            Intent chooserIntent = Intent.createChooser(toShare, shareIinfo);
            if (toShare.resolveActivity(packageManager) != null) {
                startActivity(chooserIntent);
            }
        }
    }






}



