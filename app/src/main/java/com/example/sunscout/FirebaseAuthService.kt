package com.example.sunscout

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class FirebaseAuthService(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) {

    suspend fun register(email: String, password: String) {
        return suspendCancellableCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        userId?.let {
                            createUserDocument(it)
                        }
                        continuation.resume(Unit) { }
                    } else {
                        continuation.resumeWithException(task.exception ?: Exception("Registration failed"))
                    }
                }
        }
    }
    private fun createUserDocument(userId: String) {
        val userDoc = firestore.collection("users").document(userId)
        userDoc.set(mapOf("skinType" to "")) // Set default skinType or other initial data
            .addOnSuccessListener {
                // Handle success if needed
            }
            .addOnFailureListener { e ->
                // Handle failure if needed
            }
    }

    suspend fun login(email: String, password: String) {
        return suspendCancellableCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Unit) { }
                    } else {
                        continuation.resumeWithException(task.exception ?: Exception("Login failed"))
                    }
                }
        }
    }


}
