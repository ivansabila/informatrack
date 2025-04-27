package com.example.informatrackta.view.activity.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.informatrackta.R;
import com.example.informatrackta.view.activity.dashboard.admin.DashboardAdminActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText etUserNim, etPassword;
    private String userNim, password, uid, role, student_id, email;
    private Boolean isApproved, isLoginSuccess;
    private Button btnLogin;
    private TextView tvRegister;
    private ImageView ivTogglePassword;
    private boolean isPasswordVisible = false;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();
        actionComponents();
        showHidePassword();
        eventOnFocus();
    }

    @Override
    public void finish() {
        super.finish();
        // mengatur transisi ketika lifecycle finish (ketika click tombol kembali dari sistem)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initComponents() {
        // inisialisasi semua component yang ada pada file xml
        etUserNim = findViewById(R.id.etUserNim);
        etPassword = findViewById(R.id.etPassword);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // inisialisasi database firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    private void actionComponents() {
        // event click ke registerActivity
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // event click untuk validasi data ke database
        btnLogin.setOnClickListener(view -> {
            userNim = etUserNim.getText().toString().trim().toLowerCase();
            password = etPassword.getText().toString().trim().toLowerCase();

            // validasi semua data yang di-inputkan
            if (validateInputs()) { return; }

            // retrieve data
            retrieveAuth();

        });
    }

    private void showHidePassword() {
        ivTogglePassword.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable._eye_close);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable._eye);
            }

            // meletakkan cursor pada karakter terakhir
            etPassword.setSelection(etPassword.length());

            // mengatur kembali huruf dan ukuran font
            etPassword.setTypeface(ResourcesCompat.getFont(this, R.font.helvetica));
            etPassword.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        });
    }

    // menyembunyikan keyboard ketika ada touch di luar editText dan button
    private void eventOnFocus() {
        etUserNim.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                };
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                };
            }
        });

        btnLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                };
            }
        });
    }

    private boolean validateInputs() {
        // validasi data kosong
        if (userNim.isEmpty()) {
            etUserNim.setError("NIM atau Email harus diisi");
            etUserNim.requestFocus();
            return true;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password harus diisi");
            etPassword.requestFocus();
            return true;
        }

        // validasi nim atau email
        if (userNim.contains("@")) {
            String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            if (!userNim.matches(regexEmail)) {
                etUserNim.setError("Email tidak valid");
                etUserNim.requestFocus();
                return true;
            }
        } else {
            String regexStudentId = "^[0-9]+$";
            if (userNim.length() < 8 || userNim.length() > 9 && !userNim.matches(regexStudentId)) {
                etUserNim.setError("NIM tidak valid");
                etUserNim.requestFocus();
                return true;
            }
        }

        // validasi password
        if (password.length() < 6) {
            etPassword.setError("Password minimal 6 karakter");
            etPassword.requestFocus();
            return true;
        }

        // jika semua validasi aman, kirim false
        return false;
    }

    private void retrieveAuth() {
        // cek apakah data yang diinputkan adalah nama atau email
        if (userNim.contains("@")) {
            loginWithEmail(userNim, password);
        } else {
            loginWithNim();
        }
    }

    // custom toast (dengan gambar)
    private void toast(String message) {
        // ambil layout toast-nya
        View layout = LayoutInflater.from(this).inflate(R.layout.toast, null);

        // inisialisasi dan buat pesannya
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        // instansiasi toast dan atur attribute-nya
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private void loginWithEmail(String userNim, String password) {
        // validasi email dan password di firebaseAuth
        firebaseAuth.signInWithEmailAndPassword(userNim, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // ambil Uid user
                        uid = firebaseAuth.getCurrentUser().getUid();

                        // retrieve data dengan document uid
                        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // cek keberadaan data di database
                                if (snapshot.exists()) {
                                    // ambil data isApproved dan role dari database
                                    isApproved = snapshot.child("isApproved").getValue(Boolean.class);
                                    role = snapshot.child("role").getValue(String.class);

                                    // validasi isApproved user
                                    if (isApproved == null || !isApproved) {
                                        firebaseAuth.signOut();
                                        toast("Akun anda belum disetujui oleh TU Program Studi");
                                        return;
                                    }

                                    // validasi role user
                                    switch (role) {
                                        case "admin":
                                            startActivity(new Intent(LoginActivity.this, DashboardAdminActivity.class));
                                            break;
                                        case "dosen":
                                            // startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            break;
                                        case "mahasiswa":
                                            // startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("GAGAL MENGAMBIL DATA", error.getMessage());
                            }
                        });
                    } else {
                        Log.e("GAGAL MASUK KE DATA", task.getException().getMessage());
                    }
                });
    }

    private void loginWithNim() {
        // cari data nim di database yang sama dengan yang diinput oleh user
        databaseReference.orderByChild("student_id").equalTo(userNim)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // cek keberadaan data di database
                        if (snapshot.exists()) {
                            // cek datanya satu per satu untuk antisipasi kesamaan nama
                            for (DataSnapshot child : snapshot.getChildren()) {
                                // ambil data student_id-nya
                                email = child.child("email").getValue(String.class);

                                loginWithEmail(email, password);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("GAGAL MENCARI NIM", error.getMessage());
                    }
                });
    }
}