package com.dominate.thirtySecondsChallenge.utils

import java.io.IOException

class ExceptionApi(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)