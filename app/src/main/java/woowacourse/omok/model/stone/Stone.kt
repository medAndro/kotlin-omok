package woowacourse.omok.model.stone

import woowacourse.omok.model.stone.StoneColor
import woowacourse.omok.model.stone.position.Position

data class Stone(
    val position: Position,
    val stoneColor: StoneColor,
)
