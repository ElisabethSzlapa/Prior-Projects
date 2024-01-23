"""
The rider module contains the Rider class. It also contains
constants that represent the status of the rider.

=== Constants ===
WAITING: A constant used for the waiting rider status.
CANCELLED: A constant used for the cancelled rider status.
SATISFIED: A constant used for the satisfied rider status
"""

from typing import Union
from location import Location

WAITING = "waiting"
CANCELLED = "cancelled"
SATISFIED = "satisfied"


class Rider:

    """A rider for a ride-sharing service.

    === Attributes ===
    id: A unique identifier for the driver.
    patience: Time rider is willing to wait for their driver before cancelling.
    origin: Rider's initial location.
    destination: Rider's destination location.
    status: Rider's current status (waiting, cancelled, or satisfied).

    """

    id: str
    patience: int
    origin: Location
    destination: Location
    status: Union[str, None]

    def __init__(self, identifier: str, patience: int, origin: Location,
                 destination: Location) -> None:
        """Initialize a Rider.

        """
        self.id = identifier
        self.patience = patience
        self.origin = origin
        self.destination = destination
        self.status = None


if __name__ == '__main__':
    import python_ta
    python_ta.check_all(config={'extra-imports': ['location']})
