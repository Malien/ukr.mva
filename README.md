# MVA
Use ukranian as a programming language. This side-project of mine allows you to translate Ukranian language into fully-functional Python code (possibly more languages to come)
There is still a lot of pollishing needs to be done. (Such as refining syntax, adding number spelling support, and adding language features)
## Requirements
JDK 1.8 or heigher, Python 3
## Instalation
1. Copy this repository using "download to desktop" button on top of the page or using:`git clone https://github.com/Malien/ukr.mva.git`
2. Compile project using javac or your IDE of choice (no Maven, Ant or any other fancy build tool :sadface:)
3. Use provided shell scripts (`mva` and `mvac`) to run / compile your code.
`mva program.mva` to run program written in program.mva file.
`mvac program.mva out.py [options]` to compile program written in program.mva file to out.py file
4. If scripts won't work for you (and it's highly possible) you can simply run `java -cp <classpath> external.Translation [options]`. classpath needs to be set to path where project is compiled to

#### Options
  `--python` to compile to python code (the only language option at the moment)
  
  `--verbose-lex` to show output of lexical analyzer.
  
  `--verbose-syntax` to show output of syntax analyzer.
 ## Syntax
 You can write your good ol' Ukranian inside .mva files, but defining functions and statements as such:
 
 ```
 Оголошуємо функцію плюс5 над змінною а, що повертає сумму значення змінної а та 5.
 Вивести до консолі результат операції плюс5 над 7.
 ```
 or
 ```
 Оголосимо операцію привіт, що: виводить до консолі "Привіт"; виводить до консолі "Світ".
 Оголосимо функцію показатиСумму, над змінними а та б, що: 
     до змінної сумм внести сумму значення змінної а та значення змінної б, 
     виводить до консолі значення змінної сумм,
     виводить до консолі сумму значення змінної сумм та значення змінної а,
     повертає значення змінної сумм.
 ```
