"""Drivers for the simulation"""

from location import Location, manhattan_distance
from rider import Rider


class Driver:
    """A driver for a ride-sharing service.

    === Attributes ===
    id: A unique identifier for the driver.
    location: The current location of the driver.
    is_idle: True if the driver is idle and False otherwise.
    """

    id: str
    location: Location
    is_idle: bool
    speed: int
    destination: Location

    def __init__(self, identifier: str, location: Location, speed: int) -> None:
        """Initialize a Driver.

        """
        self.id = identifier
        self.location = location
        self.is_idle = True
        self.speed = speed
        self.destination = None

    def __str__(self) -> str:
        """Return a string representation.

        """
        return str(self.id)

    def __eq__(self, other: object) -> bool:
        """Return True if self equals other, and false otherwise.

        """
        if not self.id == other.id:
            return False
        if not self.location == other.location:
            return False
        if not self.is_idle == other.is_idle:
            return False
        if not self.speed == other.speed:
            return False
        return self.destination == other.destination

    def get_travel_time(self, destination: Location) -> int:
        """Return the time it will take to arrive at the destination,
        rounded to the nearest integer.

        """
        return round(manhattan_distance(self.location, destination)
                     / self.speed)

    def start_drive(self, location: Location) -> int:
        """Start driving to the location.
        Return the time that the drive will take.

        """
        self.is_idle = False
        self.destination = location
        return self.get_travel_time(location)

    def end_drive(self) -> None:
        """End the drive and arrive at the destination.

        Precondition: self.destination is not None.

        """
        self.location = self.destination

    def start_ride(self, rider: Rider) -> int:
        """Start a ride and return the time the ride will take.

        """
        self.destination = rider.destination
        return self.get_travel_time(rider.destination)

    def end_ride(self) -> None:
        """End the current ride, and arrive at the rider's destination.

        Precondition: The driver has a rider.
        Precondition: self.destination is not None.

        """
        self.location = self.destination


if __name__ == '__main__':
    import python_ta
    python_ta.check_all(
        config={'extra-imports': ['location', 'rider']})
