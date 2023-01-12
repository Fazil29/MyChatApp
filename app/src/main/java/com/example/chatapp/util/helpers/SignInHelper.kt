package com.example.chatapp.util.helpers

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient

object SignInHelper {

    lateinit var oneTapClient: SignInClient
    lateinit var signInRequest: BeginSignInRequest

    fun prepareClientAndRequest(context: Context, showSavedAccountsOnly: Boolean) {
        oneTapClient = Identity.getSignInClient(context)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("671599120358-ksnr7s5ep61l7m9rlhuo927e6bma152n.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(showSavedAccountsOnly)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}