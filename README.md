Panopto-Java-Util
=================

This is a library of commonly used stuff throughout the various command line tools, you will need to compile this set of code as a jar which you can then include as a library in the other command line tools. At the very least it saves you having to have AXIS stub files all over the place!

Libraries required to compile
-----------------------------

* Apache AXIS... we used version 1.6.2

How to create your own axis stubs
---------------------------------

Please follow the instructions in the [building block readme](https://github.com/andmar8/Blackboard-Java-BB9.1-Plugin-for-Panopto/blob/master/README.md) for creating AXIS 1.6.2 stubs, assuming you create com.panopto.XXX packages you will then need to place com and underlying directories in the src directory (at the same level as the current "uk" directory).

The stubs included here have had their URL's cleaned and may not work, but they show you what you should end up with

