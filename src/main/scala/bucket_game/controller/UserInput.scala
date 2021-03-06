package bucket_game.controller

import bucket_game.game_management.interfaces.Controller
import bucket_game.game_management.{GameManager, GameOver, GameState, Pending, RoundFinished, Running}
import org.jline.keymap.{BindingReader, KeyMap}
import org.jline.terminal.{Terminal, TerminalBuilder}

import java.util.{Timer, TimerTask}
import scala.math.Pi

class UserInput(
                 var velocityStep: Float = 1f,
                 var rotationStep: Float = (Pi / 24).toFloat,
                 terminal: Terminal
               ) extends Controller {
  private val maxCommandLen = 3

  private val reader = terminal.reader()
  private val keyMap = new KeyMap[Action.Value]
  keyMap.bind(Action.Up, "\u001b[A")
  keyMap.bind(Action.Up, "\u001bOA")
  keyMap.bind(Action.Down, "\u001b[B")
  keyMap.bind(Action.Down, "\u001bOB")
  keyMap.bind(Action.Right, "\u001b[C")
  keyMap.bind(Action.Right, "\u001bOC")
  keyMap.bind(Action.Left, "\u001b[D")
  keyMap.bind(Action.Left, "\u001bOD")
  keyMap.bind(Action.Start, "\u000d")
  keyMap.bind(Action.ExitProgram, KeyMap.esc())
  keyMap.bind(Action.ExitProgram, KeyMap.ctrl('c'))
  keyMap.bind(Action.ExitProgram, KeyMap.ctrl('C'))
  keyMap.bind(Action.ExitProgram, "\u001bc")
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

  private def readEscape(): Boolean = {
    readBinding() match {
      case Action.ExitProgram => true
      case _ => false
    }
  }

  def consumeCommandIfExists(gameManager: GameManager): Unit = {
    gameManager.state match {
      case Pending() =>
        val action = readBinding()
        action match {
          case Action.Right => gameManager.increaseBallVelocity(velocityStep)
          case Action.Left => gameManager.decreaseBallVelocity(velocityStep)
          case Action.Up => gameManager.rotateBallDirection(rotationStep)
          case Action.Down => gameManager.rotateBallDirection(-rotationStep)
          case Action.Start => gameManager.startAction()
          case Action.ExitProgram => System.exit(0)
          case _ =>
        }
      case RoundFinished(_) =>
        val action = readBinding()
        action match {
          case Action.Start => gameManager.startNextRound()
          case Action.ExitProgram => System.exit(0)
          case _ =>
        }
      case GameOver() =>
        val action = readBinding()
        action match {
          case Action.Start => gameManager.startRound()
          case Action.ExitProgram => System.exit(0)
          case _ =>
        }
      case Running() =>
      case _ => if (readEscape()) System.exit(0)
    }
  }
}
