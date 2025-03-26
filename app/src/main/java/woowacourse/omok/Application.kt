package woowacourse.omok

import woowacourse.omok.controller.OmokConsoleControl
import woowacourse.omok.model.board.BoardSize
import woowacourse.omok.view.InputView
import woowacourse.omok.view.OutputView

fun main() {
    val boardSize = BoardSize(15)
    val inputView = InputView()
    val outputView = OutputView(boardSize)

    OmokConsoleControl(inputView, outputView, boardSize).run()
}
