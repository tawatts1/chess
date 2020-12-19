import os
import numpy as np
import matplotlib.pyplot as plt

       
def get_data(fname):
    out = []
    with open(fname, 'r') as f:
        for line in f:
            datum = []
            for entry in line.split(','):
                datum.append(float(entry))
            out.append(datum)
    return np.array(out)

files = os.listdir("game_results")



for file in files:
    if file[0] != '.':
        print(file)
        data = get_data(f"game_results/{file}")
        fig, (ax1, ax2) = plt.subplots(1,2)
        fig.suptitle(file)
        mean = np.mean(data[:,0])
        print(np.shape(data))
        # deviation for 95 pct confidence interval:
        dev = 1.96*np.std(data[:,0])/ np.sqrt( np.shape(data)[0] )
        c0,c1 = mean-dev, mean+dev
        
        ax1.hist(data[:,0])
        ax1.set_title("White performance")
        #ax1.figtext(.5,.01,f"{file} and such and such")
        ax2.hist(data[:,1])
        ax2.set_title("Game length")
        #plt.figtext(.5,.01,f"{file} and such and such")
        plt.figtext(.5,.03,f"The mean of white's performance is {mean:.3f}, with  CI ({c0:.3f}, {c1:.3f}). ", wrap=True, ha="center")
        plt.savefig("images/" + file+".png", dpi = 300)
        #plt.show()

 