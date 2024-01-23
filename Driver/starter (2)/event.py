"""Simulation Events

This file should contain all of the classes necessary to model the different
kinds of events in the simulation.
"""
from __future__ import annotations
from typing import List
from rider import Rider, WAITING, CANCELLED, SATISFIED
from dispatcher import Dispatcher
from driver import Driver
from location import deserialize_location
from monitor import Monitor, RIDER, DRIVER, REQUEST, CANCEL, PICKUP, DROPOFF


class Event:
    """An event.

    Events have an ordering that is based on the event timestamp: Events with
    older timestamps are less than those with newer timestamps.

    This class is abstract; subclasses must implement do().

    You may, if you wish, change the API of this class to add
    extra public methods or attributes. Make sure that anything
    you add makes sense for ALL events, and not just a particular
    event type.

    Document any such changes carefully!

    === Attributes ===
    timestamp: A timestamp for this event.
    """

    timestamp: int

    def __init__(self, timestamp: int) -> None:
        """Initialize an Event with a given timestamp.

        Precondition: timestamp must be a non-negative integer.

        >>> Event(7).timestamp
        7
        """
        self.timestamp = timestamp

    # The following six 'magic methods' are overridden to allow for easy
    # comparison of Event instances. All comparisons simply perform the
    # same comparison on the 'timestamp' attribute of the two events.
    def __eq__(self, other: Event) -> bool:
        """Return True iff this Event is equal to <other>.

        Two events are equal iff they have the same timestamp.

        >>> first = Event(1)
        >>> second = Event(2)
        >>> first == second
        False
        >>> second.timestamp = first.timestamp
        >>> first == second
        True
        """
        return self.timestamp == other.timestamp

    def __ne__(self, other: Event) -> bool:
        """Return True iff this Event is not equal to <other>.

        >>> first = Event(1)
        >>> second = Event(2)
        >>> first != second
        True
        >>> second.timestamp = first.timestamp
        >>> first != second
        False
        """
        return not self == other

    def __lt__(self, other: Event) -> bool:
        """Return True iff this Event is less than <other>.

        >>> first = Event(1)
        >>> second = Event(2)
        >>> first < second
        True
        >>> second < first
        False
        """
        return self.timestamp < other.timestamp

    def __le__(self, other: Event) -> bool:
        """Return True iff this Event is less than or equal to <other>.

        >>> first = Event(1)
        >>> second = Event(2)
        >>> first <= first
        True
        >>> first <= second
        True
        >>> second <= first
        False
        """
        return self.timestamp <= other.timestamp

    def __gt__(self, other: Event) -> bool:
        """Return True iff this Event is greater than <other>.

        >>> first = Event(1)
        >>> second = Event(2)
        >>> first > second
        False
        >>> second > first
        True
        """
        return not self <= other

    def __ge__(self, other: Event) -> bool:
        """Return True iff this Event is greater than or equal to <other>.

        >>> first = Event(1)
        >>> second = Event(2)
        >>> first >= first
        True
        >>> first >= second
        False
        >>> second >= first
        True
        """
        return not self < other

    def __str__(self) -> str:
        """Return a string representation of this event.

        """
        raise NotImplementedError("Implemented in a subclass")

    def do(self, dispatcher: Dispatcher, monitor: Monitor) -> List[Event]:
        """Do this Event.

        Update the state of the simulation, using the dispatcher, and any
        attributes according to the meaning of the event.

        Notify the monitor of any activities that have occurred during the
        event.

        Return a list of new events spawned by this event (making sure the
        timestamps are correct).

        Note: the "business logic" of what actually happens should not be
        handled in any Event classes.

        """
        raise NotImplementedError("Implemented in a subclass")


class RiderRequest(Event):
    """A rider requests a driver.

    === Attributes ===
    rider: The rider.
    """

    rider: Rider

    def __init__(self, timestamp: int, rider: Rider) -> None:
        """Initialize a RiderRequest event.

        """
        super().__init__(timestamp)
        self.rider = rider

    def do(self, dispatcher: Dispatcher, monitor: Monitor) -> List[Event]:
        """Assign the rider to a driver or add the rider to a waiting list.
        If the rider is assigned to a driver, the driver starts driving to
        the rider.

        Return a Cancellation event. If the rider is assigned to a driver,
        also return a Pickup event.

        """
        monitor.notify(self.timestamp, RIDER, REQUEST,
                       self.rider.id, self.rider.origin)

        events = []
        driver = dispatcher.request_driver(self.rider)

        if driver is not None:
            travel_time = driver.start_drive(self.rider.origin)
            events.append(Pickup(self.timestamp + travel_time,
                                 self.rider, driver))
        events.append(Cancellation(self.timestamp + self.rider.patience,
                                   self.rider))
        return events

    def __str__(self) -> str:
        """Return a string representation of this event.

        """
        return "{} -- {}: Request a driver".format(self.timestamp, self.rider)


class DriverRequest(Event):
    """A driver requests a rider.

    === Attributes ===
    driver: The driver.
    """

    driver: Driver

    def __init__(self, timestamp: int, driver: Driver) -> None:
        """Initialize a DriverRequest event.

        """
        super().__init__(timestamp)
        self.driver = driver

    def do(self, dispatcher: Dispatcher, monitor: Monitor) -> List[Event]:
        """Register the driver, if this is the first request, and
        assign a rider to the driver, if one is available.

        If a rider is available, return a Pickup event.

        """
        # Notify the monitor about the request.

        # Request a rider from the dispatcher.
        # If there is one available, the driver starts driving towards the
        # rider, and the method returns a Pickup event for when the driver
        # arrives at the riders location.

        monitor.notify(self.timestamp, DRIVER, REQUEST,
                       self.driver.id, self.driver.location)

        if self.driver not in dispatcher.drivers:
            dispatcher.drivers.append(self.driver)

        events = []
        if dispatcher.waiting_list:
            rider = dispatcher.waiting_list.pop()
            self.driver.start_drive(rider.origin)
            travel_time = self.driver.get_travel_time(rider.destination)
            events.append(Pickup(self.timestamp + travel_time, rider,
                                 self.driver))
        return events

    def __str__(self) -> str:
        """Return a string representation of this event.

        """
        return "{} -- {}: Request a rider".format(self.timestamp, self.driver)


class Cancellation(Event):
    """
    Dispatcher attempts to cancel rider's request.
    """
    time: int
    rider: Rider

    def __init__(self, timestamp: int, rider: Rider) -> None:
        super().__init__(timestamp)
        self.rider = rider

    def do(self, dispatcher: Dispatcher, monitor: Monitor) -> List[Event]:
        """
        Sets status to 'cancelled' if rider's status is waiting,
         (wasn't picked up), and notifies the monitor.
        """

        if self.rider.status is SATISFIED:
            return []

        monitor.notify(self.timestamp, RIDER, CANCEL,
                       self.rider.id, self.rider.origin)

        if self.rider.status is WAITING:
            self.rider.status = "cancelled"

        return []

    def __str__(self) -> str:
        """Return a string representation of this event.

        """
        return "Cancellation of " + str(self.rider.id, self.rider.status)


class Pickup(Event):
    """
    Driver attempts to pickup rider.
    """
    arrival_time: int
    rider: Rider
    driver: Driver

    def __init__(self, timestamp: int, rider: Rider, driver: Driver) -> None:
        super().__init__(timestamp)
        self.rider = rider
        self.driver = driver

    def do(self, dispatcher: Dispatcher, monitor: Monitor) -> List[Event]:
        """
        If rider's request hasn't been cancelled (if it has calls failure),
        sets the rider's status to satisfied, updates drivers destination
        location to that of the rider, and begins Dropoff event.
        """

        if self.rider.status is not CANCELLED:
            monitor.notify(self.timestamp, RIDER, PICKUP,  # notify rider
                           self.rider.id, self.rider.origin)

            self.rider.status = "satisfied"
            self.driver.destination = self.rider.destination
            return [Dropoff(self.timestamp + self.driver.get_travel_time(
                self.driver.destination), self.rider, self.driver)]
        return [self.failure()]

    def failure(self) -> Event:
        """
        Sets driver's destination to none and returns
        DriverRequest.
        """
        self.driver.is_idle = True
        self.driver.destination = None
        return DriverRequest(self.timestamp, self.driver)

    def __str__(self) -> str:
        """Return a string representation of this event.

        """
        return "Pickup at " + str(self.timestamp) + " of " \
               + str(self.rider.id) + " by " + str(self.driver.id)


class Dropoff(Event):
    """
    Driver drops off rider at their destination.
    """
    rider: Rider
    driver: Driver

    def __init__(self, timestamp: int, rider: Rider, driver: Driver) -> None:
        super().__init__(timestamp)
        self.rider = rider
        self.driver = driver

    def do(self, dispatcher: Dispatcher, monitor: Monitor) -> List[Event]:
        """
        Sets driver's location to the rider's destination, notifies monitor,
        sets the driver's destination to None and returns a new DriverRequest.
        """
        monitor.notify(self.timestamp, DRIVER, DROPOFF,
                       self.driver.id, self.rider.destination)

        self.driver.location = self.rider.destination
        self.driver.destination = None
        self.driver.is_idle = True
        return [DriverRequest(self.timestamp, self.driver)]

    def __str__(self) -> str:
        """Return a string representation of this event.

        """
        return "Drop off at " + str(self.timestamp) + " of " \
               + str(self.rider.id) + " by " + str(self.driver.id)


def create_event_list(filename: str) -> List[Event]:
    """Return a list of Events based on raw list of events in <filename>.

    Precondition: the file stored at <filename> is in the format specified
    by the assignment handout.

    filename: The name of a file that contains the list of events.
    """
    events = []
    with open(filename, "r") as file:
        for line in file:
            line = line.strip()

            if not line or line.startswith("#"):
                # Skip lines that are blank or start with #.
                continue

            # Create a list of words in the line, e.g.
            # ['10', 'RiderRequest', 'Cerise', '4,2', '1,5', '15'].
            # Note that these are strings, and you'll need to convert some
            # of them to a different type.
            tokens = line.split()
            timestamp = int(tokens[0])
            event_type = tokens[1]
            event = None

            # HINT: Use Location.deserialize to convert the location string to
            # a location.

            if event_type == "DriverRequest":
                driver = Driver(tokens[2], deserialize_location(tokens[3]),
                                int(tokens[4]))
                event = DriverRequest(timestamp, driver)

            elif event_type == "RiderRequest":
                rider = Rider(tokens[2], int(tokens[5]),
                              deserialize_location(tokens[3]),
                              deserialize_location(tokens[4]))
                event = RiderRequest(timestamp, rider)

            events.append(event)

    return events


if __name__ == '__main__':
    import python_ta
    python_ta.check_all(
        config={
            'allowed-io': ['create_event_list'],
            'extra-imports': ['rider', 'dispatcher', 'driver',
                              'location', 'monitor']})
