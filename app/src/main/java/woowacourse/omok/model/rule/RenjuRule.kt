package woowacourse.omok.model.rule

import woowacourse.omok.model.board.Board
import woowacourse.omok.model.stone.Stone
import woowacourse.omok.model.stone.StoneColor
import woowacourse.omok.model.stone.position.Position

interface RenjuRule {
    fun checkLastBlackStoneFoul(
        stonesMap: Map<Position, StoneColor>,
        lastStone: Stone,
    ): RenjuFoul

    fun isOmok(board: Board): Boolean
}
