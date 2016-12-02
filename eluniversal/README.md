### EL Universal Trust Project Source transparency [SeeSource]
### Trust Project Hackathon

### BUILDING THE TOOL. SeeSource for platforms

* A service to validate metatags [sources] (sourcename, sources).
* Recognize if a link contains metatags based on phpQuery.
* Implements a Bandage to identify that contents is available to be trust by readers.
* Users having references to their content can put this bandage to advise readers that also its references can be “trustable”

### The Problem

Audiences are asked to blindly trust news stories without understanding where the news comes from.

Also ...

Journalists usually do not ‘show their workings’ (especially if the workings aren’t that great).

Ordinary news users may not have the experience or time to identify the difference between sources based on the information available in most stories.

### How? SeeSource:
Newsroom CMS + Audience News Literacy Tool

* Journalists enter basic information about their sources (Plug in and drupal module)
* Highlight sources in text
* Provide source details popup window
* Solicit ranking of source quality from audience
* Simple newsroom tool that capitalises on work journalists already do


### Hypothesized effect on audience

* Increase in perceived transparency of organization
* Increase in news literacy
* Increased trust in organization


### Benefit to newsroom

* Increase transparency.
* Demonstrate source diversity and categorize sources.
* Directly engage with audience.
* Demonstrates your newsroom is the original collector of these sources. (Like signing a document stating this compromise.)

### Can SeeSource be gamed?

* No mechanism for peer review, lying is still possible.
* BUT, greater transparency gives the audience more information to assess the quality of the content. The tool makes lying harder,  raises the cost of doing it.

### Demo

* Sample: http://api.eluniversal.com.mx/trust/sample_note.html
* Demo for validating url: http://api.eluniversal.com.mx/trust/validate_test.php

### TO DO

Several features need development to establish an MVP:

* Portable module for implementation in several CMS.
* Validate data storage from users.
* Integrate standards for info in module.
* Integrate several formats for WebService output bandage [xml, json, html].
* Integrate capability to specify which "standard" protocol should be validated.