package com.example.projetdevmobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.button.MaterialButton;

public class ActiviteConnexion extends AppCompatActivity {
    private static final String TAG = "EmailPassword";

    private FirebaseAuth auth;
    private TextInputEditText editionEmail, editionMotDePasse;
    private TextInputLayout texteEmail, texteMotDePasse;
    private MaterialButton boutonConnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activite_connexion);

        auth = FirebaseAuth.getInstance();

        editionEmail = findViewById(R.id.editionEmail);
        editionMotDePasse = findViewById(R.id.editionMotDePasse);
        texteEmail = findViewById(R.id.texteEmail);
        texteMotDePasse = findViewById(R.id.texteMotDePasse);
        boutonConnexion = findViewById(R.id.boutonConnexion);

        boutonConnexion.setOnClickListener(v -> seConnecter());
    }

    private void seConnecter() {
        String email = editionEmail.getText().toString().trim();
        String motDePasse = editionMotDePasse.getText().toString().trim();

        if (email.isEmpty()) {
            texteEmail.setError("Email requis");
            return;
        }
        if (motDePasse.isEmpty()) {
            texteMotDePasse.setError("Mot de passe requis");
            return;
        }

        auth.signInWithEmailAndPassword(email, motDePasse)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser utilisateur = auth.getCurrentUser();
                        Toast.makeText(ActiviteConnexion.this, "Authentification réussie", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActiviteConnexion.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ActiviteConnexion.this, "Échec de l'authentification: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser utilisateurActuel = auth.getCurrentUser();
        if (utilisateurActuel != null) {
            startActivity(new Intent(ActiviteConnexion.this, MainActivity.class));
            finish();
        }
    }
}
