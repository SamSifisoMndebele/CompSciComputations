package com.compscicomputations.ui.main.dashboard

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.compscicomputations.data.repository.UserRepository
import com.compscicomputations.ui.auth.UserType
import com.google.android.play.core.splitinstall.SplitInstallManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    userRepository: UserRepository,
    val splitInstallManager: SplitInstallManager
) : ViewModel() {

//    private val _firebaseUser = MutableStateFlow<FirebaseUser?>(null)
//    private val firebaseUser = _firebaseUser.asStateFlow()
    private val _userSignedOut = MutableStateFlow(false)
    val userSignedOut = _userSignedOut.asStateFlow()

    private val _userType = MutableStateFlow(UserType.STUDENT)
    private val _displayName = MutableStateFlow("Complete your profile.")
    private val _email = MutableStateFlow("")
    private val _photoUrl = MutableStateFlow<Uri?>(null)


    val userType = _userType.asStateFlow()
    val displayName = _displayName.asStateFlow()
    val email = _email.asStateFlow()
    val photoUrl = _photoUrl.asStateFlow()

    init {
        val isUserSigned = userRepository.isUserSigned()
        _userSignedOut.value = !isUserSigned
        if (isUserSigned) userRepository.getUser()
            .addOnSuccessListener { user ->
                _userType.value = user.userType
                _displayName.value = user.displayName
                _email.value = user.email
                _photoUrl.value = user.photoUrl
            }

        /*splitInstallManager.deferredUninstall(listOf("polish_expressions", "matrix_methods"))
            .addOnSuccessListener {
                Toast.makeText(context, "Done remove polish_expressions", Toast.LENGTH_SHORT).show()
            }*/
    }
}