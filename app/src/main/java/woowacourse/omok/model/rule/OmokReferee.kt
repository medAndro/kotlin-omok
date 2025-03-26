package woowacourse.omok.model.rule

import woowacourse.omok.model.board.Board
import woowacourse.omok.model.stone.Stone
import woowacourse.omok.model.stone.StoneColor

class OmokReferee(
    private val renjuRule: RenjuRule,
) {
    fun isOmok(board: Board): Boolean = renjuRule.isOmok(board)

    fun lastStoneFoul(board: Board): RenjuFoul {
        val lastStone: Stone = board.lastStone ?: return RenjuFoul.SAFE
        if (lastStone.stoneColor == StoneColor.BLACK) {
            return renjuRule.checkLastBlackStoneFoul(board.stonesMap, lastStone)
        }
        return RenjuFoul.SAFE
    }
}
