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
        elif self.name is 'n':# Knight
            for x,y in [(2,1),(2,-1), (1,2),(1,-2), (-1,2),(-1,-2), (-2,1), (-2,-1)]:
                out0 = np.array([x,y]) + self.coords
                if in_board_space(out0):
                    out.append(out0)
                    
        elif self.name is 'b': # Bishop
            for xy in [np.array([-1,1]), np.array([1,1])]:
                for multiplier in range(-8,9):
                    out0 = self.coords + multiplier*xy
                    if in_board_space(out0) and not (out0 == self.coords).all():
                        out.append(out0)
                        
        elif self.name is 'r': # Rook
            for xy in [np.array([1,0]), np.array([0,1])]:
                for multiplier in range(-8,9):
                    out0 = self.coords + multiplier*xy
                    if in_board_space(out0) and not (out0 == self.coords).all():
                        out.append(out0)
        elif self.name is 'q': # Queen
            for xy in [np.array([1,0]), np.array([0,1]), 
                       np.array([-1,1]), np.array([1,1])]:
                for multiplier in range(-8,9):
                    out0 = self.coords + multiplier*xy
                    if in_board_space(out0) and not (out0 == self.coords).all():
                        out.append(out0)
        elif self.name is 'k':
            for x in range(-1,2):
                for y in range(-1,2):
                    out0 = self.coords + np.array([x,y])
                    if in_board_space(out0) and not (out0 == self.coords).all():
                        out.append(out0)
            
        elif self.name is 'p': # Pawn
            sign = {'b':1,'w':-1}[self.color]
            for xy in [(sign,0), (sign,1),(sign,-1)]:
                out0 = np.array(xy) + self.coords
                if in_board_space(out0):
                    out.append(out0)
        else:
            out = [self.coords]
        try:
            out.remove(self.coords)
        except ValueError:
            pass
        return out
                    
         
            
    
if __name__ == '__main__':
    
    
    pass

