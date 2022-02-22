package com.hunterbennett.niknak.niknakserver.firebase;

import java.io.FileInputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class FirebaseAdmin {
    private static final String PROJECT_ID = "niknak-e2ea4";
    // private static final String STORAGE_BUCKET = "niknak-e2ea4.appspot.com";

    private Firestore db;

    public FirebaseAdmin() throws Exception {
        FileInputStream serviceAccount = new FileInputStream("./src/main/java/com/hunterbennett/niknak/niknakserver/firebase/service-account.json");
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setProjectId(PROJECT_ID)
            .build();
        FirebaseApp.initializeApp(firebaseOptions);
        this.db = FirestoreClient.getFirestore();
    }
}
