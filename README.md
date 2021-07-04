# S.A.F.E. Hallways
An unsupervised learning path-modeling Java software to significantly reduce COVID-19 transmission in the largest high school district in Illinois. Utilizes statistical distribution of paths to optimize grouped student dismissals according to social distancing guidelines. Approved by District Chief Technology Officer and District Assistant Superintendent for Instruction. Pilot study was approved, S.A.F.E. was deployed.

A corpus of student room schedule data is inputted, along with names and individual IDs.

S.A.F.E. creates the most likely paths and speeds for each student according to a Gaussian distribution, adding some noise to improve results. An algorithm analogous to stochastic gradient descent is used to connect these paths through the map of hallways and staircases.

A total of 10 epochs are conducted over the training data to fit the model as necessary.

A simulated video displaying tracer students on one floor of the building is shown below:
https://user-images.githubusercontent.com/85823366/124393730-6ff41c80-dcc1-11eb-9960-379cdfaa0302.mp4

