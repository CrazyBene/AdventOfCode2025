package utils

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon

val geomFactory = GeometryFactory()

fun polygonFromCorners(corners: List<Pair<Long, Long>>): Polygon {
    val coords =
        corners.map { Coordinate(it.first.toDouble(), it.second.toDouble()) } +
            Coordinate(corners.first().first.toDouble(), corners.first().second.toDouble())

    return geomFactory.createPolygon(coords.toTypedArray())
}

fun rectangleFromCorners(
    a: Pair<Long, Long>,
    b: Pair<Long, Long>,
): Polygon {
    val (x1, y1) = a
    val (x2, y2) = b

    val minX = minOf(x1, x2).toDouble()
    val maxX = maxOf(x1, x2).toDouble()
    val minY = minOf(y1, y2).toDouble()
    val maxY = maxOf(y1, y2).toDouble()

    val coords =
        arrayOf(
            Coordinate(minX, minY),
            Coordinate(maxX, minY),
            Coordinate(maxX, maxY),
            Coordinate(minX, maxY),
            Coordinate(minX, minY),
        )

    return geomFactory.createPolygon(coords)
}
