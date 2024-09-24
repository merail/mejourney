package merail.life.auth.impl.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.parse.ParseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
import merail.life.auth.api.IAuthRepository
import merail.life.core.MergeStrategy
import merail.life.core.RequestResponseMergeStrategy
import merail.life.core.RequestResult
import merail.life.core.extensions.flowWithResult
import merail.life.core.toRequestResult
import javax.inject.Inject

internal class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : IAuthRepository {

    companion object {
        const val TAG = "AuthRepository"
    }

    override fun checkUser(): Flow<RequestResult<Boolean>> {
        val mergeStrategy: MergeStrategy<RequestResult<Boolean>> = RequestResponseMergeStrategy()
        val cachedAllArticles: Flow<RequestResult<Boolean>> = checkUserInFirebase()
        val remoteArticles: Flow<RequestResult<Boolean>> = checkUserInEmailParser()
        return cachedAllArticles.combine(remoteArticles, mergeStrategy::merge)
    }

    override fun createUser(
        email: String,
        password: String,
    ): Flow<RequestResult<Unit>> {
        val result = flowWithResult {
            val user = ParseUser()
            user.username = email
            user.email = email
            user.setPassword(password)
            user.signUp()
        }.onEach {
            if (it.isSuccess) {
                Log.d(TAG, "User creating. Success")
            } else {
                Log.w(TAG, "User creating. Failure", it.exceptionOrNull())
            }
        }.map(Result<Unit>::toRequestResult)
        val start = flowOf<RequestResult<Unit>>(RequestResult.InProgress())
        return merge(result, start)
    }

    private fun checkUserInFirebase(): Flow<RequestResult<Boolean>> {
        if (firebaseAuth.currentUser == null) {
            val result = flowWithResult {
                firebaseAuth.signInAnonymously().await()
                true
            }.flowOn(
                context = Dispatchers.IO,
            ).onEach {
                if (it.isSuccess) {
                    Log.d(TAG, "Firebase anonymous auth. Success ${firebaseAuth.currentUser?.uid}")
                } else {
                    Log.w(TAG, "Firebase anonymous auth. Failure", it.exceptionOrNull())
                }
            }.map(Result<Boolean>::toRequestResult)
            val start = flowOf<RequestResult<Boolean>>(RequestResult.InProgress())
            return merge(result, start)
        }
        return flowOf<RequestResult<Boolean>>(RequestResult.Success(true))
    }

    private fun checkUserInEmailParser() = flowWithResult {
        ParseUser.getCurrentUser() != null
    }.onEach {
        if (it.isSuccess) {
            Log.d(TAG, "Checking user exist. Success ${it.getOrNull()}")
        } else {
            Log.w(TAG, "Checking user exist. Failure", it.exceptionOrNull())
        }
    }.map(Result<Boolean>::toRequestResult)
}