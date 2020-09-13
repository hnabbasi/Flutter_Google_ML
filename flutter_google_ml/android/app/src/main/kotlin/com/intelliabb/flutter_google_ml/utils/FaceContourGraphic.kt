package com.google.codelab.mlkit

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark
import com.intelliabb.flutter_google_ml.utils.GraphicOverlay

/** Graphic instance for rendering face contours graphic overlay view.  */
class FaceContourGraphic(overlay: GraphicOverlay?) : GraphicOverlay.Graphic(overlay!!) {
    private val facePositionPaint: Paint
    private val idPaint: Paint
    private val boxPaint: Paint

    @Volatile
    private var face: Face? = null

    /**
     * Updates the face instance from the detection of the most recent frame. Invalidates the relevant
     * portions of the overlay to trigger a redraw.
     */
    fun updateFace(face: Face?) {
        this.face = face
        postInvalidate()
    }

    /** Draws the face annotations for position on the supplied canvas.  */
    override fun draw(canvas: Canvas?) {
        val face = face ?: return

        // Draws a circle at the position of the detected face, with the face's track id below.
        val x: Float = translateX(face.boundingBox.centerX().toFloat())
        val y: Float = translateY(face.boundingBox.centerY().toFloat())
        canvas!!.drawCircle(x, y, FACE_POSITION_RADIUS, facePositionPaint)
//        canvas!!.drawText("id: " + face.trackingId, x + ID_X_OFFSET, y + ID_Y_OFFSET, idPaint)

        // Draws a bounding box around the face.
        val xOffset: Float = scaleX(face.boundingBox.width() / 2.0f)
        val yOffset: Float = scaleY(face.boundingBox.height() / 2.0f)
        val left = x - xOffset
        val top = y - yOffset
        val right = x + xOffset
        val bottom = y + yOffset
        canvas.drawRect(left, top, right, bottom, boxPaint)
        val contour = face.allContours
        for (faceContour in contour) {
            for (point in faceContour.points) {
                val px: Float = translateX(point.x)
                val py: Float = translateY(point.y)
                canvas.drawCircle(px, py, FACE_POSITION_RADIUS, facePositionPaint)
            }
        }
        if (face.smilingProbability != null) {
            canvas.drawText(
                    "Happiness: " + String.format("%.2f", face.smilingProbability),
                    x + ID_X_OFFSET * 3,
                    y - ID_Y_OFFSET,
                    idPaint)
        }
        if (face.rightEyeOpenProbability != null) {
            canvas.drawText(
                    "Right eye: " + String.format("%.2f", face.rightEyeOpenProbability),
                    x - ID_X_OFFSET,
                    y,
                    idPaint)
        }
        if (face.leftEyeOpenProbability != null) {
            canvas.drawText(
                    "Left eye: " + String.format("%.2f", face.leftEyeOpenProbability),
                    x + ID_X_OFFSET * 6,
                    y,
                    idPaint)
        }
        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)
        if (leftEye != null) {
            canvas.drawCircle(
                    translateX(leftEye.position.x),
                    translateY(leftEye.position.y),
                    FACE_POSITION_RADIUS,
                    facePositionPaint)
        }
        val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)
        if (rightEye != null) {
            canvas.drawCircle(
                    translateX(rightEye.position.x),
                    translateY(rightEye.position.y),
                    FACE_POSITION_RADIUS,
                    facePositionPaint)
        }
        val leftCheek = face.getLandmark(FaceLandmark.LEFT_CHEEK)
        if (leftCheek != null) {
            canvas.drawCircle(
                    translateX(leftCheek.position.x),
                    translateY(leftCheek.position.y),
                    FACE_POSITION_RADIUS,
                    facePositionPaint)
        }
        val rightCheek = face.getLandmark(FaceLandmark.RIGHT_CHEEK)
        if (rightCheek != null) {
            canvas.drawCircle(
                    translateX(rightCheek.position.x),
                    translateY(rightCheek.position.y),
                    FACE_POSITION_RADIUS,
                    facePositionPaint)
        }
    }

    companion object {
        private const val FACE_POSITION_RADIUS = 10.0f
        private const val ID_TEXT_SIZE = 70.0f
        private const val ID_Y_OFFSET = 80.0f
        private const val ID_X_OFFSET = -70.0f
        private const val BOX_STROKE_WIDTH = 10.0f
        private val COLOR_CHOICES = intArrayOf(
                Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.RED, Color.WHITE, Color.YELLOW
        )
        private var currentColorIndex = 0
    }

    init {
        currentColorIndex = (currentColorIndex + 1) % COLOR_CHOICES.size
        val selectedColor = COLOR_CHOICES[currentColorIndex]
        facePositionPaint = Paint()
        facePositionPaint.color = selectedColor
        idPaint = Paint()
        idPaint.color = selectedColor
        idPaint.textSize = ID_TEXT_SIZE
        boxPaint = Paint()
        boxPaint.color = selectedColor
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = BOX_STROKE_WIDTH
    }
}