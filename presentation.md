GitHub Copilot: Our experience in the wild
==========================================

Paolo Carrasco

_(with the inputs of Claudia Lértora and Irene Torres)_

---

Main goal
---------

Share some techniques when using Copilot with IntelliJ using TDD.

What is the expected outcome?
-----------------------------

Attendants can apply the techniques in their projects if they use IntelliJ.

---

Agenda
------

- Presentation of the project example
- Testing
- Code generation
- Refactoring
- Documenting
- Bonus!
- Summary

---

Presentation of the project example
-----------------------------------

- This is a project based on some technologies we used at our previous client.
- It is a Rest API for knowing the weather.
- It uses the Open-Meteo API.
- The location now is hardcoded.
- We can obtain it from another API (GeoCoding API from [Open-Meteo](https://open-meteo.com/en/docs/geocoding-api))

---

Testing
-------

- Let's create our test with Copilot!
- Copilot can help us to do TDD :-)
- Giving context on the style of testing
- Hallucination is not bad!
- Doing parameterization

---

Code generation
---------------

Some techniques for generating code:

- zero-shot prompting (using the name of the method or context)
- one-shot prompting (using a single example)

---

Refactoring
-----------

Copilot has limitations for the refactoring, as it is applied only to the current file.

But! we can use it along with IntelliJ to fulfill what the IDE provides B-)

---

Documenting
-----------

- Copilot can help us to document our code.
- We can use it to generate the Javadoc.

---

Bonus!
------

- Copilot can answer some coding questions in IntelliJ too!

---

Summary
-------

- Copilot is a tool that can help us to be more productive.
- We can use it to help us to do TDD.
- We can use it to generate code.
- We can use it to refactor code.
- We can use it to document code.
- We can use it to answer coding questions.

---

__Thanks!__

Now let's go with questions! 
