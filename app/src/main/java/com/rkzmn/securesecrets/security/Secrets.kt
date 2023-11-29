package com.rkzmn.securesecrets.security

object Secrets {

    init {
        System.loadLibrary("secrets")
    }

    external fun getAPIKey(): String

}