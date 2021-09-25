package bucket_game.ui

import org.jline.terminal.{Cursor, Size, Terminal}
import org.jline.utils.{AttributedString, Display}
import org.jline.utils.InfoCmp.Capability

import java.util
import scala.jdk.CollectionConverters._
import scala.collection.mutable.ArrayBuffer

class ConsoleWindowAPI(
  val width: Int,
  val height: Int,
  terminal: Terminal
) extends VirtualView {
  private val display = new Display(terminal, true)
  initTerminal()

  private def initTerminal(): Unit = {
//    print(f"\u001b[${height}T")
//    print(f"\u001b[${height}S")
//    print(s"\u001b[${height}A")
//    print("\u001b[s")
    terminal.puts(Capability.enter_ca_mode)
    display.resize(terminal.getHeight, terminal.getWidth)
    display.clear()
    display.reset()
  }

//  private def clearConsole(): Unit = {
//    print("\u001b[u")
//  }

  def renderScreen(): Unit = {
    display.resize(terminal.getHeight, terminal.getWidth)
    val lines = matrix.map(line => new AttributedString(line.mkString(""))).toList.asJava
    val arrayList = new util.ArrayList[AttributedString](lines)
    display.update(arrayList, terminal.getSize.cursorPos(1, 0))
//    print(
//      matrix.map(line => line.mkString("")).mkString("\n")
//    )
  }

  def clear(): Unit = {
//    clearConsole()
//    display.clear()
//    display.reset()
//    terminal.puts(Capability.clear_screen)
//    terminal.flush()
    clearMatrix()
  }
}
