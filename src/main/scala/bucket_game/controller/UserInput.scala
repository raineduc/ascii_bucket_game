package bucket_game.controller

import bucket_game.game_management.GameManager
import org.jline.keymap.{BindingReader, KeyMap}
import org.jline.terminal.TerminalBuilder

import scala.math.Pi

class UserInput(
                 private val gameManager: GameManager,
                 var velocityStep: Float = 1f,
                 var rotationStep: Float = (Pi / 24).toFloat
               ) {
  private val maxCommandLen = 3
  private val terminal = TerminalBuilder.builder()
    .jna(true)
    .system(true)
    .build()
  terminal.enterRawMode()
  private val reader = terminal.reader()

  private val keyMap = new KeyMap[Action.Value]
  keyMap.bind(Action.Up, "\u001b[A")
  keyMap.bind(Action.Down, "\u001b[B")
  keyMap.bind(Action.Right, "\u001b[C")
  keyMap.bind(Action.Left, "\u001b[D")
  keyMap.bind(Action.Start, "\u000d")
  keyMap.bind(Action.ExitProgram, "\u001bC")


  private def readBinding(str: String): Action.Value = {
    if (str.length < maxCommandLen) {
      val chr = reader.read(100L)
      if (chr > 0) return readBinding(str + chr.toChar)
    }
    keyMap.getBound(str)
  }

  private def readBinding(): Action.Value = {
    readBinding("")
  }

  def consumeCommandIfExists(): Unit = {
    val action = readBinding()

    action match {
      case Action.Right => gameManager.increaseBallVelocity(velocityStep)
      case Action.Left => gameManager.decreaseBallVelocity(velocityStep)
      case Action.Up => gameManager.rotateBallDirection(rotationStep)
      case Action.Down => gameManager.decreaseBallVelocity(-rotationStep)
      case Action.Start => gameManager.startRound()
      case Action.ExitProgram => System.exit(0)
      case _ =>
    }
  }
}
