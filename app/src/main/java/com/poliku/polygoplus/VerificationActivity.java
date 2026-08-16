package com.poliku.polygoplus;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.poliku.polygoplus.data.AppDataStore;
public class VerificationActivity extends AppCompatActivity { @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_verification);findViewById(R.id.btnBack).setOnClickListener(v->finish());TextView status=findViewById(R.id.tvVerificationStatus);MaterialButton action=findViewById(R.id.btnVerify);if(AppDataStore.isVerified(this)){status.setText("Your account is verified.");action.setVisibility(android.view.View.GONE);findViewById(R.id.etVerificationId).setVisibility(android.view.View.GONE);findViewById(R.id.etVerificationEmail).setVisibility(android.view.View.GONE);}action.setOnClickListener(v->{String id=((EditText)findViewById(R.id.etVerificationId)).getText().toString().trim();String email=((EditText)findViewById(R.id.etVerificationEmail)).getText().toString().trim();if(id.isEmpty()||email.isEmpty()){Toast.makeText(this,"Enter both verification details",Toast.LENGTH_SHORT).show();return;}if(!AppDataStore.verifyAccount(this,id,email)){Toast.makeText(this,"Details do not match your account",Toast.LENGTH_SHORT).show();return;}status.setText("Your account is verified.");v.setVisibility(android.view.View.GONE);Toast.makeText(this,"Verification complete",Toast.LENGTH_SHORT).show();});}}
