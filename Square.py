#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Sep 12 14:53:51 2020

@author: ted
"""
import tkinter as tk
from tkinter import ttk

class Square(ttk.Frame):
    def __init__(self, parent, img = None, color = 'grey', 
                 index = None, callback = None):
        super().__init__(parent)
        
        self.parent = parent
        self.index = index
        if img is None:
            photo = tk.PhotoImage()
        else:    
            photo = tk.PhotoImage(file = img)
         
        self.button = tk.Button(self, image = photo, bg = color, 
                                height = 60, width = 60, command = callback)
        #self.button.config(height = 50, width = 50)
        self.button.grid(row=0, column=0)
        self.button.image = photo
        #self.config(bg = 'yellow')
        self.grid(row=0,column=0)
        
    
if __name__ == '__main__':
    
    root = tk.Tk()
    s1 = Square(root, img = 'images/bq.png')
    root.mainloop()
