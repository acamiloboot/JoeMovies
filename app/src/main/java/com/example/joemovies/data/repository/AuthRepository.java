package com.example.joemovies.data.repository;

import com.example.joemovies.data.FirebaseService;
import com.example.joemovies.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthRepository {
    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    public AuthRepository() {
        this.auth = FirebaseService.getAuth();
        this.db = FirebaseService.getFirestore();
    }

    public interface AuthCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public interface RoleCallback {
        void onRoleFetched(String role);
        void onError(String error);
    }

    public void loginByUsername(String username, String password, AuthCallback callback) {
        db.collection("usernames").document(username).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String email = documentSnapshot.getString("email");
                        if (email != null) {
                            auth.signInWithEmailAndPassword(email, password)
                                    .addOnSuccessListener(authResult -> callback.onSuccess("Login exitoso"))
                                    .addOnFailureListener(e -> callback.onError("Contraseña incorrecta"));
                        } else {
                            callback.onError("Error al recuperar el correo asociado");
                        }
                    } else {
                        callback.onError("El nombre de usuario no existe");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void register(String username, String email, String password, String name, AuthCallback callback) {
        // 1. Verificar si el nombre de usuario ya está tomado
        db.collection("usernames").document(username).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        callback.onError("El nombre de usuario ya está en uso");
                    } else {
                        // 2. Crear el usuario en Auth
                        performRegistration(username, email, password, name, callback);
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    private void performRegistration(String username, String email, String password, String name, AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    // Rol forzado a USER por seguridad
                    User newUser = new User(uid, name, username, email, "USER");
                    
                    // Guardar en ambas colecciones
                    saveUserToFirestore(newUser, callback);
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    private void saveUserToFirestore(User user, AuthCallback callback) {
        // Guardar perfil de usuario
        db.collection("users").document(user.getUid()).set(user)
                .addOnSuccessListener(aVoid -> {
                    // Guardar mapeo de nombre de usuario
                    Map<String, String> usernameMap = new HashMap<>();
                    usernameMap.put("email", user.getEmail());
                    usernameMap.put("uid", user.getUid());
                    
                    db.collection("usernames").document(user.getUsername()).set(usernameMap)
                            .addOnSuccessListener(aVoid2 -> callback.onSuccess("Registro exitoso"))
                            .addOnFailureListener(e -> callback.onError("Error al mapear usuario: " + e.getMessage()));
                })
                .addOnFailureListener(e -> callback.onError("Error al guardar perfil: " + e.getMessage()));
    }

    public void getUserRole(String uid, RoleCallback callback) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                        callback.onRoleFetched(role);
                    } else {
                        callback.onError("Usuario no encontrado en la base de datos");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
}
