#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Sep 30 22:40:28 2020

@author: ted
"""
import tkinter as tk
from random import choice

def random_move(board, color = 'b'):
    c1, c2, new_board = choice(board.get_next_boards(color = color))
    return c1, c2
def f1():
    print(190000)

if __name__ == '__main__':
    
    pass


