package com.varunbarad.toreadmanager.screens.home

import java.io.InputStream
import java.io.OutputStream

sealed class ExportFileChooserResult {
    object Error : ExportFileChooserResult()
    data class Success(val fileOutputStream: OutputStream): ExportFileChooserResult()
}

sealed class ImportFileChooserResult {
    object Error : ImportFileChooserResult()
    data class Success(val fileInputStream: InputStream): ImportFileChooserResult()
}
