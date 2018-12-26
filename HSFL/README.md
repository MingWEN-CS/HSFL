This folder contains the implementations to reproduce the experimental results of HSFL.


1. To generate the results of RQ1, simply run `./runRQ1`
2. To generate the results of RQ2, simply run `./runRQ2`
3. To generate the results of RQ3, simply run `./runRQ3`
4. To generate the historical spectrum for a bug, simply run `./runConstructHistrum`


You can also customize the running of HSFL by specifying the configuration file as follows:

`java -jar HSFL.jar [configuration file]`

You need to specify the following configurations in the config file

1. `bugLoc`: the folder which stores the data for bugs. In our default settings, it is set as `../`
2. `task`: the task to run `[generateRQ1|generateRQ2|generateRQ3|construcHistrum]`
3. `project`: the name of the project `[Chart|Closure|Time|Lang|Math]`. *[This is required if you are running the task of `construcHistrum`]*.
4. `bid`: the bug id. *[This is required if you are running the task of `construcHistrum`]*.
5. `technique`: the technique `[ochiai|op2|tarantula|dstar|barinel]` used to generated the reuslts of HSFL based on the historical spectrum. *[This is required if you are running the task of `construcHistrum`]*.

To learn how to set those configurations, please refer to the provided config files `config_RQ1.txt` and `config_constructHistrum.txt`

The following explains the files in this folder:

- `*_oracle.txt`: records the oracles (i.e., buggy statements) for each project. For example, `Chart_oracle.txt` records the oracles of project Chart. These oracles are provided by the existing work mentioned in the paper.
- `Defect4JInducingChange.csv`: this file contains the information of the bug-inducing commits for each bug.
- `HSFL.jar`: this is the implementations of HSFL, we will open source the source codes once the paper is accepted.
- `config_*`: configurations files
- `run*`: running scripts
