#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Sep 13 20:57:06 2020

@author: ted
"""
import numpy as np


def in_board_space(xy, a=0,b=7):
        if a-1 < xy[0] < b+1 and a-1 < xy[1] < b+1: 
            return True
        else: 
            return False;

class Piece():
    def __init__(self, color_name, coords):
        self.color_name = color_name
        self.color = color_name[0]
        self.name = color_name[1]
        self.coords = coords
    def moves(self):#, friendlies, enemies):
        out = []
        if self.coords is None:
            return None
        elif self.name is 'n':
            for x,y in [(2,1),(2,-1), (1,2),(1,-2), (-1,2),(-1,-2), (-2,1), (-2,-1)]:
                out0 = np.array([x,y]) + self.coords
                if in_board_space(out0):
                    out.append(out0)
        elif self.name is 'p':
            sign = {'b':1,'w':-1}[self.color]
            for xy in [(sign,0), (sign,1),(sign,-1)]:
                out0 = np.array(xy) + self.coords
                if in_board_space(out0):
                    out.append(out0)
        else:
            out = [self.coords]
        return out
                    
         
            
    
if __name__ == '__main__':
    
    
    pass

