"""Assignment 2: Trees for Treemap

=== CSC148 Fall 2020 ===
Diane Horton and David Liu
Department of Computer Science,
University of Toronto

=== Module Description ===
This module contains the basic tree interface required by the treemap
visualiser. You will both add to the abstract class, and complete a
concrete implementation of a subclass to represent files and folders on your
computer's file system.
"""

from __future__ import annotations
import os
from random import randint
import math

from typing import Tuple, List, Optional, Union


class AbstractTree:
    """A tree that is compatible with the treemap visualiser.

    This is an abstract class that should not be instantiated directly.

    You may NOT add any attributes, public or private, to this class.
    However, part of this assignment will involve you adding and implementing
    new public *methods* for this interface.

    === Public Attributes ===
    data_size: the total size of all leaves of this tree.
    colour: The RGB colour value of the root of this tree.
        Note: only the colours of leaves will influence what the user sees.

    === Private Attributes ===
    _root: the root value of this tree, or None if this tree is empty.
    _subtrees: the subtrees of this tree.
    _parent_tree: the parent tree of this tree; i.e., the tree that contains
        this tree
        as a subtree, or None if this tree is not part of a larger tree.

    === Representation Invariants ===
    - data_size >= 0
    - If _subtrees is not empty, then data_size is equal to the sum of the
      data_size of each subtree.
    - colour's elements are in the range 0-255.

    - If _root is None, then _subtrees is empty, _parent_tree is None, and
      data_size is 0.
      This setting of attributes represents an empty tree.
    - _subtrees IS allowed to contain empty subtrees (this makes deletion
      a bit easier).

    - if _parent_tree is not empty, then self is in _parent_tree._subtrees
    """
    data_size: int
    colour: (int, int, int)
    _root: Optional[object]
    _subtrees: List[AbstractTree]
    _parent_tree: Optional[AbstractTree]

    def __init__(self: AbstractTree, root: Optional[object],
                 subtrees: List[AbstractTree], data_size: int = 0) -> None:
        """Initialize a new AbstractTree.

        If <subtrees> is empty, <data_size> is used to initialize this tree's
        data_size. Otherwise, the <data_size> parameter is ignored, and this
        tree's data_size is computed from the data_sizes of the subtrees.

        If <subtrees> is not empty, <data_size> should not be specified.

        This method sets the _parent_tree attribute for each subtree to self.

        A random colour is chosen for this tree.

        Precondition: if <root> is None, then <subtrees> is empty.
        """
        self._root = root
        self._subtrees = subtrees
        self._parent_tree = None

        # 1. Initialize self.colour and self.data_size,
        # according to the docstring.

        self.colour = (randint(0, 255), randint(0, 255), randint(0, 255))
        if len(subtrees) == 0:
            self.data_size = data_size

        else:
            self.data_size = self.get_data_size()

        # 2. Properly set all _parent_tree attributes in self._subtrees
        self.parent_initializer()

    def parent_initializer(self: AbstractTree) -> AbstractTree:
        """
        Goes through tree initializing parents of each leaf, except for the root
        which has no parent.
        """
        parent = self
        for subtree in self._subtrees:
            subtree._parent_tree = parent
            sub_parent = subtree
            for sub_subtree in subtree._subtrees:
                sub_subtree._parent_tree = sub_parent
                sub_subtree.parent_initializer()
        return self

    def get_data_size(self: AbstractTree) -> int:
        """
        Goes through tree initializing datasize of each node
        (the sum of those of its subtrees).
        """
        if self.is_empty():
            return 0

        if len(self._subtrees) == 0:
            return self.data_size

        data_size = 0
        for subtree in self._subtrees:
            data_size += subtree.get_data_size()
        self.data_size = data_size
        return data_size

    def is_empty(self: AbstractTree) -> bool:
        """Return True if this tree is empty."""
        return self._root is None

    def generate_treemap(self: AbstractTree, rect: Tuple[int, int, int, int])\
            -> List[Tuple[Tuple[int, int, int, int], Tuple[int, int, int]]]:
        """Run the treemap algorithm on this tree and return the rectangles.

        Each returned tuple contains a pygame rectangle and a colour:
        ((x, y, width, height), (r, g, b)).

        One tuple should be returned per non-empty leaf in this tree.

        @type self: AbstractTree
        @type rect: (int, int, int, int)
            Input is in the pygame format: (x, y, width, height)
        @rtype: list[((int, int, int, int), (int, int, int))]
        """

        # Read the handout carefully to help get started identifying base cases,
        # and the outline of a recursive step.
        #
        # Programming tip: use "tuple unpacking assignment" to easily extract
        # coordinates of a rectangle, as follows.
        # x, y, width, height = rect

        leaf_rectangles = self.tree_iterator(rect, rect[0], rect[1], [])
        rectangles = []
        for rectangle in leaf_rectangles:
            rectangles.append((rectangle[0], rectangle[1]))
        return rectangles

    def tree_iterator(self, rect: Tuple[int, int, int, int], width: int,
                      height: int,
                      rectangles: List[Optional[Tuple[int, int, int, int]]])\
            -> List[Optional[Tuple[Tuple[int, int, int, int],
                                   Tuple[int, int, int],
                                   AbstractTree]]]:

        """
        Iterates through tree's content, creating subtrees proportional to the
        size of the node, returning a list of rectangles.
        """

        if self.data_size == 0:
            return []

        if self.data_size > 0 and len(self._subtrees) == 0:
            return [(rect, self.colour, self)]

        if self.data_size > 0 and len(self._subtrees) == 1 and \
                len(self._subtrees[0]._subtrees) == 0:
            return [(rect, self._subtrees[0].colour, self._subtrees[0])]

        if rect[2] > rect[3]:
            width, height, rect, rectangles = \
                self.width_greater_than_height(width, height, rect, rectangles)
        else:
            total_data_size = 0
            total_subtrees = len(self._subtrees)
            subtree_count = 0
            sectional_height = 0

            for subtree in self._subtrees:
                total_data_size += subtree.data_size

            if total_data_size == 0:
                return []
            for subtree in self._subtrees:
                if subtree_count == total_subtrees - 1:
                    rect_height = rect[3] - sectional_height
                else:
                    rect_height = math.floor(
                        subtree.data_size * rect[3] / total_data_size)

                rectangles += subtree.tree_iterator((width, height,
                                                     rect[2],
                                                     rect_height),
                                                    width, height,
                                                    rectangles)

                rectangles = double_clear(rectangles)
                subtree_count += 1
                height += rect_height
                sectional_height += rect_height

        return double_clear(rectangles)

    def get_separator(self: AbstractTree) -> str:
        """Return the string used to separate nodes in the string
        representation of a path from the tree root to a leaf.

        Used by the treemap visualiser to generate a string displaying
        the items from the root of the tree to the currently selected leaf.

        This should be overridden by each AbstractTree subclass, to customize
        how these items are separated for different data domains.
        """
        raise NotImplementedError

    def width_greater_than_height(self, width: int, height: int,
                                  rect: Tuple[int, int, int, int],
                                  rectangles:
                                  Union[list,
                                        List[Tuple[int, int, int, int]]]) \
            -> Union[int, int, Tuple[int, int, int, int],
                     Union[list, List[Tuple[int, int, int, int]]]]:
        """
        Called when width of the rectangle to fill is greater than height,
        iterates through subtrees producing rectangles by calling tree_iterator
        """
        total_data_size = 0
        total_subtrees = len(self._subtrees)
        subtree_count = 0
        sectional_width = 0

        for subtree in self._subtrees:
            total_data_size += subtree.data_size

        if total_data_size != 0:
            for subtree in self._subtrees:
                if subtree_count == total_subtrees - 1:
                    rect_width = rect[2] - sectional_width
                else:
                    rect_width = math.floor(
                        subtree.data_size * rect[2] / total_data_size)

                rectangles += subtree.tree_iterator((width, height,
                                                     rect_width, rect[3]),
                                                    width, height,
                                                    rectangles)
                rectangles = double_clear(rectangles)
                subtree_count += 1
                width += rect_width
                sectional_width += rect_width

        return width, height, rect, rectangles

    def click(self, position: Tuple[int, int],
              button: int, screen: Tuple[int, int, int, int],
              prior: AbstractTree) -> \
            Union[AbstractTree, Tuple[str, Optional[AbstractTree]], None]:
        """
        Responds to user clicking interface,
        removing a leaf from the parent tree if right clicked, returning the
        updated tree, and returning a tuple of the string route to a leaf and
        the leaf itself if left clicked.
        """
        rectangles = self.tree_iterator(screen, 0, 0, [])
        leaf = None
        for rectangle in rectangles:
            if (rectangle[0][0] <= position[0] <= rectangle[0][2]
                    + rectangle[0][0]) and (rectangle[0][1]
                                            <= position[1] <= rectangle[0][3]
                                            + rectangle[0][1]):
                leaf = rectangle[2]
                break
        if button == 1 and leaf is not None:
            if leaf == prior:
                return "", None
            return str(leaf.data_size) + " " + self.path_finder(leaf)[0], leaf

        if button == 3 and leaf is not None:
            leaf._parent_tree._subtrees.remove(leaf)
            leaf._parent_tree.data_size -= leaf.data_size
            self.get_data_size()
            self.parent_initializer()
            return self

        return None

    def button_press(self, val: str, leaf: FileSystemTree) -> Optional[int]:
        """
        Returns new datasize for a leaf after an up or down arrowkey is pressed,
        increasing datasize by 10% if its up, lowering it by 10% if it's down
        """
        if val == 'up':
            return math.ceil(leaf.data_size / 100) + leaf.data_size

        if val == 'down':
            new_leaf_data = leaf.data_size - math.ceil(leaf.data_size / 100)
            if new_leaf_data >= 1:
                return new_leaf_data
            return None
        return None

    def path_finder(self, leaf: AbstractTree) -> \
            Tuple[str, List[Optional[FileSystemTree]]]:
        """
        Returns a string representation of the path taken to reach a file as
        well as a list of its ancestor files in order, with it at the end.
        """
        curr_leaf = leaf
        path = []
        while curr_leaf._parent_tree is not None:
            path.insert(0, curr_leaf)
            curr_leaf = curr_leaf._parent_tree

        str_path = str(curr_leaf._root)
        for item in path:
            str_path = str_path + self.get_separator() + str(item._root)

        path.insert(0, curr_leaf)

        return str_path, path


class FileSystemTree(AbstractTree):
    """A tree representation of files and folders in a file system.

    The internal nodes represent folders, and the leaves represent regular
    files (e.g., PDF documents, movie files, Python source code files, etc.).

    The _root attribute stores the *name* of the folder or file, not its full
    path. E.g., store 'assignments', not '/Users/David/csc148/assignments'

    The data_size attribute for regular files as simply the size of the file,
    as reported by os.path.getsize.
    """
    def __init__(self: FileSystemTree, path: str) -> None:
        """Store the file tree structure contained in the given file or folder.

        Precondition: <path> is a valid path for this computer.
        """
        # Remember that you should recursively go through the file system
        # and create new FileSystemTree objects for each file and folder
        # encountered.
        #
        # Also remember to make good use of the superclass constructor!

        tree = self.file_recursor(AbstractTree(os.path.basename(path), []),
                                  path)

        self._root = tree._root
        self._subtrees = tree._subtrees
        self.data_size = tree.get_data_size()
        self._parent_tree = tree.parent_initializer()

    def file_recursor(self, tree: AbstractTree, d: str) -> AbstractTree:
        """
        Recurses through directory data and builds an AbstractTree out of it.
        """

        sub_tree = AbstractTree(os.path.basename(d), [], os.path.getsize(d))
        for filename in os.listdir(d):
            sub_tree._root = os.path.basename(d)
            subitem = os.path.join(d, filename)
            if os.path.isdir(subitem):
                self.file_recursor(sub_tree, subitem)

            else:
                sub_tree._subtrees.append(AbstractTree(filename, [],
                                                       os.path.getsize(d)))

        tree._subtrees.append(sub_tree)
        if len(tree._subtrees) > 0:
            return tree._subtrees[0]

        return tree

    def get_separator(self: AbstractTree) -> str:
        """
        Returns system specific separator
        """
        return os.sep


def double_clear(rectangles: Tuple[Tuple[int, int, int, int]]) -> List:
    """
    Removes any "doubled" rectangles from a given list, returning a new list
    with a single occurrence of each rectangle
    """
    cleaned_list = []

    for rectangle in rectangles:
        if rectangle not in cleaned_list:
            cleaned_list.append(rectangle)

    return cleaned_list


if __name__ == '__main__':
    import python_ta
    python_ta.check_all(
        config={
            'extra-imports': ['os', 'random', 'math'],
            'generated-members': 'pygame.*'})
