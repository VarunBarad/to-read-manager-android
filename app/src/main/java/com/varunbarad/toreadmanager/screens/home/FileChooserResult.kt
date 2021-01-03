package com.varunbarad.toreadmanager.screens.home

import java.io.OutputStream

sealed class FileChooserResult {
    object Error : FileChooserResult()
    data class Success(val fileOutputStream: OutputStream): FileChooserResult()
}
