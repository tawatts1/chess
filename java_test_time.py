import subprocess
import time



t0 = time.time()
string = "br_bn_bb_00_bk_bb_bn_br=00_00_bp_bp_00_bp_bp_bp=bp_bp_00_00_bp_bq_00_00=00_00_00_00_00_00_00_00=00_00_wb_00_00_00_wq_00=00_00_wp_00_wp_00_00_wp=wp_wp_00_wp_00_wp_wp_00=wr_wn_wb_00_wk_00_wn_wr"
file = 'next_move'

#print(string)
move =  subprocess.run(
                    ['java',file, string],
                    check=True, 
                    stdout=subprocess.PIPE).stdout.decode('ascii')
print(move)
#move = [int(x) for x in move.split(',')]
print('time: ', time.time()-t0)
