#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Sep 25 19:59:05 2020

@author: ted
"""
from Virtual_board import VBoard
from time import sleep
from time_heavy_functions import t1
from random import choice

def random_move(board, color = 'b'):
    c1, c2, new_board = choice(board.get_next_boards(color = color))
    return c1, c2

if __name__ == '__main__':
    
    
    pass

