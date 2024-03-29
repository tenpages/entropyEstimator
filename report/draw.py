import matplotlib.pyplot as plt
import numpy as np
x = [1,2,3,4]
#xname = [0.02,0.1,0.2,0.4,1.0,2.0]
xname = [0.1,0.2,0.5,0.8]
#y0 = [7.0812539,4.770184196,3.766914635,2.77234657,1.502044657,0.668422305]
#y1 = [7.082049634,4.771676083,3.767176947,2.773255707,1.502236879,0.668332622]
#std1 = [0.007359063,0.006426952,0.006178896,0.007261845,0.002536469,9.64E-04]
#t1 = [190.114,193.290,190.610,192.889,191.991,189.133]
#y2 = [7.083002096,4.768530929,3.765988196,2.770046393,1.527842378,0.695035285]
#std2 = [0.00712064,0.007257608,0.006614981,0.007515428,0.003612133,0.00106796]
#t2 = [2.062,2.685,3.541,4.525,4.909,3.657]

#std1 = [0.005975383,0.014232693,0.03979863,0.053097419]
#std2 = [0.00698302,0.013495464,0.03701151,0.053253849]
t1 = [277.425,67.504,10.776,4.174]
t2 =[8.197,.711,0.054,0.019]

#t1 = [7.442,7.446,7.437,7.438]
#t2 = [.052,.036,.036,.035]

#std1 = [0.013232017,0.013383537,0.016461443,0.017209973]
#std2 = [0.016237264,0.018435076,0.024171468,0.025291472]
#t1 = [10.659,7.439,5.557,4.227]
#t2 = [.073,.038,.021,.021]

#std1 = [2.10E-03,0.001975599,0.003041194,0.002701608]
#std2 = [0.002603517,0.002555301,0.003293731,0.002531161]
#t1 = [10.388,22.805,127.388,279.569]
#t2 = [4.892,6.344,8.623,9.992]

#plt.xticks(x,xname)
#plt.plot(x,y0,".")
#plt.errorbar(x,y1,yerr=std1)
#plt.errorbar(x,y2,yerr=std2)
#plt.plot(xname,std1,".-",label="orinial")
plt.plot(xname,t1,".-",label="orinial")
#plt.legend(loc="upper right")
#plt.xlabel("Distribution parameter $\lambda$")
#plt.ylabel("Average running time")
#plt.show()
#plt.plot(xname,std2,".-",label="fast")
plt.plot(xname,t2,".-",label="fast")
plt.legend(loc="upper right")
plt.xlabel("Accuracy parameter $\epsilon$")
plt.ylabel("Average running time (s)")
#plt.ylabel("Standard error of estimations")
plt.show()