Frequency Server
================

This project contains a simple Web Service written in Java, inspired by the
example with the same name from the book _Erlang Programming_ by Francesco
Cesarini and Simon Thompson.

# Usage

It does not require any special configuration or database, all state is
kept in memory. The actions implemented are:

* __`StartServer`__ - it starts the server with all the frequencies free.

* __`StopServer`__ - it stops the server and clears information about frequencies.

* __`AllocateFrequency`__ - returns a frequency if available.

* __`DeallocateFrequency`__ - expects a frequency and adds it to the list of
available.

All URLs expect POST requests, and all of them return in response XML formatted
as specified by `response.xsd`.

