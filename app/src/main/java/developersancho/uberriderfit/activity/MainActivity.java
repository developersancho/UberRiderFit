package developersancho.uberriderfit.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import developersancho.uberriderfit.R;
import developersancho.uberriderfit.common.Common;
import developersancho.uberriderfit.model.Rider;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    RelativeLayout mainLayout;
    Button btnSignIn, btnRegister;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference riders;

    private final static int PERMISSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        //Init Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        riders = db.getReference(Common.user_rider_tbl);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInDialog();
            }
        });
    }


    private void showSignInDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use email to sign in");

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View signin_view = layoutInflater.inflate(R.layout.dialog_signin, null);

        final MaterialEditText edtEmail = signin_view.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = signin_view.findViewById(R.id.edtPassword);

        dialog.setView(signin_view);

        dialog.setPositiveButton("SIGNIN", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                btnSignIn.setEnabled(false);

                // Check validate
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    Snackbar.make(mainLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    Snackbar.make(mainLayout, "Please enter password", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (edtPassword.getText().toString().length() < 6) {
                    Snackbar.make(mainLayout, "Password too short !!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                final SpotsDialog waitingDialog = new SpotsDialog(MainActivity.this);
                waitingDialog.show();

                // login
                auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                waitingDialog.dismiss();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                waitingDialog.dismiss();
                                Snackbar.make(mainLayout, "Failed " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                btnSignIn.setEnabled(true);
                            }
                        });

            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showRegisterDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please use email to register");

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View register_view = layoutInflater.inflate(R.layout.dialog_register, null);

        final MaterialEditText edtEmail = register_view.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = register_view.findViewById(R.id.edtPassword);
        final MaterialEditText edtName = register_view.findViewById(R.id.edtName);
        final MaterialEditText edtPhone = register_view.findViewById(R.id.edtPhone);

        dialog.setView(register_view);

        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                // Check validate
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    Snackbar.make(mainLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    Snackbar.make(mainLayout, "Please enter password", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    Snackbar.make(mainLayout, "Please enter phone number", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (edtPassword.getText().toString().length() < 6) {
                    Snackbar.make(mainLayout, "Password too short !!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtName.getText().toString())) {
                    Snackbar.make(mainLayout, "Please enter name", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                // register new user
                auth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // save db
                                Rider rider = new Rider();
                                rider.setEmail(edtEmail.getText().toString());
                                rider.setName(edtName.getText().toString());
                                rider.setPhone(edtPhone.getText().toString());
                                rider.setPassword(edtPassword.getText().toString());

                                // set user
                                riders.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(rider)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(mainLayout, "Register Successed!!!", Snackbar.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(mainLayout, "Failed " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                            }
                                        });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(mainLayout, "Failed " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
