package com.github.akighan.tetris

import android.graphics.Point
import com.github.akighan.tetris.constants.CellConstants
import com.github.akighan.tetris.constants.FieldConstants
import com.github.akighan.tetris.helper.array2dOfByte
import com.github.akighan.tetris.models.Block
import com.github.akighan.tetris.storage.AppPreferences


class AppModel {
    var score: Int = 0
    private var preferences: AppPreferences? = null

    var currentBlock: Block? = null
    var currentState: String = Statuses.AWAITING_START.name

    private var field: Array<ByteArray> = array2dOfByte(FieldConstants.ROW_COUNT.value, FieldConstants.COLUMN_COUNT.value)

    fun setPreferences(preferences: AppPreferences?) {
        this.preferences = preferences
    }

    fun getCellStatus(row: Int, column: Int): Byte? {
        return field[row][column]
    }

    private fun setCellStatus(row: Int, column: Int, status: Byte?) {
        if (status != null) {
            field[row][column] = status
        }
    }

    fun isGameOver(): Boolean {
        return currentState == Statuses.OVER.name
    }

    fun isGameActive(): Boolean {
        return currentState == Statuses.ACTIVE.name
    }

    fun isGameAwaitingStart(): Boolean {
        return currentState == Statuses.AWAITING_START.name
    }

    private fun boostScore() {
        score += 10
        if (score > preferences?.getHighScore() as Int)
            preferences?.saveHighScore(score)
    }

    private fun generateNextBlock() {
        currentBlock = Block.createBlock()
    }

    enum class Statuses {
        AWAITING_START, ACTIVE, INACTIVE, OVER
    }

    enum class Motions {
        LEFT, RIGHT, DOWN, ROTATE
    }

    private fun moveValid(position: Point, frameNumber: Int?):Boolean {
        val shape:Array<ByteArray>? = currentBlock?.getShape(frameNumber as Int)
        return validTranslation(position, shape as Array<ByteArray>)
    }

    private fun validTranslation(position: Point, shape: Array<ByteArray>): Boolean {
        return if (position.y < 0 || position.x < 0) {
            false
        } else if (position.y + shape.size > FieldConstants.ROW_COUNT.value) {
            false
        } else if (position.x + shape[0].size > FieldConstants.COLUMN_COUNT.value) {
            false
        } else {
            for (i in shape.indices) {
                for (j in shape[i].indices) {
                    val y = position.y + i
                    val x = position.x + j
                    if (CellConstants.EMPTY.value != shape[i][j] &&
                            CellConstants.EMPTY.value != field[y][x]) {
                        return false
                    }
                }
            }
            true
        }
    }
    //TODO: p.132
}