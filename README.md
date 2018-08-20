
Test-spec is a collection of general purpose Cucumber steps (implemented in Groovy).
Gherkin features have dual purpose: 
* they are being a specification for some feature under development
* they are being a set of instructions for a functional test that could be executed automatically 


**What does test-spec solves?**

Traditionally, in large organizations software had been tested manually by a dedicated QA team.
While it has some merits, this approach suffers from serious limitations, 
main one is being - inability to speed up testing phase after a certain point. 
As application grows larger, it requires more time to perform manual testing on every release cycle.
To keep the quality of the product up, regression testing is required before each release.

One obvious solution to this situation is to hire more manual QA testers.
Beside the fact that it's expensive, after the team grows to a certain point, 
you'll start seeing diminishing returns on each added QA team member.
Even if you hire more QA, large application still take considerable amount of time to test,
which prolongs your release cycles. 

Another solution is to automate functional testing.
Here comes the dilemma though: test automation _is_ software development.
But your manual QA testers - as a rule - are not.  
Teams that came to conclusion to automate, implement it differently: 
* some try to train QAs to become programmers
* others hire programmers to do automation

Test-spec enabled QAs to automate functional test scenarios _without becoming "full-blown" programmers_.
Of course it can also be used by developers, as it simply saves time that developers would spend on coding test scenarios.  


**Functionality**

Test-spec makes testing easier by implementing commonly used operation by encapsulating them into Cucumber steps.
Eventually test-spec will do:
* web testing:
    * clicking on web page, 
    * entering values into input fields, 
    * checking web page content
* making REST calls
* querying DB (to validate outcome of some operation)
* working with files
    * check content
    * create a files based on a template
* sending JMS messages
* execute external scripts
* execute user code as extension of the framework, 
* while still allowing users to create their own custom steps 

Developers either need to write functional tests themselves.
In many real world environments DEV teams do not have a dedicated QA team.
