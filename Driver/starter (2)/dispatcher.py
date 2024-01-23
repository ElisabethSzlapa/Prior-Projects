"""Dispatcher for the simulation"""

from typing import Optional
from driver import Driver
from rider import Rider


class Dispatcher:
    """A dispatcher fulfills requests from riders and drivers for a
    ride-sharing service.

    When a rider requests a driver, the dispatcher assigns a driver to the
    rider. If no driver is available, the rider is placed on a waiting
    list for the next available driver. A rider that has not yet been
    picked up by a driver may cancel their request.

    When a driver requests a rider, the dispatcher assigns a rider from
    the waiting list to the driver. If there is no rider on the waiting list
    the dispatcher does nothing. Once a driver requests a rider, the driver
    is registered with the dispatcher, and will be used to fulfill future
    rider requests.
    """
    drivers: list
    waiting_list: list

    def __init__(self) -> None:
        """Initialize a Dispatcher.

        """
        self.drivers = []
        self.waiting_list = []

    def __str__(self) -> str:
        """Return a string representation.

        """
        return "Drivers: " + self.drivers + \
               ", Waiting list: " + self.waiting_list

    def request_driver(self, rider: Rider) -> Optional[Driver]:
        """Return a driver for the rider, or None if no driver is available.

        Add the rider to the waiting list if there is no available driver.

        """
        best_driver = None
        for driver in self.drivers:
            if driver.is_idle:
                if best_driver is None:
                    best_driver = driver
                elif (driver.get_travel_time(rider.origin)
                      < best_driver.get_travel_time(rider.origin)):
                    best_driver = driver

        if best_driver is not None:
            best_driver.is_idle = False

        else:
            self.waiting_list.append(rider)
            rider.status = "waiting"

        return best_driver

    def request_rider(self, driver: Driver) -> Optional[Rider]:
        """Return a rider for the driver, or None if no rider is available.

        If this is a new driver, register the driver for future rider requests.

        """
        if driver not in self.drivers:
            self.drivers.append(driver)

        if self.waiting_list:
            latest = self.waiting_list[0]
            self.waiting_list.remove(0)
            return latest
        return None

    def cancel_ride(self, rider: Rider) -> None:
        """Cancel the ride for rider.

        """
        rider.status = "canceled"
        if rider in self.waiting_list:
            self.waiting_list.pop(rider)


if __name__ == '__main__':
    import python_ta
    python_ta.check_all(config={'extra-imports': ['typing', 'driver', 'rider']})
