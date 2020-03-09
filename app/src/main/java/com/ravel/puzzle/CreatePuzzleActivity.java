package com.ravel.puzzle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rp.puzzle.bean.Block;
import com.rp.puzzle.view.PuzzleView;

import org.json.JSONArray;
import org.json.JSONObject;

public class CreatePuzzleActivity extends AppCompatActivity {

    private TextView mTextViewGridSize;
    private PuzzleView mPuzzleView;
    private SeekBar mSeekBarGridSize;
    private String imagePath;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_puzzle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imagePath = getIntent().getExtras().getString("imagePath");
        mBitmap = BitmapFactory.decodeFile(imagePath);
        mTextViewGridSize = (TextView) findViewById(R.id.tv_grid_size);
        mPuzzleView = (PuzzleView) findViewById(R.id.puzzle_view);
        mSeekBarGridSize = (SeekBar) findViewById(R.id.sb_grid_size);
        mTextViewGridSize.setText(String.format(getString(R.string.grid_size), 2, 2));
        mSeekBarGridSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progress = seekBar.getProgress() + 2;
                mTextViewGridSize.setText(String.format(getString(R.string.grid_size), progress, progress));
                createPuzzle();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        createPuzzle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        else if (item.getItemId() == R.id.menu_add) {
            savePuzzle();
        }
        return super.onOptionsItemSelected(item);

    }

    private void createPuzzle() {
        try {
            mPuzzleView.createPuzzle(mBitmap, mSeekBarGridSize.getProgress() + 2);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_puzzle_menu, menu);
        return true;
    }

    private void savePuzzle() {
        try {
            JSONObject mJsonObjectPuzzleInfo = new JSONObject();
            mJsonObjectPuzzleInfo.put("puzzle_size", mPuzzleView.getPuzzleSize());
            mJsonObjectPuzzleInfo.put("puzzle_image", imagePath);
            JSONArray positionArray = new JSONArray();
            Block[][] mPuzzleViewPuzzleBlockState = mPuzzleView.getPuzzleBlockState();
            for (int i = 0; i < mPuzzleViewPuzzleBlockState.length; i++) {
                for (int j = 0; j < mPuzzleViewPuzzleBlockState.length; j++) {
                    Block block = mPuzzleViewPuzzleBlockState[i][j];
                    JSONObject mJsonObject = new JSONObject();
                    mJsonObject.put("realX", block.getRealX());
                    mJsonObject.put("realY", block.getRealY());
                    mJsonObject.put("currentX", block.getCurrentX());
                    mJsonObject.put("currentY", block.getCurrentY());
                    positionArray.put(mJsonObject);
                }
            }
            mJsonObjectPuzzleInfo.put("positionArray", positionArray);
            Utils.savePuzzle(this, mJsonObjectPuzzleInfo);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
