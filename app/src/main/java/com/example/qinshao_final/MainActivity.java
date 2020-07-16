package com.example.qinshao_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    final static int REQUEST_CODE1 = 1;
    final static int REQUEST_CODE2 = 2;

    Button btnAddNew;
    ListView listView;
    ArrayList<Custemer> listOfAllRecord = new ArrayList<>();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        btnAddNew = findViewById(R.id.btnAddNewCustomer);
        listView = findViewById(R.id.lvDisplay);
        listOfAllRecord.add(new Custemer("1", new Date(), 1000, "Jim", "Smith", "5141234567", "123456"));
        listOfAllRecord.add(new Custemer("2", new Date(), 1000, "Tim", "Filed", "5147654321", "111111"));
        listOfAllRecord.add(new Custemer("3", new Date(), 1000, "Tom", "Lora", "0123456789", "222222"));

        Collections.sort(listOfAllRecord);

        showListOfResultRecord(listOfAllRecord);
    }

    public void AddNewCustomer(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bundleExtra", listOfAllRecord);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("intentExtra", bundle);
        startActivityForResult(intent, REQUEST_CODE1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE1 && resultCode == RESULT_OK) {
            Bundle bundleinside = data.getBundleExtra("intentExtra");

            Serializable b = bundleinside.getSerializable("bundleExtra");
            listOfAllRecord = (ArrayList<Custemer>) b;
            showListOfResultRecord(listOfAllRecord);
        }

        if(requestCode==REQUEST_CODE2 && resultCode==RESULT_OK){

                Bundle bundle = data.getBundleExtra("return_result");
                Serializable bundleListOfCustomer = bundle.getSerializable("bundleExtra");

            listOfAllRecord = (ArrayList<Custemer>) bundleListOfCustomer;
            showListOfResultRecord(listOfAllRecord);

        }
    }

    private void showListOfResultRecord(ArrayList<Custemer> list) {

        ArrayAdapter<Custemer> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list
        );
        listView.setAdapter(listAdapter);

       AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("bundleExtra", listOfAllRecord);
                bundle.putSerializable("bundleCurrent", position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("intentExtra", bundle);
                startActivityForResult(intent,REQUEST_CODE1);

            }
        };

        listView.setOnItemClickListener(itemClickListener);

        AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Custemer customer = listOfAllRecord.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bundleExtra", listOfAllRecord);
                bundle.putSerializable("bundleCurrentObj", customer);
                Intent myIntent = new Intent(MainActivity.this, WithdrawActivity.class);
                myIntent.putExtra("intentExtra", bundle);
                startActivityForResult(myIntent,REQUEST_CODE2);
                return true;
            }


        };

        listView.setOnItemLongClickListener(itemLongClickListener);
    }

}