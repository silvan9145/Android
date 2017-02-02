package com.davidsilvan.fileconcealer;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends ListActivity {

    private ListView listView;
    private TextView currentDirLabel, currentDir;
    private String root;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private List<String> pathList;
    private AlertDialog dialog;
    private ProgressDialog progressDialog;
    public final static String MESSAGE = "com.davidsilvan.fileconcealer.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        root = Environment.getExternalStorageDirectory().getPath();
        currentDirLabel = (TextView) findViewById(R.id.currentDirLabel);
        currentDir = (TextView) findViewById(R.id.currentDir);
        listView = (ListView) findViewById(android.R.id.list);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_main, null));
        dialog = builder.create();

        setup(root);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        if (dialog.isShowing()) {
            dialog.cancel();
        }
    }

    private void setup(String path) {
        currentDirLabel.setText("Current:");
        currentDir.setText(path);
        itemList = new ArrayList<Item>();
        pathList = new ArrayList<String>();

        File f = new File(path);
        File[] files = f.listFiles();
        Arrays.sort(files);
        if (files != null) {
            if (!path.equals(root)) {
                itemList.add(new Item("../"));
                pathList.add(f.getParent());
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    itemList.add(new Item(files[i].getName() + "/"));
                }
                else {
                    itemList.add(new Item(files[i].getName()));
                }

                pathList.add(path + File.separator + files[i].getName());
            }
        }
        itemAdapter = new ItemAdapter(this, R.layout.row, itemList);
        listView.setAdapter(itemAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final String str = pathList.get(position);
        File f = new File(str);

        if (f.isDirectory())
        {
            if (f.canRead()) {
                setup(str);
            }
        }
        else {
            dialog.show();
            TextView fileNameText = (TextView) dialog.findViewById(R.id.fileNameTextView);
            Button viewButton = (Button) dialog.findViewById(R.id.viewButton);
            Button encryptButton = (Button) dialog.findViewById(R.id.encryptButton);

            fileNameText.setText("Filename: " + getFileName(str));

            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    Intent intent = new Intent(v.getContext(), ViewFileActivity.class);
                    intent.putExtra(MESSAGE, str);
                    startActivity(intent);
                }
            });

            encryptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    private String getFileName(String str) {
        String[] tokens = str.split("/");
        return tokens[tokens.length - 1];
    }
}
