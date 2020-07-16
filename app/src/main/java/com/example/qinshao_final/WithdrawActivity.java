package com.example.qinshao_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class WithdrawActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<Custemer> listOfAllRecord;
    TextView lblAmount;
    EditText tbAmount;
    Button buttonWithDrawer;
    AlertDialog.Builder alertDialogBuilder;
    Custemer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        initialize();
    }

    private void initialize() {

        lblAmount = findViewById(R.id.lblAmount);
        tbAmount = findViewById(R.id.tbAmount);
        buttonWithDrawer = findViewById(R.id.btnWithdraw);
        buttonWithDrawer.setOnClickListener(this);
        alertDialogBuilder = new AlertDialog.Builder(this);

        Bundle bundle = getIntent().getBundleExtra("intentExtra");
        Serializable bundleCustomer = bundle.getSerializable("bundleCurrentObj");
        Serializable bundleRecords = bundle.getSerializable("bundleExtra");
        customer = (Custemer)bundleCustomer;
        listOfAllRecord = (ArrayList<Custemer>) bundleRecords;
        double balance= customer.getBalance();
        lblAmount.setText(balance+"");


    }

    @Override
    public void onClick(View v) {
        String strAmount=tbAmount.getText().toString();
        if(strAmount.equals("")){
            Toast.makeText(WithdrawActivity.this,"input the amount of withdrawal.",Toast.LENGTH_SHORT).show();
            return;
        }


        double amount = 0;
        try{
            amount=Double.parseDouble(strAmount);
            if(amount==0){
                alertDialogBuilder.setTitle("Error:\n")
                        .setMessage("you have no money to withdrawal")
                        .setPositiveButton("Yes", (DialogInterface.OnClickListener) this);
                alertDialogBuilder.show();
                return;
            }


        } catch (NumberFormatException e) {
            alertDialogBuilder.setTitle("Error:\n")
                    .setMessage("input number.")
                    .setPositiveButton("Yes", (DialogInterface.OnClickListener) this);
            alertDialogBuilder.show();
            return;
        }

        double newBalance = customer.getBalance()-amount;
        if(newBalance<0) {
            Toast.makeText(WithdrawActivity.this,"not enough money.",Toast.LENGTH_LONG).show();
            return;
        }
        customer.setBalance(newBalance);
        lblAmount.setText(customer.getBalance()+"");
        tbAmount.setText("0.0");
        for (Custemer c : listOfAllRecord){
            if(c.getSinNumber().equals(customer.getSinNumber())){
                c.setBalance(customer.getBalance());
                break;
            }
        }
        Toast.makeText(WithdrawActivity.this,"The balance is updated successfully.",Toast.LENGTH_LONG).show();




        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bundleExtra", listOfAllRecord);
        intent.putExtra("return_result",bundle);
        setResult(RESULT_OK,intent);
        finish();
    }
}