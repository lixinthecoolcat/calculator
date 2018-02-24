# calculator
A simple command-line calculator: take command line input as expression and generate result as output.

An expression is one of the of the following:
Numbers: integers between Integer.MIN_VALUE and Integer.MAX_VALUE
Variables: strings of characters, where each character is one of a-z, A-Z
Arithmetic functions: add, sub, mult, div, each taking two arbitrary expressions as arguments.  In other words, each argument may be any of the expressions on this list.
A “let” operator for assigning values to variables:
	let(<variable name>, <value expression>, <expression where variable is used>)
As with arithmetic functions, the value expression and the expression where the variable is used may be an arbitrary expression from this list.

Besides the operator functions, this system supports command line options: -h and -e
-h: print out help info
-e: set log level [ERROR|DEBUG|TRACE|INFO]. Default is DEBUG

Note: use fatJar task to build standalone executable jar file.
Note: for better user experience, this system running in loop. Use crl-c to quit.

