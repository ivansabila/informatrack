package com.example.informatrackta.view.activity.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
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
import com.example.informatrackta.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etStudentId, etEmail, etPassword;
    private String uid, name, student_id, email, password, createdAt;
    private Button btnRegister;
    private TextView tvLogin;
    private ImageView ivTogglePassword;
    private boolean isPasswordVisible = false;
    private UserModel userModel;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        etName = findViewById(R.id.etName);
        etStudentId = findViewById(R.id.etStudentId);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // inisialisasi database firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    private void actionComponents() {
        // event click ke loginActivity
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // event click untuk store data ke database
        btnRegister.setOnClickListener(view -> {
            // ambil semua data dan convert ke string
            name = etName.getText().toString().trim().toLowerCase();
            student_id = etStudentId.getText().toString().trim().toLowerCase();
            email = etEmail.getText().toString().trim().toLowerCase();
            password = etPassword.getText().toString().trim().toLowerCase();

            // validasi semua data yang di-inputkan
            if (validateInputs()) { return; }


            databaseReference.orderByChild("student_id").equalTo(student_id)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // cek keberadaan data di database
                            if (snapshot.exists()) {
                                toast("NIM yang anda masukkan sudah ada");
                            } else {
                                // store data dengan firebaseAuth (terhubung dengan email dan password)
                                firebaseAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                storeToDatabase();
                                            } else {
                                                toast("Email yang anda masukkan sudah ada");
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("GAGAL MENCARI NIM", error.getMessage());
                        }
                    });
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
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                };
            }
        });

        etStudentId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                };
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        btnRegister.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                };
            }
        });
    }

    private void storeToDatabase() {
        // ambil data tanggal dan waktu lalu convert ke string sebagai timestamp createdAt
        LocalDateTime localDateTime = LocalDateTime.now();
        createdAt = String.valueOf(localDateTime);

        // membuat Uid untuk user
        uid = firebaseAuth.getCurrentUser().getUid();

        // inisialisasi UserModel dengan data yang akan di-store
        userModel = new UserModel(name, student_id, email, "mahasiswa", createdAt, false);

        // store data dengan document uid dan data dari UserModel yang telah diinisialisasi sebelumnya
        databaseReference.child(uid).setValue(userModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                toast("Daftar berhasil, menunggu persetujuan TU Program Studi");

            } else {
                Log.e("GAGAL SIMPAN DATA", task.getException().getMessage());
            }
        });
    }

    private boolean validateInputs() {
        // validasi data kosong
        if (name.isEmpty()) {
            etName.setError("Nama harus diisi");
            etName.requestFocus();
            return true;
        }

        if (student_id.isEmpty()) {
            etStudentId.setError("NIM harus diisi");
            etStudentId.requestFocus();
            return true;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email harus diisi");
            etEmail.requestFocus();
            return true;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password harus diisi");
            etPassword.requestFocus();
            return true;
        }

        // validasi nama
        String regexName = "^[a-zA-Z. ]+$";
        if (!name.matches(regexName)) {
            etName.setError("Nama tidak valid");
            etName.requestFocus();
            return true;
        }

        // validasi nim
        if (student_id.length() < 8 || student_id.length() > 9) {
            etStudentId.setError("NIM tidak valid");
            etStudentId.requestFocus();
            return true;
        }

        // validasi email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email tidak valid");
            etEmail.requestFocus();
            return true;
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
}