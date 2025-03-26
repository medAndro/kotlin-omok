package woowacourse.omok

import android.os.Bundle
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import woowacourse.omok.controller.OmokAppControl
import woowacourse.omok.model.board.BoardSize
import kotlin.concurrent.thread
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val omokAppControl = OmokAppControl(this, BoardSize(BOARD_SIZE))

        val board = findViewById<TableLayout>(R.id.board)
        board.children.forEachIndexed { rowIndex, rowView ->
            if (rowView is TableRow) {
                rowView.children.forEachIndexed { colIndex, cellView ->
                    if (cellView is ImageView) {
                        cellView.setTag(Pair(abs(BOARD_SIZE - INDEX_OFFSET - rowIndex), colIndex))
                        cellView.setOnClickListener {
                            thread {
                                val coordinate = cellView.tag as Pair<Int, Int>
                                omokAppControl.turn(cellView, coordinate)
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val BOARD_SIZE = 15
        private const val INDEX_OFFSET = 1
    }
}
