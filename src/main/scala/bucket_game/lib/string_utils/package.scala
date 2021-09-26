package bucket_game.lib

package object string_utils {
  // parse [] in the line and fill this place with spaces
  // so that the line length equals to width argument
  def fillWithSpaces(width: Int)(line: String): String = {
    val substituteCount = "\\[\\]".r.findAllIn(line).length
    val totalSpaceCount = width - line.length + 2 * substituteCount + 1
    val equalPart = totalSpaceCount / substituteCount
    val rest = totalSpaceCount - equalPart * substituteCount
    line.replaceFirst("\\[\\]", " " * (equalPart + rest))
    line.replaceAll("\\[\\]", " " * equalPart)
  }
}