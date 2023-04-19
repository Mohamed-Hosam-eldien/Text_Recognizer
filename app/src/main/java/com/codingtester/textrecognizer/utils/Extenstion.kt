package com.codingtester.textrecognizer.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

// this function to wait call with firebase until completed

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Task<T>.await(): T {
    return suspendCancellableCoroutine { cont->
        addOnCompleteListener{
            if(it.exception != null) {
                cont.resumeWithException(exception!!)
            } else {
                cont.resume(it.result, null)
            }
        }
    }
}