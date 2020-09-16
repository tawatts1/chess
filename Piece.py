#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Sep 13 20:57:06 2020

@author: ted
"""
import numpy as np


def in_board_space(self, xy, a=0,b=7):
        if a-1 < xy[0] < b+1 and a-1 < xy[1] < b+1: 
            return True
        else: 
            return False;

class Piece():
    def __init__(self, color, name, coords = None):
        self.color = color
        self.name = name
        self.coords = coords
    def moves(self, friendlies, enemies):
        out = []
        if self.coords is None:
            return None
        elif True:#self.name is 'r':
            for x,y in [(2,1),(2,-1), (1,2),(1,-2), (-1,2),(-1,-2), (-2,1), (-2,-1)]:
                out0 = np.array([x,y]) + self.coords
                if in_board_space(out0):
                    if out0 not in friendlies:
                        out.append(out0)
            print(out)
            return out
                    
         
            
    
if __name__ == '__main__':
    
    
    pass

