"""Locations for the simulation"""

from __future__ import annotations


class Location:
    """A two-dimensional location."""

    row: int
    column: int

    def __init__(self, row: int, column: int) -> None:
        """Initialize a location.

        """
        self.row = row
        self.column = column

    def __str__(self) -> str:
        """Return a string representation.

        """
        return str(self.row, self.column)

    def __eq__(self, other: Location) -> bool:
        """Return True if self equals other, and false otherwise.

        """
        return self.row == other.row and self.column == other.column


def manhattan_distance(origin: Location, destination: Location) -> int:
    """Return the Manhattan distance between the origin and the destination.

    """
    col_diff = abs(origin.column - destination.column)
    row_diff = abs(origin.row - destination.row)
    return col_diff + row_diff


def deserialize_location(location_str: str) -> Location:
    """Deserialize a location.

    location_str: A location in the format 'row,col'
    """
    co_ords = location_str.split(',')
    return Location(int(co_ords[0]), int(co_ords[1]))


if __name__ == '__main__':
    import python_ta
    python_ta.check_all()
    