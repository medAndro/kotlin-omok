package woowacourse.omok.controller

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import woowacourse.omok.R
import woowacourse.omok.model.board.Board
import woowacourse.omok.model.board.BoardSize
import woowacourse.omok.model.board.PositionStatus
import woowacourse.omok.model.board.PositionStatus.EMPTY
import woowacourse.omok.model.board.PositionStatus.OUT_OF_RANGE
import woowacourse.omok.model.board.PositionStatus.STONE_ALREADY_EXITS
import woowacourse.omok.model.rule.BudoolRenjuRuleAdapter
import woowacourse.omok.model.rule.OmokReferee
import woowacourse.omok.model.rule.RenjuFoul
import woowacourse.omok.model.rule.RenjuFoul.FOUR_BY_FOUR_FOUL
import woowacourse.omok.model.rule.RenjuFoul.OVER_FIVE_FOUL
import woowacourse.omok.model.rule.RenjuFoul.SAFE
import woowacourse.omok.model.rule.RenjuFoul.THREE_BY_THREE_FOUL
import woowacourse.omok.model.stone.StoneColor
import woowacourse.omok.model.stone.position.Col
import woowacourse.omok.model.stone.position.Position
import woowacourse.omok.model.stone.position.Row

class OmokAppControl(
    private val context: Context,
    boardSize: BoardSize,
) {
    private val omokReferee = OmokReferee(BudoolRenjuRuleAdapter(boardSize))
    private var board = Board(boardSize)

    fun turn(
        view: ImageView,
        coordinate: Pair<Int, Int>,
    ) {
        if (omokReferee.isOmok(board)) {
            omokAlert()
            return
        }
        val row = Row(coordinate.first)
        val col = Col(coordinate.second)
        val nextPosition = Position(row, col)
        if (!isValidPosition(nextPosition)) return

        stoneAdd(nextPosition, view)
    }

    private fun isValidPosition(position: Position): Boolean {
        val positionState = board.positionStatus(position)

        if (positionState == EMPTY) {
            return true
        }
        printPositionStatus(positionState)
        return false
    }

    private fun printPositionStatus(positionState: PositionStatus) {
        when (positionState) {
            STONE_ALREADY_EXITS -> printToast(ERROR_STONE_ALREADY_EXITS)
            OUT_OF_RANGE -> printToast(ERROR_OUT_OF_RANGE)
            EMPTY -> {}
        }
    }

    private fun printFoul(foul: RenjuFoul) {
        when (foul) {
            THREE_BY_THREE_FOUL -> printToast(ERROR_THREE_BY_THREE_FOUL)
            FOUR_BY_FOUR_FOUL -> printToast(ERROR_FOUR_BY_FOUR_FOUL)
            OVER_FIVE_FOUL -> printToast(ERROR_OVER_FIVE_FOUL)
            SAFE -> {}
        }
    }

    private fun printToast(message: String) {
        (context as? Activity)?.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun stoneAdd(
        nextPosition: Position,
        view: ImageView,
    ) {
        val newBoard = board.nextStonePlacedBoard(nextPosition)
        val foul = omokReferee.lastStoneFoul(newBoard)

        if (foul == SAFE) {
            showStone(board.nextStoneColor, view)
            board = newBoard
            if (omokReferee.isOmok(board)) {
                omokAlert()
            }
            return
        }
        printFoul(foul)
    }

    private fun showStone(
        nextStoneColor: StoneColor,
        view: ImageView,
    ) {
        (context as? Activity)?.runOnUiThread {
            when (nextStoneColor) {
                StoneColor.BLACK -> view.setImageResource(R.drawable.black_stone)
                StoneColor.WHITE -> view.setImageResource(R.drawable.white_stone)
            }
        }
    }

    private fun omokAlert() {
        val stoneColorText = stoneColorText(board.lastStone?.stoneColor)
        printToast(WIN_MESSAGE.format(stoneColorText))
    }

    private fun stoneColorText(stoneColor: StoneColor?): String =
        when (stoneColor) {
            StoneColor.BLACK -> BLACK_STONE_KOREAN_TEXT
            StoneColor.WHITE -> WHITE_STONE_KOREAN_TEXT
            else -> ""
        }

    companion object {
        private const val NEXT_TURN_MESSAGE = "%s의 차례 입니다."
        private const val NEXT_TURN_WITH_LAST_STONE_MESSAGE = "%s의 차례 입니다. (마지막 돌의 위치: %s)"
        private const val WIN_MESSAGE = "%s이 우승했습니다."

        private const val ERROR_THREE_BY_THREE_FOUL = "3-3 반칙이 발생했습니다"
        private const val ERROR_FOUR_BY_FOUR_FOUL = "4-4 반칙이 발생했습니다"
        private const val ERROR_OVER_FIVE_FOUL = "장목 반칙이 발생했습니다"

        private const val ERROR_STONE_ALREADY_EXITS = "해당하는 위치에 돌이 존재합니다"
        private const val ERROR_OUT_OF_RANGE = "돌이 보드의 범위를 벗어났습니다"
        private const val BLACK_STONE_KOREAN_TEXT = "흑"
        private const val WHITE_STONE_KOREAN_TEXT = "백"
    }
}
