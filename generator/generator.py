import random

output = []

for i in range(10000):
	#We could use a gaussian generator for products so we simulate
	#a more common buying trend.
    output.append((random.randint(0,500),random.randint(0,100)))
    
output.sort(key=lambda x: x[0])

for i in output:
    print(str(i[0])+";"+str(i[1]))

