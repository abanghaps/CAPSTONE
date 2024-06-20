package com.dicoding.capstone.saiko.helper

import android.content.Context
import com.dicoding.capstone.saiko.R
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class TFLiteHelper(context: Context) {

    private var interpreter: Interpreter
    private val labels = listOf("Ikan Tongkol", "Ikan Lele", "Ikan Nila", "Sayur Bayam", "Sayur Kangkung")

    init {
        interpreter = Interpreter(loadModelFile(context))
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.resources.openRawResourceFd(R.raw.recommendation_model)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun predict(input: FloatArray): List<Pair<String, Float>> {
        val inputTensor = Array(1) { input }  // Ensure input is 2D array with shape [1, input_length]
        val outputTensor = Array(1) { FloatArray(labels.size) }

        interpreter.run(inputTensor, outputTensor)

        return labels.zip(outputTensor[0].toList()).sortedByDescending { it.second }
    }
}
