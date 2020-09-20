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
        self.has_moved = False
    
                    
         
            
    
if __name__ == '__main__':
    
    
    pass

