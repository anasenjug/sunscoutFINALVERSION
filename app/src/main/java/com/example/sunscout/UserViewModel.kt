import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

enum class SkinType {
    Light, Medium, Dark
}

class UserViewModel : ViewModel() {
    private val _skinType = mutableStateOf<SkinType?>(null)
    val skinType: State<SkinType?> = _skinType

    private val _successMessage = mutableStateOf<String?>(null)
    val successMessage: State<String?> = _successMessage

    init {
        getSkinTypeFromFirestore()
    }

    // Modify to accept null skinType
    fun updateSkinTypeInFirestore(skinType: SkinType?) {
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val userId = auth.currentUser?.uid ?: return

        val userDoc = firestore.collection("users").document(userId)

        // Update Firestore with nullable skin type
        userDoc.update("skinType", skinType?.name) // Pass null if skinType is null
            .addOnSuccessListener {
                _skinType.value = skinType
                _successMessage.value = skinType?.let { "Success! Your skin type is: ${it.name}" }
                    ?: "Skin type cleared."
            }
            .addOnFailureListener { e ->
                _successMessage.value = "Failed to update skin type."
            }
    }

    private fun getSkinTypeFromFirestore() {
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val userId = auth.currentUser?.uid ?: return

        val userDoc = firestore.collection("users").document(userId)
        userDoc.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val skinTypeStr = document.getString("skinType")
                    // Check for null skinTypeStr and assign accordingly
                    _skinType.value = if (skinTypeStr != null) {
                        SkinType.values().find { it.name == skinTypeStr }
                    } else {
                        null // Handle case where skinTypeStr is null
                    }
                } else {
                    _skinType.value = null // Handle case where document does not exist
                }
            }
            .addOnFailureListener { e ->
                // Handle the error, possibly set skinType to null
                _skinType.value = null
            }
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }
}
