import Util.Vector._
import zio.UIO

import scala.io.Source
import scala.util.Try

object Day3 extends Day[Long, Long] {
  sealed trait MapThing
  case object Tree extends MapThing
  case object Empty extends MapThing

  case class Map(rows: Int, cols: Int, content: Array[Array[MapThing]]) {
    def get(p: Pos): Option[MapThing] = {
      val r = p.y
      val c = p.x % cols
      Try(content(r)(c)).toOption
    }

    def prettyPrint: String = (for {
      arr <- content
      s = arr.map {
        case Tree => '#'
        case Empty => '.'
      }.mkString
    } yield s).mkString("\n")
  }

  object Map {
    def fromString(s: String): Map = {
      def fromLine(line: String): Array[MapThing] = line.map {
        case '#' => Tree
        case '.' => Empty
        case _ => ???
      }.toArray

      val content = Source.fromString(s).getLines().map(fromLine).toArray
      Map(content.size, content.head.size, content)
    }
  }

  def countOnSlope(start: Pos, slope: Dir, thing: MapThing, map: Map, acc: Long): Long = map.get(start) match {
    case Some(t) if t == thing => countOnSlope(start + slope, slope, thing, map, acc + 1)
    case Some(_) => countOnSlope(start + slope, slope, thing, map, acc)
    case None => acc
  }

  def part1(in: String) = UIO{
    val m = Map.fromString(in)
    countOnSlope(Pos(0, 0), Dir(3, 1), Tree, m, 0)
  }

  def part2(in: String) = UIO{
    val m = Map.fromString(in)
    val slopes = List(Dir(1, 1), Dir(3, 1), Dir(5, 1), Dir(7, 1), Dir(1, 2))
    slopes.map(d => countOnSlope(Pos(0, 0), d, Tree, m, 0)).reduce(_ * _)
  }

  val inputs = scala.Predef.Map(
    "example" -> InputString("""..##.......
                   |#...#...#..
                   |.#....#..#.
                   |..#.#...#.#
                   |.#...##..#.
                   |..#.##.....
                   |.#.#.#....#
                   |.#........#
                   |#.##...#...
                   |#...##....#
                   |.#..#...#.#""".stripMargin),
    "puzzle" -> InputString(""".......#..#....#...#...#......#
                  |..##..#...##.###.#..#.....#.#..
                  |#..#.#....#......#..#.........#
                  |.#..##...#........#....#..#..#.
                  |#.#.#....###...#........#.....#
                  |.#...#.#.##.#.##...#.#.........
                  |####......#.......###.##.#.....
                  |..#...........#...#.#.#........
                  |.#.......#....###.####..#......
                  |...##........#....##.......##..
                  |.###......##.#......##....#.#.#
                  |........#.#......##...#......#.
                  |#....##.#..#...#.......#.......
                  |.#..##........##.........#....#
                  |.#..#..#...#....#.#......#.#...
                  |..#.#......##.#.......#....##..
                  |......##......#.#..##.#..#...#.
                  |.....##.......#.#....#.#.......
                  |........#.....#.....#..###.#...
                  |#........#..#.....#...#.#.#..#.
                  |.#..#.....#...#........#.....#.
                  |.#.#.....#.....#...#...........
                  |.....#.#..#..#...#..#..#..##..#
                  |##.#...#....#..#.##..#.....#.#.
                  |#.......####......#..#.#....#..
                  |......#.#...####.........#.#..#
                  |.#.........#..#.#...#..........
                  |...#####.#....#.#..#......#.#.#
                  |##....#.###....##...##..#.....#
                  |...........####.##.#....##.##..
                  |#.#.#..........#.#..##.#.######
                  |##...#..#...........###..#....#
                  |.#.#.#...##..........##.#...#..
                  |...#.#........#..##...#....#...
                  |......#..#...#..##....#.......#
                  |.#..#.......#..#......##....##.
                  |.......#.......#........#..##..
                  |...#...#...#.##......#.##.#....
                  |.........#.........#.#.#.##....
                  |..#...................#....#..#
                  |.........#..#.....#.#...#....#.
                  |#.#.#...#........#..###.#......
                  |#.#.#.####......##...#...#....#
                  |#...........##..#.#.#....#..#..
                  |........#..#.#...........##.#.#
                  |.#.........#...........#..#....
                  |#............##.#..#....##...##
                  |.#....##..#.#....#.......#..#..
                  |..#.#...#.#......####.......#..
                  |...#.#.......###......#.....#..
                  |#......#.......#.#...#.#..##...
                  |...#.....#...##.#.....#.#......
                  |#.#.#............#..#......#..#
                  |....#...#...##.##.##...##.#....
                  |..##........#..#........#...##.
                  |.......#..#...#.........#.....#
                  |...........#.#......#...#......
                  |...##..##..##..###..#..#..#..#.
                  |#..##.......##..#....#....#.#..
                  |#.#.##.#..##.....#....#.#......
                  |....#..##......#.#..#....#....#
                  |.#.#.........##...#......##.##.
                  |##...........#..#.....#.###....
                  |.#.###........#...#....##..#...
                  |......##.....#.................
                  |.#.##..#.#.......#......#.#.#..
                  |.#...#....#.##..........##.##..
                  |#...##......####.#....#....#...
                  |.#...#.##.#.#.....#...#........
                  |.#................#.##.#.###...
                  |...#.#..#.#.....##.....##....#.
                  |..##.#..#..##.....#....#...#.##
                  |........###.##..#..###.....#..#
                  |..##.....#.......#.#...##......
                  |#.#..###...##.###.##.#..#...#..
                  |#..#..#.#...#....#...##.....#.#
                  |#..................#........#..
                  |#.....#.......#.##....##....#..
                  |...#.............#.....#...#...
                  |...#...#.##..##.....#........#.
                  |.......#........##....###..##..
                  |.#....#....#.#..#......#....#.#
                  |..........#..#.#.....##...#.##.
                  |.#...##.#...........#.#.......#
                  |..#.##.....#.###.#.............
                  |..#....###..........#.#.#......
                  |#.....#.####..#.#......#..#.#.#
                  |...#........#..#...............
                  |.###.#.##.....#.#...........#..
                  |..#....#..#....#..##....#......
                  |......#..#.....#.#.##.......#.#
                  |###..#...#.#..#....#..##.###..#
                  |.#....##.###........##...##.#.#
                  |........##..##.#....##..#....#.
                  |...#..#....#.#....#...#...##...
                  |#.....#......#.##........#....#
                  |....#....###.##...#.#.##....#..
                  |......#.##..#.#..........#...#.
                  |...........#...#....##...#....#
                  |......#.#.........#....#.#.#...
                  |.###..........#.###.##....#...#
                  |...##.......#......#....#....#.
                  |#..#...#.#..####...#......#..#.
                  |....##..#.#.........#..........
                  |.##.###.##....##.####....#...#.
                  |..##.......#........#...#..#...
                  |....#####..........###....#....
                  |.#.#..#.#.#....#..#............
                  |........#.....#....#.......##..
                  |...........##....##..##.....##.
                  |..###........#.#.#..#....##...#
                  |.....#...........##......#..#..
                  |...##........#.##.#......##..#.
                  |##..#....#............##..#..#.
                  |.#.....#...##.##..............#
                  |#..##........#...#...#......##.
                  |......##.....#.......####.##..#
                  |...#.#....#...#..#.............
                  |..#...#..##.###..#..#.......##.
                  |##....###.......#...#..#.......
                  |#..#.....###.....#.#.........#.
                  |#.#....#.............#...#.....
                  |..#.#.##..........#.....##.#...
                  |.....##......#..#..#.....#..#..
                  |##.#..#..#.##......###....#..#.
                  |...#............##...#..##.....
                  |.#..#....#.........#......#.##.
                  |.##.##...#..............#..#.##
                  |...#....#...###...#...#....#..#
                  |..#...#..####..#....#.#...##..#
                  |..............##.##.......##...
                  |..##.#..##...........#.#.#...#.
                  |..................##.####.###..
                  |.#...........#.......#......#..
                  |.#.#.#...#....#.........##...##
                  |....#..........#.#....#.#.....#
                  |..........#.#..........#.#.....
                  |...........#.....#.#......#....
                  |........#..#.#.#.#.............
                  |...###...##...##..####.##......
                  |.#..#......###.....#...#.....#.
                  |.........##............#.#.....
                  |#.#..#.#.#....###.#.#..#..#..##
                  |..........#...#.##.#..#..#....#
                  |#..#.......##....#..##........#
                  |##.#...#....##.............#...
                  |....#........#......##..#..#.##
                  |.................#.#.#.#.#.....
                  |...........#.#.....#.......#...
                  |#.......#.......#............#.
                  |....#...........#.#.##.....#..#
                  |#...#.....#....#..##...#.......
                  |..#.....#.....#.##.##....#.....
                  |.#.#..#...#..#..##.....##..#...
                  |.#.#....#.........####.........
                  |#...#..####.....#...#..##......
                  |..#...##.#.....#...#.....##....
                  |.#...#.....#.#.#......#.......#
                  |..#.....##.#..#.#...##.........
                  |##.#...#..#....#....#.##.##...#
                  |.#..#....#..##.#.......#..#....
                  |...##.#......#...###.......#...
                  |...#..#.........##.####........
                  |#.#..#..##...........#..#......
                  |.#...#.#......#.#..........#...
                  |...###...#.......#.....#.#...##
                  |..#....#.#.##..........##...#..
                  |.....###.........#.....#..##..#
                  |.......##.....#.#.....#.#..##..
                  |.#.#.###..##.......##...#......
                  |......#.....#................##
                  |.#......##..##.#.#...#...#...##
                  |.#...#......#.......#.#........
                  |.#..........###...#..#...#.....
                  |.........##.....#.#..#..#.#...#
                  |#...#...#.........#..#..#....#.
                  |###.......#.#.....#....##......
                  |.#..#......#..#...........#..#.
                  |..##....##..##...#......#......
                  |.#........#....#...#....#.....#
                  |.#.......#...#...#..##.#.#..#..
                  |#...#........#.##.....#.....#..
                  |#..##.....#..........#...#...##
                  |............#...............#..
                  |.#.##...#.....#.#..#..#..#.....
                  |.#.#.#...#........#....#...##..
                  |##......#.....#.###.#...#.#..#.
                  |.........##..#..#.#...#...#...#
                  |#...#.#....#..#..#.....#.......
                  |.......#.###...#.............#.
                  |..#.....#.#.#..###.#....#.....#
                  |....#...#.#....#.#..........#..
                  |..#......#.###.#.#..#.....#...#
                  |#............#..##...##......#.
                  |#...........#..#....#.###..###.
                  |.#.##.#.#.......#.............#
                  |..............#................
                  |..#.#.....#.....#...#......#...
                  |.#.#.#..#..#.#...........##....
                  |.....##.#......#..#.##....#....
                  |.......##..#.#.#..#............
                  |..#.....#.....#.###..#.....#.#.
                  |......##.....#..##.#...#.....#.
                  |...#...#....#..#..#........#...
                  |..#.##..#....#.........#.#..#..
                  |#....#.....###.....#......#....
                  |##.....#..#..##.........#.##.##
                  |.#.#....#.#..........#.........
                  |.##.#...#..#.......#.##...#....
                  |...#...#.....#....#...#.#..#...
                  |.....#....#.....#.....#.#......
                  |...........#.#.......#.......#.
                  |.........##.###.##........#....
                  |#..##.....#...#.#..............
                  |.#...#....##........#.#..#....#
                  |..#...#........#...#..#.##.#..#
                  |........#...#.....##.#.#....#.#
                  |#..#.......###.#....#.#.#......
                  |.......#...##....#...#..##..#..
                  |.....##........#.#.#..#....##..
                  |.#....#..#.#...........#......#
                  |...##....#.##.....##.......#...
                  |.##..#..#....#.#....#..#....##.
                  |..#....#.....###.......#..##..#
                  |....#.......#....##..#....#..##
                  |....#......##..#....#.#...#.#..
                  |.##.#......##..................
                  |##.#....#........#..#..#...##.#
                  |.......#..#.#...##.....#.#.....
                  |..##.#...........#.#.#..#.#.#..
                  |.....#....#......#..#.......#..
                  |#.#...#.####..##.......#..##...
                  |...#....#.....#.##.#..#.##..#..
                  |.#.......#......##........##.#.
                  |.......#.#...#..#...#..##.#....
                  |.#....#........#.#.....##..#..#
                  |#..#.....#..#.............#...#
                  |#...#....#..#...###..#...#.#...
                  |.#..#.....#..........#..##.####
                  |#.#.#.#.##.#.#.....##.#........
                  |...#....##....#...#..##.......#
                  |..##.##.#.#........#..........#
                  |..###........###..#..........#.
                  |...#......#..##.#........#..#..
                  |#.#.#..#........#..#..........#
                  |...#........#..##.#...#.###....
                  |##......#.####.#....#......#...
                  |.#..#......#................#..
                  |#.#........#.#.....##.....##...
                  |#...............#..#.......#.#.
                  |.##..#...........##..#..#.#....
                  |#......#.#.......#.#.#.##..#.##
                  |.....##.#..###.............##..
                  |....##.........#..#...#........
                  |.....#.....#.#.#..#.#..........
                  |#.........#....##.#.##.....#..#
                  |.#.........#......#.#.##.#.#...
                  |##.........#.....#..#.#..#.##.#
                  |....#......##...#.....#..#..###
                  |..#..............#...#..####...
                  |#....#...##.#.......#...#..#...
                  |#.......###.#.#.......#.......#
                  |...##....#.#...........#...###.
                  |...........#..#.#.....#..##..#.
                  |..#.........#..###..#.....#...#
                  |..#.#.....#.#.#...#.#.#......#.
                  |........#.....#.#......##....##
                  |##.#.#...#.#........#.....#...#
                  |........#....#...............#.
                  |##.###......####...#####..#....
                  |...##...#..#....#........#...#.
                  |...###.#..................##.#.
                  |##.#.......###.......#...#.#...
                  |....#..#.#...#...#....#.#.#..##
                  |....#...........#..#...........
                  |#..#.#..#...#...#..#...........
                  |...#...#.#....#..#....#........
                  |#....#.......#.##........#..#..
                  |.....#...#..#................#.
                  |#......#.......#..........##..#
                  |.#....#.#......#.#...#....##..#
                  |...#.##...#......#.#...##...##.
                  |..#...#..##...#...#....#.......
                  |.....#....#.#.#..........#.#...
                  |...#...#..#....#..#.#..........
                  |......#.#..........##.......#..
                  |.#...##.#.#...#..##..#...#.....
                  |..#..#.........#........#.#.#..
                  |#.#..##..#.....##......#.....#.
                  |#..#.....#.#....#...#.#....#.#.
                  |......#........##.#..#...#.....
                  |...#.##.#.#......#.#..##...#..#
                  |....#..###..#..#.....###....##.
                  |.....#...#.#.....#..........#.#
                  |.#...##..##.....#..#...#.#.#...
                  |.##.#......##...##..#...#.....#
                  |.#.##....#...#.##.#.#...#.#...#
                  |....#.#...#....###.#.....#.....
                  |#.....####................#..#.
                  |....#.....#...#.#.......##.#...
                  |.#...##.#...#..#...........#.#.
                  |..#####..#.#...#...##........#.
                  |...#...##........#...#.#....###
                  |........#.#.#..#.....#.......#.
                  |...#...#..##............##.....
                  |#.#..###....###.#...#.#...##.##
                  |..#.##...#......#..#.........##
                  |.##..#..#.....#..#.........#.#.
                  |.#..#.#....#.##...#..#.##....##
                  |..#...#.#...##.#.#...#...#....#
                  |#..........#.......##..##....#.
                  |#...###.#......#....#.........#
                  |#.....#...##.......##....##....
                  |.##.#..#.##......#.##....#..#..
                  |............#.#....##.#..#....#
                  |.#.........##.##...#....#.....#
                  |##....##..#..#....##...#.....##
                  |...#.....#...........#.....##..
                  |......#...#.........#.......#..
                  |............#...##.#.....#.#.#.
                  |.#........##..........#.....#.#
                  |.###.........#.....#.##...#....
                  |.##..#...##...#..#..#.##.......
                  |""".stripMargin)
  )

}
