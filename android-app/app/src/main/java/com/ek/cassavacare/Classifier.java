/*
 * Project: CassavaCare
 * File: Classifier.java
 * Description: TensorFlow Lite image classifier for cassava leaf disease detection.
 *              Based on the open-source CassavaCare project by Emmanuel Kirui Barkacha (MIT License).
 */

package com.ek.cassavacare;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Classifier {

    public static final String NOT_LEAF_MESSAGE =
            "No cassava leaf found. Please photograph a cassava leaf in good light.";
    public static final String UNCERTAIN_MESSAGE =
            "The model is uncertain about this image. Please ensure the leaf is clearly visible and well-lit, then try again.";

    private static final String TAG = "Classifier";
    private static final String MODEL_FILE = "cassava_model.tflite";
    private static final String NOT_LEAF_LABEL = "Not a Cassava Leaf";
    private static final float CONFIDENCE_THRESHOLD = 0.75f;

    // Must match the order in labels.txt from the Teachable Machine export.
    private final String[] labels = {
            "Cassava Bacterial Blight",
            "Cassava Brown Streak Disease",
            "Cassava Green Mottle",
            "Cassava Mosaic Disease",
            "Healthy",
            NOT_LEAF_LABEL
    };

    private Interpreter tflite;
    private final int inputSize;

    public Classifier(Context context) throws IOException {
        tflite = new Interpreter(FileUtil.loadMappedFile(context, MODEL_FILE));
        int[] shape = tflite.getInputTensor(0).shape(); // expected [1, size, size, 3]
        inputSize = shape[1];
        Log.d(TAG, "Model input shape: " + Arrays.toString(shape));
    }

    public String classify(Bitmap bitmap) {
        if (bitmap == null) {
            throw new IllegalArgumentException("Invalid input image");
        }

        float[] probabilities = getProbabilities(bitmap);
        Log.d(TAG, "Probabilities: " + Arrays.toString(probabilities));

        int best = 0;
        for (int i = 1; i < probabilities.length; i++) {
            if (probabilities[i] > probabilities[best]) {
                best = i;
            }
        }
        float confidence = probabilities[best];

        if (labels[best].equals(NOT_LEAF_LABEL)) {
            return NOT_LEAF_MESSAGE;
        }
        if (confidence < CONFIDENCE_THRESHOLD) {
            Log.w(TAG, "Low confidence: " + (confidence * 100) + "%");
            return UNCERTAIN_MESSAGE;
        }
        return String.format("%s - %.0f%% sure", labels[best], confidence * 100);
    }

    public float[] getProbabilities(Bitmap bitmap) {
        ByteBuffer input = toInputBuffer(bitmap);
        float[][] output = new float[1][labels.length];
        tflite.run(input, output);
        return toProbabilities(output[0]);
    }

    // Uses the model's outputs as they are if they already sum to 1, otherwise applies softmax.
    private float[] toProbabilities(float[] values) {
        float sum = 0f;
        boolean nonNegative = true;
        for (float v : values) {
            sum += v;
            if (v < 0f) nonNegative = false;
        }
        if (nonNegative && Math.abs(sum - 1f) < 0.01f) {
            return values;
        }
        return softmax(values);
    }

    private float[] softmax(float[] logits) {
        float maxLogit = logits[0];
        for (float logit : logits) {
            if (logit > maxLogit) maxLogit = logit;
        }
        float sum = 0f;
        float[] result = new float[logits.length];
        for (int i = 0; i < logits.length; i++) {
            result[i] = (float) Math.exp(logits[i] - maxLogit);
            sum += result[i];
        }
        for (int i = 0; i < result.length; i++) {
            result[i] /= sum;
        }
        return result;
    }

    // Scales the photo to the model's input size and maps pixels to -1..1,
    // the range Teachable Machine image models expect.
    private ByteBuffer toInputBuffer(Bitmap bitmap) {
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3);
        buffer.order(ByteOrder.nativeOrder());

        int[] pixels = new int[inputSize * inputSize];
        scaled.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize);
        for (int pixel : pixels) {
            buffer.putFloat(((pixel >> 16) & 0xFF) / 127.5f - 1f);
            buffer.putFloat(((pixel >> 8) & 0xFF) / 127.5f - 1f);
            buffer.putFloat((pixel & 0xFF) / 127.5f - 1f);
        }
        buffer.rewind();
        return buffer;
    }

    public void close() {
        if (tflite != null) {
            tflite.close();
            tflite = null;
        }
    }
}
