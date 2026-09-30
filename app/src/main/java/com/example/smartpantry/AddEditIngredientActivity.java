package com.example.smartpantry;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartpantry.database.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;

public class AddEditIngredientActivity extends AppCompatActivity {
    private DatabaseHelper db; private EditText name, quantity, unit, expiry; private int itemId=-1;
    @Override protected void onCreate(Bundle b) { super.onCreate(b); setContentView(R.layout.activity_add_edit_ingredient); db=new DatabaseHelper(this); name=findViewById(R.id.edtName); quantity=findViewById(R.id.edtQuantity); unit=findViewById(R.id.edtUnit); expiry=findViewById(R.id.edtExpiry); TextView title=findViewById(R.id.txtTitle); itemId=getIntent().getIntExtra("ITEM_ID",-1); if(itemId!=-1){ title.setText("Edit Ingredient"); PantryItem item=db.getPantryItem(itemId); if(item!=null){name.setText(item.getName());quantity.setText(String.valueOf(item.getQuantity()));unit.setText(item.getUnit());expiry.setText(item.getExpiryDate());} } Button save=findViewById(R.id.btnSave); save.setOnClickListener(v->save()); }
    private void save(){ String n=name.getText().toString().trim(), q=quantity.getText().toString().trim(), u=unit.getText().toString().trim(), e=expiry.getText().toString().trim(); if(TextUtils.isEmpty(n)||TextUtils.isEmpty(q)||TextUtils.isEmpty(u)){Toast.makeText(this,"Name, quantity and unit are required",Toast.LENGTH_SHORT).show();return;} double qty; try{qty=Double.parseDouble(q); if(qty<=0) throw new NumberFormatException();}catch(NumberFormatException ex){quantity.setError("Enter a quantity greater than 0");return;} if(itemId==-1) db.insertPantryItem(n,qty,u,e); else db.updatePantryItem(itemId,n,qty,u,e); Toast.makeText(this,"Ingredient saved",Toast.LENGTH_SHORT).show(); finish(); }
}
