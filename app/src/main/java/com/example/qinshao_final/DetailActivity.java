package com.example.qinshao_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    final static int REQUEST_CODE2=2;
    ArrayList<String> erroMsg ;
    EditText tbAccountNumber, tbOpenDate, tbFirstName, tbFamilyName, tbPhone, tbSin, tbBalance;
    Button btnAddInDetail, btnFind, btnClear, btnRemove, btnShowAll, btnUpdate,btnLoad,btnSave;
    ArrayList<Custemer> listOfAllRecord;
    int customerIndex;
    File external_private_storage;
    int REQUEST_CODE = 1;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    AlertDialog.Builder alertDialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        listOfAllRecord = null;
        customerIndex = -1;
        init();
    }

    private void init() {

        alertDialogBuilder = new AlertDialog.Builder(this);

        btnAddInDetail = findViewById(R.id.btnAddInDetail);
        btnAddInDetail.setOnClickListener(this);
        btnFind = findViewById(R.id.btnFind);
        btnFind.setOnClickListener(this);
        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        btnRemove = findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(this);
        btnShowAll = findViewById(R.id.btnShowAll);
        btnShowAll.setOnClickListener(this);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        btnLoad = findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(this);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);


        tbAccountNumber = findViewById(R.id.tbAccountNumber);

        tbOpenDate = findViewById(R.id.tbOpenDate);

        tbFirstName = findViewById(R.id.tbFirstName);
        tbFamilyName = findViewById(R.id.tbFamilyName);
        tbPhone = findViewById(R.id.tbPhone);
        tbBalance = findViewById(R.id.tbBalance);

        tbSin = findViewById(R.id.tbSin);


        Bundle bundle = getIntent().getBundleExtra("intentExtra");
        assert bundle != null;
        Serializable b = bundle.getSerializable("bundleExtra");
        Serializable bundeCurrentCustomer = bundle.getSerializable("bundleCurrent");
        listOfAllRecord = (ArrayList<Custemer>)b;


        if(bundeCurrentCustomer!=null){
            customerIndex = (int)bundeCurrentCustomer;
            Custemer c= listOfAllRecord.get(customerIndex);
            tbAccountNumber.setText(c.getAccountNum());
            tbOpenDate.setText(dateFormat.format( c.getOpenDate()));
            tbFirstName.setText(c.getFirstName());
            tbFamilyName.setText(c.getFamilyName());
            tbPhone.setText(c.getPhoneNumber());
            tbBalance.setText( c.getBalance()+"");
            tbSin.setText(c.getSinNumber());

        }else{
            btnUpdate.setEnabled(false);
            tbOpenDate.setText(dateFormat.format(new Date()));
        }
        ActivityCompat.requestPermissions(this,
                new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},
                REQUEST_CODE);

        external_private_storage = new File(getExternalFilesDir("private_file"),"external_private_storage");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddInDetail:
                toAddInDetail();
                break;
            case R.id.btnClear:
                toClear();
                break;
            case R.id.btnFind:
                toFind();
                break;
            case R.id.btnUpdate:
                toUpdate();
                break;

            case R.id.btnRemove:
                toRemove();
                break;

            case R.id.btnShowAll:
                toShowAll();
                break;
            case R.id.btnSave:
                toSave();
                break;

            case R.id.btnLoad:
                toLoad();
                break;

        }
    }

    private void toLoad() {
         alertDialogBuilder.setTitle("Load")
                .setMessage("want to discard current changes and load new data?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Object object = null;
                        try {

                            FileInputStream fileInputStream = new FileInputStream(external_private_storage);
                            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                            object = objectInputStream.readObject();
                            fileInputStream.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        listOfAllRecord = (ArrayList<Custemer>)object;
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("bundleExtra", listOfAllRecord);
                        intent.putExtra("intentExtra",bundle);
                        setResult(RESULT_OK,intent);
                        Toast.makeText(DetailActivity.this,"The data is loaded successfully.",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("No",this);

        alertDialogBuilder.show();
    }

    private void toSave() {
        try {

            FileOutputStream fileOutputStream = new FileOutputStream(external_private_storage);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(listOfAllRecord);

            fileOutputStream.close();
            Toast.makeText(DetailActivity.this,"Data's saved successfully.",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toUpdate() {
        if(customerIndex!=-1){
            Custemer c=listOfAllRecord.get(customerIndex);
            String name = tbFirstName.getText().toString();
            String family = tbFamilyName.getText().toString();
            String phone = tbPhone.getText().toString();
            String sin = tbSin.getText().toString();

            String accountNumber = tbAccountNumber.getText().toString();

            if(accountNumber.equals("")){

                alertDialogBuilder.setTitle("Error:\n")
                        .setMessage("The account number can not be empty.")
                        .setPositiveButton("Yes", this);

                alertDialogBuilder.show();
                return;
            }

            if(!isUnique(accountNumber,c)){
                alertDialogBuilder.setTitle("Error:\n")
                        .setMessage("Account number exits.")
                        .setPositiveButton("Yes", this);
                alertDialogBuilder.show();
                return;
            }
            String strOpenDate = tbOpenDate.getText().toString();
            String strBalance = tbBalance.getText().toString();
            Date openDate;
            try{
                openDate =  dateFormat.parse(strOpenDate);
            }catch(ParseException ex)
            {
                alertDialogBuilder.setTitle("Error:\n")
                        .setMessage("Invalidate open date.")
                        .setPositiveButton("Yes", this);
                alertDialogBuilder.show();

                return;
            }

            double balance = Double.parseDouble(strBalance);

            Custemer customer = new Custemer(accountNumber, openDate, balance, name, family, phone, sin);

            listOfAllRecord.set(customerIndex,customer);
            Toast.makeText(DetailActivity.this,"The customer updated successfully.",Toast.LENGTH_SHORT).show();


        }

    }

    private void toFind() {
        String toRemove = tbSin.getText().toString();
        if(toRemove.length()==0){

            alertDialogBuilder.setTitle("Error:\n")
                    .setMessage("input first")
                    .setPositiveButton("Yes", this);

            alertDialogBuilder.show();
            return;

        }else if(hasIt(toRemove)){

            int i=0;
            for (Custemer c : listOfAllRecord
            ) {

                if (c.getSinNumber().equals(toRemove)) {
                    Custemer cc=listOfAllRecord.get(i);
                    tbAccountNumber.setText(c.getAccountNum());
                    tbOpenDate.setText(dateFormat.format(cc.getOpenDate()));
                    tbFirstName.setText(cc.getFirstName());
                    tbFamilyName.setText(cc.getFamilyName());
                    tbPhone.setText(cc.getPhoneNumber());
                    tbBalance.setText( cc.getBalance()+"");
                    tbSin.setText(cc.getSinNumber());
                    btnUpdate.setEnabled(true);
                    return;
                }
                i++;
            }

        }else{

            alertDialogBuilder.setTitle("Error:\n")
                    .setMessage("can not find")
                    .setPositiveButton("Yes", this);

            alertDialogBuilder.show();
            toClear();
            return;
        }
    }

    private void toRemove() {
        String toRemove = tbSin.getText().toString();
        if(toRemove.length()==0){
            alertDialogBuilder.setTitle("Error:\n")
                    .setMessage("input first")
                    .setPositiveButton("Yes", this);

            alertDialogBuilder.show();
            return;

        }else if(hasIt(toRemove)){
            int i=0;
            for (Custemer c : listOfAllRecord
            ) {

                if (c.getSinNumber().equals(toRemove)) {
                    listOfAllRecord.remove(i);
                    toClear();
                    return;
                }
                i++;
            }



        }else{

            alertDialogBuilder.setTitle("can not find");
            alertDialogBuilder.setPositiveButton("Yes", this);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private boolean hasIt(String toRemove) {
        for (Custemer c : listOfAllRecord
        ) {
            if (c.getSinNumber().equals(toRemove)) {
                return true;
            }
        }
        return false;

    }

    private void toShowAll() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bundleExtra", listOfAllRecord);
        intent.putExtra("intentExtra",bundle);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void toClear() {
        tbAccountNumber.setText("");
        tbOpenDate.setText("");
        tbFirstName.setText("");
        tbFamilyName.setText("");
        tbPhone.setText("");
        tbBalance.setText("");
        tbSin.setText("");

    }

    private void toAddInDetail()  {
        erroMsg = new ArrayList<>();
        Date openDate = null;
        String strAccuntNumber = tbAccountNumber.getText().toString();
        Custemer c=null;
        if (strAccuntNumber.length() == 0) {
            erroMsg.add("Account Number can not empty");
        } else if (!isUnique(strAccuntNumber,c)) {
            erroMsg.add("Account Number must unique");
        } else {

        }


        
        try{
            openDate=dateFormat.parse(tbOpenDate.getText().toString());
        }
        catch (ParseException e)
            {
                erroMsg.add("open date must input");
            }
        double doubleBalance = 0;

        String strBalance = tbBalance.getText().toString();
        if(!strBalance.equals("")){
            try {
                doubleBalance = Double.parseDouble(strBalance);

            } catch (NumberFormatException e) {
                erroMsg.add("input number in balance");
            }
        }



        String strFirstName = tbFirstName.getText().toString();
        String strLastName = tbFamilyName.getText().toString();
        if(strFirstName.equals("") || strLastName.equals("")){

            erroMsg.add("The name and/or family can not be empty.");
        }

        String phone = tbPhone.getText().toString();
        String strSin = tbSin.getText().toString();
        if(strSin.equals("")) {
            erroMsg.add("The SIN can not be empty.");
        }

        if(strSin.equals("")) {
            erroMsg.add("The SIN can not be empty.");
        }

        if (erroMsg.size() > 0 )
        {
            int i=1;
            String errInfo = "";
            for (String err : erroMsg
            ) {
                errInfo = errInfo+i+": "+ err + "\n" ;
                i++;
            }
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Error:\n");
            alertDialogBuilder.setMessage(errInfo);
            alertDialogBuilder.setPositiveButton("Yes", this);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        } else {
            listOfAllRecord.add(new Custemer(strAccuntNumber, openDate, doubleBalance, strFirstName, strLastName, phone, strSin));
            Toast.makeText(DetailActivity.this, "added", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean isUnique(String strAccuntNumber,Custemer custemer) {

        for (Custemer c : listOfAllRecord) {
            if (c.getAccountNum().equals(strAccuntNumber) && !c.equals(custemer)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE2 && resultCode == RESULT_OK) {

            double receivedName = (Double) data.getDoubleExtra("newB",0);
           tbBalance.setText(receivedName+"");
        }
    }
}