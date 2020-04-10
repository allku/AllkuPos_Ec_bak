@echo off
set DIRNAME=%~dp0
set CP="%DIRNAME%AllkuPos_Ec.jar"
start /B javaw -cp %CP% com.unicenta.orderpop.OrderPop